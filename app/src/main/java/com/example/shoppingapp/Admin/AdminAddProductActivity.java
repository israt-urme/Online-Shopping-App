package com.example.shoppingapp.Admin;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProductActivity extends AppCompatActivity
{
    private String CategoryName, Pname, Details, Price, saveDate, saveTime, ProductRandomKey, downloadImgUrl;
    private Button AddProductBtn;
    private ImageView ProductImg;
    private EditText ProductName,ProductDetails,ProductPrice;
    private Uri ImageUri;
    private StorageReference ProductImgRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loading;

    private static final int GalleryPick=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        CategoryName=getIntent().getExtras().get("Category").toString();
        ProductImgRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");
        loading= new ProgressDialog(this);

        AddProductBtn=(Button) findViewById(R.id.add_new_product);
        ProductImg=(ImageView) findViewById(R.id.select_product_image);
        ProductName=(EditText) findViewById(R.id.product_name);
        ProductDetails=(EditText) findViewById(R.id.product_details);
        ProductPrice=(EditText) findViewById(R.id.product_price);

        ProductImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    private void ValidateProductData()
    {
        Details=ProductDetails.getText().toString();
        Pname=ProductName.getText().toString();
        Price=ProductPrice.getText().toString();

        if(ImageUri==null)
        {
            Toast.makeText(this,"Product Image is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Details))
        {
            Toast.makeText(this,"Write about product details",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this,"Write about product price",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this,"Write about product name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        loading.setTitle("Add New Product");
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd,yyyy");
        saveDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveTime=currentTime.format(calendar.getTime());

        ProductRandomKey=saveDate+" "+saveTime;

        final StorageReference filePath= ProductImgRef.child(ImageUri.getLastPathSegment()+ProductRandomKey+".jpg");
        final UploadTask Utask=filePath.putFile(ImageUri);

        Utask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.toString();
                Toast.makeText(AdminAddProductActivity.this, "ERROR: "+msg,Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "Product Image Uploaded.",Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask=Utask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImgUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            downloadImgUrl=task.getResult().toString();
                            Toast.makeText(AdminAddProductActivity.this, "Got Product Image Url.",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> ProductMap =new HashMap<>();
        ProductMap.put("pid",ProductRandomKey);
        ProductMap.put("date",saveDate);
        ProductMap.put("time",saveTime);
        ProductMap.put("details",Details);
        ProductMap.put("image",downloadImgUrl);
        ProductMap.put("category",CategoryName);
        ProductMap.put("price",Price);
        ProductMap.put("pname",Pname);

        ProductRef.child(ProductRandomKey).updateChildren(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(AdminAddProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);

                    loading.dismiss();
                    Toast.makeText(AdminAddProductActivity.this, "Product is added.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loading.dismiss();
                    String msg=task.getException().toString();
                    Toast.makeText(AdminAddProductActivity.this, "ERROR: "+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void OpenGallery()
    {
        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==GalleryPick && data!=null)
        {
           ImageUri=data.getData();
           ProductImg.setImageURI(ImageUri);
        }
    }
}
