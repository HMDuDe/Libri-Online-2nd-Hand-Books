package com.koddev.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koddev.chatapp.Model.Upload;
import com.koddev.chatapp.Model.UserData;
import com.squareup.picasso.Picasso;

public class publicProfileView extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private Intent intent;  //Needs to getExtra userID of user being viewed
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private TextView nameSurnameTxt, totalAdsTxt, cityTxt, contactNoTxt;
    private String nameSurname, totalAds, city, contactNo;
    private ImageView img;
    private int total =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile_view);

        auth = FirebaseAuth.getInstance();

        String sellerID = getIntent().getStringExtra("sellerid");
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(sellerID).child("Details");  //needs reference of user

        nameSurnameTxt = findViewById(R.id.nameLbl3);
        totalAdsTxt = findViewById(R.id.numAdsPostedLbl);
        cityTxt = findViewById(R.id.userCityLbl);
        contactNoTxt = findViewById(R.id.contactNoLbl);
        img = findViewById(R.id.userProfileImg);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameSurnameTxt.setText(dataSnapshot.getValue(UserData.class).getNameSurname());
                //Total ads required also
                DatabaseReference aDataRef = FirebaseDatabase.getInstance().getReference("adverts");
                final String userid=getIntent().getStringExtra("sellerid");

                aDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Upload ad = postSnapshot.getValue(Upload.class);
                            if (ad.getSellerID().equals(userid)){
                                total++;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                totalAdsTxt.setText(String.valueOf(total));

                Log.d("IMG PATH: ", "" + dataSnapshot.getValue(UserData.class).getImgPath());
                Picasso.with(publicProfileView.this)
                        .load(dataSnapshot.getValue(UserData.class).getImgPath())
                        .into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).child("Details");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cityTxt.setText(dataSnapshot.getValue(UserData.class).getCity());
                contactNoTxt.setText(dataSnapshot.getValue(UserData.class).getContactNo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        configMsgBtn();
        configAdsBtn();

        ImageView home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(publicProfileView.this, Home.class));
            }
        });

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(publicProfileView.this, Cart.class));
            }
        });
    }

    private void configMsgBtn(){
        Button messages = (Button) findViewById(R.id.messagesBtn);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(publicProfileView.this, MessageActivity.class));
                //ask jarryd what is required
            }
        });
    }

    private void configAdsBtn(){
        Button ads = (Button) findViewById(R.id.userAdsBtn);
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(publicProfileView.this,Main_Buy.class);
                String seller = getIntent().getStringExtra("seller");
                String by = "Seller";
                intent.putExtra("search_parameters",seller);
                intent.putExtra("search_by",by);
                startActivity(intent);
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
                intent = new Intent(publicProfileView.this,ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:
                intent = new Intent(publicProfileView.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(publicProfileView.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}