package com.example.shoppingapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interface.ItemClickListner;
import com.example.shoppingapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView order_pr_date,order_pr_number, order_pr_price;
    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        order_pr_date =itemView.findViewById(R.id.order_pr_date);
        order_pr_number = itemView.findViewById(R.id.order_pr_number);
        order_pr_price = itemView.findViewById(R.id.order_pr_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }

}
