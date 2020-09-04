package com.koddev.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.koddev.chatapp.Model.Upload;
import com.koddev.chatapp.Model.User;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Create_Ad extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;

    private long advertId = 0;
    private Upload upload;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference tDatabaseRef;
    private StorageTask mUploadTask;

    private EditText ISBN, Title, Author, Faculty, Year, Price, Publisher;
    private Spinner Condition;
    private Button add_Image, post;
    private ImageView home, image;
    private ProgressBar progressBar;
    private Uri imageUri;
    private Handler handler = new Handler();
    private Intent intent;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__ad);


        home = findViewById(R.id.libri_logo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        ImageButton cart = findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Create_Ad.this, Cart.class));
            }
        });


        ISBN = findViewById(R.id.ISBN);
        Title = findViewById(R.id.Title);
        Author = findViewById(R.id.Author);
        Faculty = findViewById(R.id.Faculty);
        Condition = findViewById(R.id.Condition);
        Year = findViewById(R.id.Year);
        Price = findViewById(R.id.Price);
        Publisher = findViewById(R.id.Publisher);
        image = findViewById(R.id.image);


        add_Image = findViewById(R.id.Add_image_button);
        add_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        post = findViewById(R.id.post_button);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {

                } else if (ISBN != null && Title != null && Author != null && Faculty != null && Condition != null && Year != null && Price != null && imageUri != null && Publisher != null) {//checks that all fields have values

                    try {
                        int year = Integer.parseInt(Year.getText().toString().trim()); // checks that year is an integer

                        try {
                            double price = Double.parseDouble(Price.getText().toString().trim()); // checks that price is a double

                            int current_year = Calendar.getInstance().get(Calendar.YEAR);
                            String t = String.valueOf(ISBN.getText());
                            if (t.length() <10) { // checks ISBN length is correct
                                Toast.makeText(Create_Ad.this, "Please make sure you enter the correct ISBN number", Toast.LENGTH_LONG).show();
                                ISBN.setText(null);

                            } else if (year > current_year) {//checks for year being less than current year
                                Toast.makeText(Create_Ad.this, "The year you have entered is invalid, please try again", Toast.LENGTH_LONG).show();
                                Year.setText(null);

                            } else
                                uploadAd();

                        } catch (NumberFormatException e) {
                            Toast.makeText(Create_Ad.this, "Please enter the price as a number eg.9.99", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(Create_Ad.this, "Please enter the year as a number eg.2019", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(Create_Ad.this, "Please fill in all of the fields and add a picture", Toast.LENGTH_SHORT).show();
            }
        });

        progressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("adverts");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    advertId = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tDatabaseRef = FirebaseDatabase.getInstance().getReference("textbooks");

        Spinner con = findViewById(R.id.Condition);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.conditions));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        con.setAdapter(adapter);
    }

    public void openHome() {
        intent = new Intent(this, Home.class);
        startActivity(intent);
    }


    public void openFileChooser() {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(image);
            //mImageView.setImageURI(mImageUri); native

        } else Toast.makeText(this, " Retrieval Failed", Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    boolean uploaded = false;

    public void uploadAd() {
        if (imageUri != null) {

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
                    "." + getFileExtension(imageUri));
            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 2000);


                            final String isbn = ISBN.getText().toString().trim();
                            final String title = Title.getText().toString().trim();
                            final String author = Author.getText().toString().trim();
                            final String faculty = Faculty.getText().toString().trim();
                            final String condition = Condition.getSelectedItem().toString();
                            final String price = Price.getText().toString().trim();
                            final String year = Year.getText().toString().trim();


                            final String publisher = Publisher.getText().toString().trim();
                            final String date = convertDate();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imageurl = uri.toString();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            String sellerID = user.getId();
                                            String seller = user.getUsername();
                                            upload = new Upload(advertId, isbn, author, title, sellerID, seller, faculty, imageurl, price, year, condition, publisher, date);
                                            mDatabaseRef.child(String.valueOf(advertId)).setValue(upload);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });

                            try {

                                upload = new Upload(isbn, author, title, faculty, year, publisher);
                                tDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.hasChild("isbn"))
                                            tDatabaseRef.child(isbn).setValue(upload);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                advertId++;
                                uploaded = true;
                                Toast.makeText(Create_Ad.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            if (uploaded == true) {
                                openHome();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Create_Ad.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Create_Ad.this, "Upload in progress", Toast.LENGTH_LONG).show();
                            post.setClickable(false);
                            post.setBackgroundColor(getResources().getColor(R.color.red));
                            double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    public String convertDate() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date curDate = new Date();
            return dateFormat.format(curDate);
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
        return "nd";
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
                intent = new Intent(this, PrivateProfileView.class);
                startActivity(intent);
                return true;
            case R.id.menu_help:
                //Toast.makeText(this, "Help selected", Toast.LENGTH_SHORT).show();
                intent = new Intent(Create_Ad.this, ReportIncident.class);
                startActivity(intent);
                return true;

            case R.id.menu_messages:

                intent = new Intent(Create_Ad.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Create_Ad.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return false;
        }
    }
}






