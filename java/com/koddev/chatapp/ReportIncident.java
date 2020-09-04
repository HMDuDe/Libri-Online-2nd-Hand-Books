
package com.koddev.chatapp;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportIncident extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private DatabaseReference databaseReference;
    private EditText editText;
    private Button button2;
    private Spinner spinner1;
    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editText = (EditText)findViewById(R.id.editText);
        button2 = (Button)findViewById(R.id.button2);
        spinner1  = (Spinner) findViewById(R.id.spinner1);


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String desField = editText.getText().toString().trim();

                SaveToDataBase saveit = new SaveToDataBase(editText.getText().toString());    //  https://www.youtube.com/watch?v=wuYudQxFNwE


                databaseReference.child("Reported Incidents").push().setValue(saveit).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ReportIncident.this, "Incident submitted", Toast.LENGTH_LONG).show();
                    }
                });



                finish();




            }
        });



        //DROP DOWN LIST FOR INCIDENT// https://www.youtube.com/watch?v=urQp7KsQhW8
        Spinner mySpinner = findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>
                (ReportIncident.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.IncidentType));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
//END OF DROP DOWN LIST FOR INCIDENT//







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
                intent = new Intent(ReportIncident.this,ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:

                intent = new Intent(ReportIncident.this,MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ReportIncident.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}

