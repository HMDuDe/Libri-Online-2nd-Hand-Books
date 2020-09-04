package com.koddev.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koddev.chatapp.Model.CartItem;
import com.koddev.chatapp.Model.Upload;
import com.koddev.chatapp.Model.User;

import java.util.List;

public class View_ad extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private ImageView home, view_image;
    private TextView view_title, view_author,view_date, view_seller, view_price, view_condition,view_status ;
    private Intent intent;
    private List<CartItem> items;
    private long itemid;
    private DatabaseReference cref;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String cartid = firebaseUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad);

        home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_ad.this, Home.class));
            }
        });


        view_title = findViewById(R.id.view_title);
        final String title = getIntent().getStringExtra("title");
        view_title.setText(title);


        view_author = findViewById(R.id.view_author);
        String author = getIntent().getStringExtra("author");
        view_author.setText("by " + author);

        view_date = findViewById(R.id.view_date);
        String date = getIntent().getStringExtra("date");
        view_date.setText(date);

        view_seller = findViewById(R.id.view_seller);
        final String seller = getIntent().getStringExtra("seller");
        view_seller.setText(seller);

        view_price = findViewById(R.id.view_price);
        final String price = getIntent().getStringExtra("price");
        view_price.setText("R"+price);

        view_condition = findViewById(R.id.view_condition);
        String condition = getIntent().getStringExtra("condition");
        view_condition.setText(condition);

        view_status = findViewById(R.id.view_status);
        String status = getIntent().getStringExtra("status");
        view_status.setText(status);

        view_image = findViewById(R.id.view_image);
        final String image = getIntent().getStringExtra("image");

        Glide.with(this)
                .load(image)
                .placeholder(R.mipmap.ic_launcher)
                .into(view_image);



        final String sellerid = getIntent().getStringExtra("sellerID");
        Button contact = findViewById(R.id.btn_contact_seller);

        if(!firebaseUser.getUid().equals(sellerid))
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(View_ad.this, publicProfileView.class);
                intent.putExtra("sellerid",sellerid);
                intent.putExtra("seller",seller);
                startActivity(intent);
            }
        });else {
            contact.setVisibility(View.INVISIBLE);

        }
        final String ad_id = getIntent().getStringExtra("ad_id");
        final DatabaseReference dref = FirebaseDatabase.getInstance().getReference("adverts").child(ad_id);
        Button delete = findViewById(R.id.btn_delete);

        if(firebaseUser.getUid().equals(sellerid)&&status.equals("Available")) {
            delete.setVisibility(View.VISIBLE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(View_ad.this);
                dialog.setContentView(R.layout.delete_confirmation);
                Button no = dialog.findViewById(R.id.no);
                Button yes = dialog.findViewById(R.id.yes);
                dialog.show();
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     dref.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          Upload upload = dataSnapshot.getValue(Upload.class);
                          upload.setStatus("Deleted");
                             dref.setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {
                                     Toast.makeText(View_ad.this, "Advert successfully deleted but still available in my ads", Toast.LENGTH_LONG).show();
                                     startActivity(new Intent(View_ad.this, Home.class));
                                 }
                             });

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     })   ;
                    }
                });
            }
        });


        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_ad.this, Cart.class));
            }
        });


        cref = FirebaseDatabase.getInstance().getReference("carts").child(cartid);
        cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    itemid = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        final Button add_to_cart = findViewById(R.id.btn_add_to_cart);



        if(status.equals("Available")&&!firebaseUser.getUid().equals(sellerid)) {
            add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dref.addListenerForSingleValueEvent(new ValueEventListener() { // change ad status to on hold when in cart
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final Upload upload = dataSnapshot.getValue(Upload.class);

                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            String buyerID = user.getId();
                                            CartItem cartItem = new CartItem(itemid, title, price, image, ad_id, buyerID);
                                            cref.child(Long.toString(itemid)).setValue(cartItem);
                                            add_to_cart.setClickable(false);
                                            add_to_cart.setVisibility(View.INVISIBLE);
                                            upload.setStatus("On hold for " + user.getUsername());
                                            dref.setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(View_ad.this, "Textbook added to cart", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(View_ad.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else add_to_cart.setVisibility(View.INVISIBLE);
    }



    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_profile:
                intent = new Intent(this,PrivateProfileView.class);
                startActivity(intent);
                return true;
            case R.id.menu_help:
                //Toast.makeText(this, "Help selected", Toast.LENGTH_SHORT).show();
                intent = new Intent(View_ad.this,ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:
                intent = new Intent(View_ad.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(View_ad.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}

