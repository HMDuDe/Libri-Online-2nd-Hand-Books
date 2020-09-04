package com.koddev.chatapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koddev.chatapp.Cart;
import com.koddev.chatapp.Model.CartItem;
import com.koddev.chatapp.R;
import com.koddev.chatapp.Model.Upload;
import com.koddev.chatapp.View_ad;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHolder> {
    private Context mContext;
    private List<com.koddev.chatapp.Model.CartItem> CartItem;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    public CartAdapter(Context context, List<CartItem> cartItems) {
        mContext = context;
        CartItem = cartItems;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cartitem, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        final CartItem cart = CartItem.get(position);
        final long itemid  = cart.getItemid();
        final ImageViewHolder holder_ = holder;
        final String ad_id = cart.getAd_id();
        holder.cart_title.setText(cart.getTitle());
        holder.cart_quantity.setText(cart.getQuantity());
        holder.cart_price.setText("R"+ cart.getPrice());


        Glide.with(mContext)
                .load(cart.getImageURL())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.book_image);

        holder.cart_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.remove_confirmation);
                Button no = dialog.findViewById(R.id.no);
                Button yes = dialog.findViewById(R.id.yes);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "removing item", Toast.LENGTH_SHORT).show();
                        final DatabaseReference cref = FirebaseDatabase.getInstance().getReference("carts").child(firebaseUser.getUid());
                        cref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                cref.child(Long.toString(itemid)).removeValue(); //resolve removing overwriting items
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        final DatabaseReference dref = FirebaseDatabase.getInstance().getReference("adverts");
                        dref.addListenerForSingleValueEvent(new ValueEventListener() { // change ad status to on hold when in cart
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                        Upload upload = postSnapshot.getValue(Upload.class);
                                        if(ad_id.equals(upload.getId())){
                                            upload.setStatus("Available");
                                            dref.child(ad_id).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(mContext, "Item removed", Toast.LENGTH_SHORT).show();
                                                    mContext.startActivity(new Intent(mContext, Cart.class));
                                                }
                                            });
                                        }
                                    }}catch (Exception e){
                                    Toast.makeText(mContext, "" + e, Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    dialog.dismiss();
                    }
                });
            }
        });

        holder.cart_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference dref = FirebaseDatabase.getInstance().getReference("adverts");
                dref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Upload uploadCurrent = postSnapshot.getValue(Upload.class);
                            if(ad_id.equals(uploadCurrent.getId())) {
                                Intent intent = new Intent(mContext, View_ad.class);
                                intent.putExtra("ad_id", uploadCurrent.getId());
                                intent.putExtra("title", uploadCurrent.getTitle());
                                intent.putExtra("author", uploadCurrent.getAuthor());
                                intent.putExtra("status", uploadCurrent.getStatus());
                                intent.putExtra("condition", uploadCurrent.getCondition());
                                intent.putExtra("seller", uploadCurrent.getSeller());
                                intent.putExtra("price", uploadCurrent.getPrice());
                                intent.putExtra("date", uploadCurrent.getDate());
                                intent.putExtra("image", uploadCurrent.getImageURL());
                                mContext.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void removeItem(){


    }

    @Override
    public int getItemCount() {
        return CartItem.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView cart_title, cart_price, cart_quantity, cart_remove;
        public RelativeLayout cart;
        public ImageView book_image;



        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            cart_title = itemView.findViewById(R.id.cart_book_title);
            cart_price = itemView.findViewById(R.id.cart_book_price);
            cart_quantity = itemView.findViewById(R.id.cart_book_quantity);
            cart = itemView.findViewById(R.id.cart);
            book_image = itemView.findViewById(R.id.cart_book_image);
            cart_remove = itemView.findViewById(R.id.cart_book_remove_btn);

        }
    }
}
