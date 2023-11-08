package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;

    FirebaseDatabase database;

    ArrayList<User> userArrayList;

    ImageView imglogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("user");
        //To Store unique ID in RealtimeDatabase

        //Now we have to allocate memory for ArrayList

        userArrayList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //To set the data
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //Jo bhi data aayega woh iske andar aayega.
                    User user = dataSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imglogout = findViewById(R.id.logoutimg);
        //Logout button
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this,R.style.dialogue);
                dialog.setContentView(R.layout.dialog_layout);
                Button no,yes;
                yes = dialog.findViewById(R.id.yesbnt);
                no = dialog.findViewById(R.id.nobnt);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, login.class);
                        startActivity(intent);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(MainActivity.this,userArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        //If Not Already logged in then the below code will redirect to login page.
        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }
    }
}