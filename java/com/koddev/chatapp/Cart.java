package com.koddev.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.koddev.chatapp.Adapter.CartAdapter;
import com.koddev.chatapp.Model.CartItem;
import com.koddev.chatapp.Model.Upload;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private Button checkout;
    private Intent intent;
    private RecyclerView cRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> CartItems;
    private DatabaseReference cDatabaseRef;
    private FirebaseStorage cStorageRef;
    private double total =0;
    private String booklist ="";
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private int count = 0;
    private  DatabaseReference aDatabaseRef = FirebaseDatabase.getInstance().getReference("adverts");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageView home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cart.this, Home.class));
            }
        });


        cRecyclerView = findViewById(R.id.cart);
        cRecyclerView.setHasFixedSize(true);
        cRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(Cart.this, CartItems);
        cRecyclerView.setAdapter(cartAdapter);

        loadCart();




        checkout = (Button) findViewById(R.id.but_checkout);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CartItems.isEmpty()){
                    intent = new Intent(Cart.this, Invoice.class);
                    intent.putExtra("total", total);
                    intent.putExtra("booklist", booklist);
                    startActivity(intent);
                }else {
                    Toast.makeText(Cart.this, "You don't have anything in your cart to purchase.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cart.this, Cart.class));
            }
        });

    }


    public void loadCart(){
        final ProgressBar ProgressCircle = findViewById(R.id.cart_progress_circle);
        cStorageRef = FirebaseStorage.getInstance();
        cDatabaseRef =  FirebaseDatabase.getInstance().getReference("carts").child(firebaseUser.getUid());//change to retrieve items from cart

        CartItems.clear();
        cDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final CartItem cartitem = postSnapshot.getValue(CartItem.class);
                    try {
                        if (cartitem.getBuyerid().equals(firebaseUser.getUid())) {

                            CartItems.add(cartitem);
                            total += Double.parseDouble(cartitem.getPrice());
                            if (count == dataSnapshot.getChildrenCount() - 1) {
                                booklist += cartitem.getTitle();
                            } else {
                                booklist += cartitem.getTitle() + ", ";
                            }
                            count++;

                        }

                    }catch(Exception e){
                        Toast.makeText(Cart.this, ""+e, Toast.LENGTH_SHORT).show();

                    }
                }
                if (count == 0){
                    Toast.makeText(Cart.this, "You haven't added anything to your cart yet", Toast.LENGTH_SHORT).show();
                }
                cartAdapter.notifyDataSetChanged();
                ProgressCircle.setVisibility(View.INVISIBLE);
                TextView derived_total = findViewById(R.id.total);

                derived_total.setText("R" + total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.menu_profile:
                intent = new Intent(this,PrivateProfileView.class);
                startActivity(intent);
                return true;
            case R.id.menu_help:
                intent = new Intent(Cart.this,ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:

                intent = new Intent(Cart.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Cart.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}
