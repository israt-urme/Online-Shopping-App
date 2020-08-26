package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Model.Cart;
import com.example.shoppingapp.Model.Favorites;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.example.shoppingapp.ViewHolder.CartViewHolder;
import com.example.shoppingapp.ViewHolder.WishViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class WishListActivity extends AppCompatActivity {
    private String wish_name, wish_price;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //edited recent
    private String productId="";
    //end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        recyclerView = findViewById((R.id.wish_list));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

        FirebaseRecyclerOptions<Favorites> options =
                new FirebaseRecyclerOptions.Builder<Favorites>()
                        .setQuery(wishListRef.child("User View")
                                .child(Prevalent.currentUser.getPhone()).child("Products"), Favorites.class).build();

        FirebaseRecyclerAdapter<Favorites, WishViewHolder> adapter
                = new FirebaseRecyclerAdapter<Favorites, WishViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishViewHolder holder, int position, @NonNull final Favorites model)
            {
                holder.wish_pr_price.setText("Price = " + model.getPrice() + "tk");
                holder.wish_pr_name.setText(model.getPname());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(WishListActivity.this);
                        builder.setTitle("Want to Remove the Product from Wish List? ");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0)
                                {
                                    wishListRef.child("User View")
                                            .child(Prevalent.currentUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(WishListActivity.this, "Item removed from wish list.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(WishListActivity.this, WishListActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_items_layout, parent, false);
                WishViewHolder holder = new WishViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
