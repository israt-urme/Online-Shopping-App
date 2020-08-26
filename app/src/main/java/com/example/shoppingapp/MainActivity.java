package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shoppingapp.Model.Users;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button join, login;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        join = (Button) findViewById(R.id.join_button);
        login= (Button) findViewById(R.id.login_button);
        loading= new ProgressDialog(this);

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey!="" && UserPasswordKey!="")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccessId(UserPhoneKey,UserPasswordKey);

                loading.setTitle("Already Logged in");
                loading.setMessage("Please wait...");
                loading.setCanceledOnTouchOutside(false);
                loading.show();

            }
        }
    }

    private void AllowAccessId(final String userPhoneKey,final String userPasswordKey)
    {

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(userPhoneKey).exists())
                {
                    Users usersData= snapshot.child("Users").child(userPhoneKey).getValue(Users.class);

                    if(usersData.getPhone().equals(userPhoneKey))
                    {
                        if(usersData.getPassword().equals(userPasswordKey))
                        {
                            Toast.makeText(MainActivity.this, "Welcome to the Shopping App", Toast.LENGTH_SHORT).show();
                            loading.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentUser=usersData;
                            startActivity(intent);


                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(MainActivity.this, "incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "This number "+ userPhoneKey +" doesn't exist.", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }
}
