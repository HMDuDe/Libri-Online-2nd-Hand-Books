package com.koddev.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koddev.chatapp.Model.ProfileEdit;
import com.koddev.chatapp.Model.UserData;

import java.util.List;

public class profileEditor extends AppCompatActivity {

    private DatabaseReference libriDbRef1, libriRef2;
    private AdapterView.OnItemClickListener libriDbListener;
    private ProgressBar progressCircle;
    private Intent intent;
    private EditText emailDisplLbl, contactNoLbl, streetLbl, suburbLbl, cityLbl, zipLbl, usernameLbl, passwordLbl, confirmPasswordLbl;
    private String emailOriginal, contactNoOriginal, streetOriginal, suburbOriginal, cityOriginal, zipOriginal;
    private String emailDispl, contactNo, street, suburb, city, zip;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        emailDisplLbl = findViewById(R.id.editEmailTxt);
        contactNoLbl = findViewById(R.id.editContactNoTxt);
        streetLbl = findViewById(R.id.editStreetTxt);
        suburbLbl = findViewById(R.id.editSuburbTxt);
        cityLbl = findViewById(R.id.editCityTxt);
        zipLbl = findViewById(R.id.editZipTxt);

        auth = FirebaseAuth.getInstance();

        libriDbRef1 = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).child("Details");

        emailDisplLbl.setText(auth.getCurrentUser().getEmail());
        emailOriginal = String.valueOf(emailDisplLbl.getText());

        libriDbRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactNoLbl.setText(dataSnapshot.getValue(UserData.class).getContactNo());
                contactNoOriginal = String.valueOf(contactNoLbl.getText());

                streetLbl.setText(dataSnapshot.getValue(UserData.class).getStreet());
                streetOriginal = String.valueOf(streetLbl.getText());

                suburbLbl.setText(dataSnapshot.getValue(UserData.class).getSuburb());
                suburbOriginal = String.valueOf(suburbLbl.getText());

                cityLbl.setText(dataSnapshot.getValue(UserData.class).getCity());
                cityOriginal = String.valueOf(cityLbl.getText());

                zipLbl.setText(dataSnapshot.getValue(UserData.class).getZIP());
                zipOriginal = String.valueOf(zipLbl.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        configSaveBtn();

    }

    public void configSendEmailBtn(){
        Button sendEmailBtn = findViewById(R.id.sendEmailBtn);
        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail());
            }
        });
    }

    private void configSaveBtn(){
        Button save = (Button) findViewById(R.id.saveDataBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code for saving new data here:
                emailDispl = String.valueOf(emailDisplLbl.getText());
                contactNo = String.valueOf(contactNoLbl.getText());
                street = String.valueOf(streetLbl.getText());
                suburb = String.valueOf(suburbLbl.getText());
                city = String.valueOf(cityLbl.getText());
                zip = String.valueOf(zipLbl.getText());

                UserData updateUser = new UserData();

                //Email section:
                if(emailDispl != emailOriginal){
                    if(emailDispl.equals("")){
                        Toast.makeText(profileEditor.this, "Email can't be blank!", Toast.LENGTH_SHORT).show();
                    }else{
                        auth.getCurrentUser().updateEmail(emailDispl);
                    }
                }

                //ContactNo Section:
                if(contactNo != contactNoOriginal){
                    DatabaseReference contactNoRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(auth.getCurrentUser().getUid())
                            .child("Details")
                            .child("contactNo");
                    contactNoRef.setValue(contactNo);
                }

                //Street Section:
                if(street != streetOriginal){
                    DatabaseReference streetRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(auth.getCurrentUser().getUid())
                            .child("Details")
                            .child("street");
                    streetRef.setValue(street);
                }

                //Suburb Section:
                if(suburb != suburbOriginal){
                    DatabaseReference suburbRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(auth.getCurrentUser().getUid())
                            .child("Details")
                            .child("suburb");
                    suburbRef.setValue(suburb);
                }

                //City Section:
                if(city != cityOriginal){
                    DatabaseReference cityRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(auth.getCurrentUser().getUid())
                            .child("Details")
                            .child("city");
                    cityRef.setValue(city);
                }

                //ZIP Section:
                if(zip != zipOriginal){
                    DatabaseReference zipRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(auth.getCurrentUser().getUid())
                            .child("Details")
                            .child("ZIP");
                    zipRef.setValue(zip);
                }
                Toast.makeText(profileEditor.this, "Data updated", Toast.LENGTH_SHORT).show();
                //Start next activity
                startActivity(new Intent(profileEditor.this, PrivateProfileView.class));

            }
        });
    }

}


