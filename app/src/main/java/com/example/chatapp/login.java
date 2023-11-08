package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button button;
    EditText email,password;
    FirebaseAuth auth;

    TextView logsignup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.loginbutton);
        email = findViewById(R.id.editLogEmailAddress);
        password = findViewById(R.id.editLogTextPassword);
        logsignup = findViewById(R.id.logsignup);

        logsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,registration.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if((TextUtils.isEmpty(Email))){
                    Toast.makeText(login.this,"Please Enter the Email",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Password)){
                    Toast.makeText(login.this,"Please Enter the Password",Toast.LENGTH_SHORT).show();
                }
                else if(!Email.matches(emailPattern)){
                    email.setError("Give Proper Email Address In Proper Format");
                }


                else{
                    //Now to check if task(if email and pass match) is done or not
                    auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                try{
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();//To close app if pressed the back button
                                } catch (Exception e){
                                    Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}