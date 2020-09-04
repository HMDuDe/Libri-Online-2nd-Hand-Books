package com.koddev.chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.koddev.chatapp.Adapter.TransactionAdapter;
import com.koddev.chatapp.Model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class LibriTransactionHistory extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private Intent intent;
    private List<Transaction> transactions;
    private TransactionAdapter adapter;
    private DatabaseReference dbRef1;
    private FirebaseAuth auth;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libri_transaction_history);

        Log.d("TRANSAC HISTORY", "ON CREATE METHOD IN TRANSAC HISTORY");
        RecyclerView recyclerView = findViewById(R.id.transacRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        transactions = new ArrayList<>();
        count = 0;

        auth = FirebaseAuth.getInstance();
        dbRef1 = FirebaseDatabase.getInstance().getReference("transactions");

        dbRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                transactions.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Transaction currentTran = postSnapshot.getValue(Transaction.class);

                    if(currentTran.getBuyerID().equals(auth.getCurrentUser().getUid())){

                        Log.d("CURRENT TRANSACTION: ", "" + currentTran.getBooks());
                        transactions.add(currentTran);
                        count++;
                    }
                }

                adapter.notifyDataSetChanged();
                if(count < 1){
                    Toast.makeText(LibriTransactionHistory.this, "No transactions found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new TransactionAdapter(LibriTransactionHistory.this, transactions);
        recyclerView.setAdapter(adapter);

        //Bottom/top navigation pane
        ImageView home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LibriTransactionHistory.this, Home.class));
            }
        });

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LibriTransactionHistory.this, Cart.class));
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
                intent = new Intent(LibriTransactionHistory.this,ReportIncident.class);
                startActivity(intent);
                return true;
            case R.id.menu_messages:
                startActivity(new Intent(LibriTransactionHistory.this, MainActivity.class));
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LibriTransactionHistory.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}