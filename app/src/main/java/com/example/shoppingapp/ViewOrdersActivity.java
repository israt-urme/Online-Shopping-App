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
import android.widget.Toast;

import com.example.shoppingapp.Admin.AdminMaintainProductsActivity;
import com.example.shoppingapp.Model.Favorites;
import com.example.shoppingapp.Model.Orders;
import com.example.shoppingapp.Model.Products;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.example.shoppingapp.ViewHolder.OrderViewHolder;
import com.example.shoppingapp.ViewHolder.ProductViewHolder;
import com.example.shoppingapp.ViewHolder.WishViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewOrdersActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        recyclerView = findViewById((R.id.order_list));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference orderListRef = FirebaseDatabase.getInstance().getReference().child("ViewOrders");

        FirebaseRecyclerOptions<Orders> options= new FirebaseRecyclerOptions.Builder<Orders>().setQuery(orderListRef, Orders.class).build();

        FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Orders model) {
                        holder.order_pr_date.setText(model.getDate());
                        holder.order_pr_number.setText(model.getNumber());
                        holder.order_pr_price.setText(model.getTotalAmount()+"tk");
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_layout, parent, false);
                        OrderViewHolder holder= new OrderViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}
