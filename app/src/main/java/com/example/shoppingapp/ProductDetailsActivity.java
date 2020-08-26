package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingapp.Model.Products;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity
{
    private Button addToCartBtn, addToWishBtn;
    private ImageView productImg;
    private ElegantNumberButton numberButton;
    private TextView PrName,PrDetails,PrPrice;
    private String productId="", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");

        addToCartBtn = (Button) findViewById(R.id.add_product_to_cart_btn);
        addToWishBtn = (Button) findViewById(R.id.add_product_to_wish_btn);
        productImg = (ImageView) findViewById(R.id.product_img_details);
        numberButton = (ElegantNumberButton) findViewById(R.id.quantity_btn);
        PrName = (TextView) findViewById(R.id.product_name_details);
        PrDetails = (TextView) findViewById(R.id.product_details1);
        PrPrice = (TextView) findViewById(R.id.product_price_details);

        getProductDetails(productId);

        addToWishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishList();
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Order Placed") || state.equals("Order Sent"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "you can purchase more products, once your order is confirmed.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addToCartList();
                }
            }
        });
    }

    private void addToWishList()
    {
        String saveDate;
        Calendar calForDate =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveDate = currentDate.format(calForDate.getTime());

        final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

        final HashMap<String, Object> wishMap =new HashMap<>();
        wishMap.put("pid",productId);
        wishMap.put("pname",PrName.getText().toString());
        wishMap.put("date",saveDate);
        wishMap.put("price",PrPrice.getText().toString());

        wishListRef.child("User View").child(Prevalent.currentUser.getPhone())
                .child("Products").child(productId).updateChildren(wishMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProductDetailsActivity.this, "Added to Wish List.", Toast.LENGTH_SHORT).show();

                            Intent intent =new Intent(ProductDetailsActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }

    private void addToCartList()
    {
        String saveTime, saveDate;
        Calendar calForDate =  Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap =new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("pname",PrName.getText().toString());
        cartMap.put("date",saveDate);
        cartMap.put("time",saveTime);
        cartMap.put("price",PrPrice.getText().toString());
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentUser.getPhone())
                .child("Products").child(productId).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentUser.getPhone())
                                    .child("Products").child(productId).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                                Intent intent =new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(final String productId)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);

                    PrName.setText(products.getPname());
                    PrDetails.setText(products.getDetails());
                    PrPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("sent"))
                    {
                        state = "Order Sent";
                    }
                    else if(shippingState.equals("not send yet"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
