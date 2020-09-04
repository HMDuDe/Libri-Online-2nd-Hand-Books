package com.koddev.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koddev.chatapp.Model.ProfileEdit;
import com.koddev.chatapp.Model.UserData;
import com.squareup.picasso.Picasso;

public class PrivateProfileView extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Intent intent;
    private DatabaseReference dbRef1, dbRef2;
    private FirebaseAuth auth;
    private TextView emailDisplLbl, contactNoLbl, streetLbl, suburbLbl, cityLbl, zipLbl, nameSurnameLbl;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_private_profile_view);

            emailDisplLbl = findViewById(R.id.emailDispLbl);
            contactNoLbl = findViewById(R.id.contactNoDispLbl);
            streetLbl = findViewById(R.id.streetDispLbl);
            suburbLbl = findViewById(R.id.suburbDispLbl);
            cityLbl = findViewById(R.id.cityDispLbl);
            zipLbl = findViewById(R.id.zipDispLbl);
            nameSurnameLbl = findViewById(R.id.nameSurnameLbl);

            img = findViewById(R.id.userProfileImage);
            auth = FirebaseAuth.getInstance();

            dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).child("Details");

            emailDisplLbl.setText(auth.getCurrentUser().getEmail());
            DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
            dbRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserData user = dataSnapshot.getValue(UserData.class);

                    Log.d("USER PROFILE IMG PATH", "" + dataSnapshot.getValue(UserData.class).getImgPath());
                    Glide.with(PrivateProfileView.this)
                            .load(dataSnapshot.getValue(UserData.class).getImgPath())
                            .into(img);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            dbRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    nameSurnameLbl.setText(dataSnapshot.getValue(UserData.class).getNameSurname());
                    contactNoLbl.setText(dataSnapshot.getValue(UserData.class).getContactNo());
                    streetLbl.setText(dataSnapshot.getValue(UserData.class).getStreet());
                    suburbLbl.setText(dataSnapshot.getValue(UserData.class).getSuburb());
                    cityLbl.setText(dataSnapshot.getValue(UserData.class).getCity());
                    zipLbl.setText(dataSnapshot.getValue(UserData.class).getZIP());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            configTransacBtn();
            configEditProfileBtn();

            //Buttons at bottom of the screen
            Button myAds = findViewById(R.id.userAdsBtn);
            myAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PrivateProfileView.this,MyAds.class));
                }
            });

            Button msgBtn = findViewById(R.id.msgBtn);
            msgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PrivateProfileView.this, MainActivity.class));
                }
            });

            ImageView home = findViewById(R.id.libri_logo);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PrivateProfileView.this, Home.class));
                }
            });
        }catch (Exception e){
            Log.d("Exception: ", "" + e);
        }
    }

    private void configTransacBtn(){
        Button transac = (Button) findViewById(R.id.transacHistBtn);
        transac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PrivateProfileView.this, LibriTransactionHistory.class));

            }
        });

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrivateProfileView.this, Cart.class));
            }
        });
    }

    private void configEditProfileBtn(){
        Button editprofile = (Button) findViewById(R.id.editProfileBtn);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PrivateProfileView.this, profileEditor.class));
            }
        });
    }

    //Menu bar code
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
                intent = new Intent(PrivateProfileView.this,ReportIncident.class);
                startActivity(intent);
                return true;
            case R.id.menu_messages:
                startActivity(new Intent(PrivateProfileView.this, MainActivity.class));
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PrivateProfileView.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}