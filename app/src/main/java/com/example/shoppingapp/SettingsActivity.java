package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity
{
    private CircleImageView profileImgView;
    private EditText nameTxt,numberTxt,addressTxt;
    private TextView imgChangeBtn,closeBtn,updateBtn;
    private Button securityQuestionBtn;

    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageProfileImgReference;
    private String checker="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfileImgReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profileImgView = (CircleImageView) findViewById(R.id.setting_profile_img);
        nameTxt = (EditText) findViewById(R.id.name_setting);
        numberTxt = (EditText) findViewById(R.id.number_setting);
        addressTxt = (EditText) findViewById(R.id.address_setting);
        imgChangeBtn = (TextView) findViewById(R.id.change_profile_img);
        closeBtn = (TextView) findViewById(R.id.close_setting_btn);
        updateBtn = (TextView) findViewById(R.id.update_setting_btn);
        securityQuestionBtn = findViewById(R.id.security_questions_btn);

        userInfoDisplay(profileImgView,nameTxt,numberTxt,addressTxt);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SettingsActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked"))
                {
                    UserInfoSaved();
                }
                else
                {
                    UpdateUserInfo();
                }
            }
        });

        imgChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });
    }

    private void UpdateUserInfo()
    {
        /*no image will be updated*/
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", nameTxt.getText().toString());
        userMap.put("phoneToOrder", numberTxt.getText().toString());
        userMap.put("address", addressTxt.getText().toString());
        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImgView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "ERROR! Try Again.",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void UserInfoSaved()
    {
        if(TextUtils.isEmpty(nameTxt.getText().toString()))
        {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(numberTxt.getText().toString()))
        {
            Toast.makeText(this, "Enter Phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressTxt.getText().toString()))
        {
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImg();
        }
    }

    private void uploadImg()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, Account is updating!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef = storageProfileImgReference.child(Prevalent.currentUser.getPhone()+".jpg");
            uploadTask=fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", nameTxt.getText().toString());
                        userMap.put("numberToOrder", numberTxt.getText().toString());
                        userMap.put("address", addressTxt.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Select an Image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImgView, final EditText nameTxt, final EditText numberTxt, final EditText addressTxt)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("image").exists())
                    {
                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImgView);
                        nameTxt.setText(name);
                        numberTxt.setText(phone);
                        addressTxt.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
