package com.koddev.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Initial_Buy extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {
    private ImageButton search;
    private ImageView home;
    private String search_by, parameters;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial__buy);

        search = (ImageButton) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainBuy();
            }
        });

        home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        Spinner by = findViewById(R.id.search_by_button1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_item, getResources().getStringArray(R.array.book_field));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        by.setAdapter(adapter);
        by.setOnItemSelectedListener(this);

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Initial_Buy.this, Cart.class));
            }
        });

    }

    public void openMainBuy() {
        EditText search_parameters = findViewById(R.id.search_parameters);
        parameters = search_parameters.getText().toString().trim();
        intent = new Intent(this, Main_Buy.class);
        intent.putExtra("search_parameters",parameters);
        intent.putExtra("search_by",search_by);
        startActivity(intent);

    }

    public void openHome(){
        intent = new Intent(this, Home.class);
        startActivity(intent);
    }



    public void showPopUp(View v){
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
                intent = new Intent(Initial_Buy.this,ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:
                intent = new Intent(Initial_Buy.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Initial_Buy.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        search_by = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
