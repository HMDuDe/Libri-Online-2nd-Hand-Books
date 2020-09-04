package com.koddev.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koddev.chatapp.Model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class DataEntry extends AppCompatActivity implements Serializable {
    private Intent intent;
    private EditText fullNameTxt, streetTxt, suburbTxt, cityTxt, zipTxt, contactNoTxt;
    private String fullName, street, suburb, city, zip, contactNo;
    private Uri filePath;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private final int pickImageRequest = 1;
    private ImageView img;
    private StorageReference storeRef;
    private UserData appendNewData;
    private FirebaseUser currentUser;
    StorageReference fileReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        appendNewData = new UserData();

        //Database initialization
        auth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).child("Details");
        storeRef = FirebaseStorage.getInstance().getReference("UserProfileImgs");

        fullNameTxt = findViewById(R.id.addNameSurnameTxt);
        contactNoTxt = findViewById(R.id.contactNoTxt);
        streetTxt = findViewById(R.id.addStreetTxt);
        suburbTxt = findViewById(R.id.suburbTxt);
        cityTxt = findViewById(R.id.addCityTxt);
        zipTxt = findViewById(R.id.addZIPTxt);
        img = findViewById(R.id.profileImage);

        configConfirmBtn();
        configChooseBtn();
    }

    public void configConfirmBtn(){
        Button confirmBtn = findViewById(R.id.enterDataBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = String.valueOf(fullNameTxt.getText());
                contactNo = String.valueOf(contactNoTxt.getText());
                street = String.valueOf(streetTxt.getText());
                suburb = String.valueOf(suburbTxt.getText());
                city = String.valueOf(cityTxt.getText());
                zip = String.valueOf(zipTxt.getText());

                if(fullName != "" || contactNo != "" || street != "" || suburb != "" || city != "" || zip != ""){

                    appendNewData.setNameSurname(fullName);
                    appendNewData.setContactNo(contactNo);
                    appendNewData.setStreet(street);
                    appendNewData.setSuburb(suburb);
                    appendNewData.setCity(city);
                    appendNewData.setZIP(zip);

                    dbRef.setValue(appendNewData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(DataEntry.this, "Data uploaded", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(DataEntry.this, Home.class));
                                }
                            });
                }
            }
        });
    }

    //Image upload code
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void configChooseBtn(){
        Button chooseImgBtn = findViewById(R.id.chooseProfileImageBtn);
        chooseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, pickImageRequest);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pickImageRequest && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            Picasso.with(this).load(filePath).into(img);

        }else{
            Toast.makeText(this, "ERROR adding IMAGE", Toast.LENGTH_LONG).show();
        }

        Log.d("METHOD CALLING: ", "CALLING METHOD UPLOADIMAGE");
        uploadImage();
    }

    public void uploadImage(){
        if(filePath != null){
            fileReference = storeRef.child("images/" + System.currentTimeMillis() +
                    "." + getFileExtension(filePath));

            fileReference.putFile(filePath);

            fileReference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("URI VAL: ", "" + uri);
                            appendNewData.setImgPath(String.valueOf(uri));
                            Log.d("IMAGE URI: ", "" + appendNewData.getImgPath());
                        }
                    });

            fileReference.putFile(filePath)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(DataEntry.this, "Image uploaded to database", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}