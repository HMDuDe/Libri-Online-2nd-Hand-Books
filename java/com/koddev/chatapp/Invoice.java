package com.koddev.chatapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koddev.chatapp.Model.CartItem;
import com.koddev.chatapp.Model.Transaction;
import com.koddev.chatapp.Model.Upload;
import com.koddev.chatapp.Model.User;
import com.koddev.chatapp.Model.UserData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Invoice extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private Intent intent;
    private long transactionId = 0;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String address, booklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Invoice.this, Cart.class));
            }
        });
        ImageView home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Invoice.this, Home.class));
            }
        });


        booklist = getIntent().getStringExtra("booklist");
        final double total = getIntent().getDoubleExtra("total",0);


        final TextView txtAddress = findViewById(R.id.buyeraddress);
        DatabaseReference uref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Details");
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData ud = dataSnapshot.getValue(UserData.class);
                address = ud.getStreet() + "\n" + ud.getSuburb() + "\n" + ud.getCity() + "\n" + ud.getZIP();
                txtAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        TextView txtTotal = findViewById(R.id.invoice_total);
        txtTotal.setText("R" + total);

        final TextView orderno = findViewById(R.id.orderno);
        final DatabaseReference transRef = FirebaseDatabase.getInstance().getReference("transactions");
        transRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transactionId = dataSnapshot.getChildrenCount()-1;
                orderno.setText(""+transactionId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        TextView book = findViewById(R.id.itemname);
        book.setText(booklist);

        Button cont = findViewById(R.id.btn_continue);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              // creating the transaction
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date curDate = new Date();
                String date = dateFormat.format(curDate);
                String tot = Double.toString(total);
                String buyerID = firebaseUser.getUid();
                Transaction transaction = new Transaction(transactionId,date,tot,buyerID,booklist);

                  transRef.child(Long.toString(transactionId)).setValue(transaction);



                final DatabaseReference iref = FirebaseDatabase.getInstance().getReference("carts").child(firebaseUser.getUid());
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
                //changing ad status to sold
                iref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                            CartItem ci = postsnapshot.getValue(CartItem.class);
                            final String ad_id = ci.getAd_id();
                            final DatabaseReference mref = FirebaseDatabase.getInstance().getReference("adverts").child(ad_id);
                            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final Upload upload = dataSnapshot.getValue(Upload.class);
                                            databaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    User user = dataSnapshot.getValue(User.class);
                                                    upload.setStatus("Sold - purchased by " +user.getUsername());
                                                    mref.child(ad_id).setValue(upload);
                                                    iref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            intent = new Intent(Invoice.this, Receipt.class);
                                                            intent.putExtra("total", total);
                                                            intent.putExtra("booklist", booklist);
                                                            intent.putExtra("orderno", orderno.getText());
                                                            intent.putExtra("Address", address);
                                                            Toast.makeText(Invoice.this, "Redirecting to payment \n processing system", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent);
                                                        }
                                                    });   //removing items from cart
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
                //Toast.makeText(this, "Help selected", Toast.LENGTH_SHORT).show();
                intent = new Intent(Invoice.this,ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:
                intent = new Intent(Invoice.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Invoice.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }

}

