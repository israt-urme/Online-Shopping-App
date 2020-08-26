package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Admin.AdminCategoryActivity;
import com.example.shoppingapp.Model.Users;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText number,password;
    private Button loginB;
    private CheckBox rememberMe;
    private TextView adminLink, forgetPass;

    private ProgressDialog loading;

    private String parentDbname= "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginB= (Button) findViewById(R.id.login_button1);
        number= (EditText) findViewById(R.id.input_number);
        password= (EditText) findViewById(R.id.input_password);
        adminLink=(TextView) findViewById(R.id.admin_panel);
        loading= new ProgressDialog(this);
        rememberMe=(CheckBox) findViewById(R.id.remember_me);
        forgetPass=(TextView) findViewById(R.id.forget_pass);
        Paper.init(this);


        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginB.setText("ADMIN LOGIN");
                adminLink.setVisibility(View.INVISIBLE);
                parentDbname="Admin";
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

    }

    private void LoginUser()
    {
        String i_number = number.getText().toString();
        String i_password = password.getText().toString();

        if (TextUtils.isEmpty(i_number)) {
            Toast.makeText(this, "Enter your number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_password)) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("Login Account");
            loading.setMessage("Please wait...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            AllowAccess(i_number, i_password);
        }
    }

    private void AllowAccess(final String i_number, final String i_password)
    {
        if(rememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, i_number);
            Paper.book().write(Prevalent.UserPasswordKey, i_password);
        }

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbname).child(i_number).exists())
                {
                    Users usersData= snapshot.child(parentDbname).child(i_number).getValue(Users.class);

                    if(usersData.getPhone().equals(i_number))
                    {
                        if(usersData.getPassword().equals(i_password))
                        {
                            if(parentDbname.equals("Admin"))
                            {
                                Toast.makeText(LoginActivity.this, "Admin successfully logged in.", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentUser= usersData;
                                startActivity(intent);

                            }

                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "This number "+ i_number +" doesn't exist.", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }
}
