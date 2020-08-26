package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton;
    private EditText name,number,password;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton= (Button) findViewById(R.id.reg_button);
        name= (EditText) findViewById(R.id.reg_input_name);
        number= (EditText) findViewById(R.id.reg_input_number);
        password= (EditText) findViewById(R.id.reg_input_password);
        loading= new ProgressDialog(this);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }

    private void SignIn()
    {
        String i_name= name.getText().toString();
        String i_number= number.getText().toString();
        String i_password= password.getText().toString();

        if (TextUtils.isEmpty(i_name))
        {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_number))
        {
            Toast.makeText(this, "Enter your number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(i_password))
        {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("Sign In");
            loading.setMessage("Please wait...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            ExistingNumber(i_name, i_number, i_password);
        }

    }

    private void ExistingNumber(final String i_name, final String i_number, final String i_password)
    {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if(!(snapshot.child("Users").child(i_number).exists()))
                {
                    HashMap<String,Object>userdataMap =new HashMap<>();
                    userdataMap.put("name",i_name);
                    userdataMap.put("phone",i_number);
                    userdataMap.put("password",i_password);

                    Rootref.child("Users").child(i_number).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Congratulation, you have successfully sign in",Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loading.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error!try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "this" + i_number + "already exists", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please use another number.",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
