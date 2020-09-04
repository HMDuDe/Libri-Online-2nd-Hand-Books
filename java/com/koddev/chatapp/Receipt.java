package com.koddev.chatapp;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;

public class Receipt extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);


        ImageView home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Receipt.this, Home.class));
            }
        });

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Receipt.this, Cart.class));
            }
        });

        Button continue_to_home = findViewById(R.id.btn_continue_to_home);
        continue_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Receipt.this, "Details have been sent to courier", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Receipt.this, Home.class));
            }
        });


        final String booklist = getIntent().getStringExtra("booklist");
        final double total = getIntent().getDoubleExtra("total",0);
        final String orderno = getIntent().getStringExtra("orderno");
        String addresses = getIntent().getStringExtra("Address");


        TextView ordernum = findViewById(R.id.orderNoRec);
        ordernum.setText(orderno);

        TextView txtTotal = findViewById(R.id.TotalRec);
        txtTotal.setText("R" + total);

        TextView book = findViewById(R.id.bookNameRec);
        book.setText(booklist);

        TextView address = findViewById(R.id.addressRec);
        address.setText(addresses);
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
                intent = new Intent(Receipt.this,ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:
                intent = new Intent(Receipt.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Receipt.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}


