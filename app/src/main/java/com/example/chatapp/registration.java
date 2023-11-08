package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {
    TextView loginbut;
    EditText rg_Username,rg_Email,rg_pass,rg_repass;
    Button rg_signup;
    CircleImageView rg_profile;
    FirebaseAuth auth;

    Uri imageURI;

    String imageuri;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseDatabase database;

    FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        loginbut = findViewById(R.id.loginbut);
        rg_Username = findViewById(R.id.rgName);
        rg_Email = findViewById(R.id.rgEmail);
        rg_pass = findViewById(R.id.rgPassword1);
        rg_repass = findViewById(R.id.rgPassword2);
        rg_profile = findViewById(R.id.profilerg);
        rg_signup = findViewById(R.id.SignUp);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration.this,login.class);
                startActivity(intent);
                finish();
            }
        });

        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To get the email,password etc. to be stored.
                String namee = rg_Username.getText().toString();//To save the username in string.
                String emaill = rg_Email.getText().toString();
                String password = rg_pass.getText().toString();
                String repassword = rg_repass.getText().toString();
                String status = "Hey I am Using CosmicLink";

                //To check if all the fields entered is valid or not(Eg. is password is atleast 6 characters or not etc.)
                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)){
                    Toast.makeText(registration.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                }
                else if(!emaill.matches(emailPattern)){
                    rg_Email.setError("Give Proper Email Address In Proper Format");
                }
                else if(password.length()<6){
                    rg_pass.setError("Password must be 6 characters or more");
                }
                else if(!password.equals(repassword)){
                    rg_pass.setError("The Password Doesn't Match");
                }
                else{
                    auth.createUserWithEmailAndPassword(emaill,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //If task is successful
                            if(task.isSuccessful()){
                                //To create new Unique ID
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (imageURI != null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        User user = new User(id,namee,emaill,password,imageuri,status);
                                                        //To set the values
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Intent intent = new Intent(registration.this,MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else{
                                                                    Toast.makeText(registration.this, "Error in Creating The User", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else{
                                    String status = "Hey I am Using CosmicLink";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/chatapp-1384e.appspot.com/o/man.png?alt=media&token=399ffd92-922c-4212-a56c-46106f2dc1a8";
                                    User user = new User(id,namee,emaill,password,imageuri,status);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent intent = new Intent(registration.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(registration.this, "Error in Creating The User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        rg_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data != null){
                imageURI = data.getData();//Data would get stored in image URI.
                //To set picture in the display frame
                rg_profile.setImageURI(imageURI);
            }
        }
    }
}