package com.example.shoppingapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interface.ItemClickListner;
import com.example.shoppingapp.R;

public class WishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView wish_pr_name, wish_pr_price;
    private ItemClickListner itemClickListner;

    public WishViewHolder(@NonNull View itemView) {
        super(itemView);

        wish_pr_name = itemView.findViewById(R.id.wish_pr_name);
        wish_pr_price = itemView.findViewById(R.id.wish_pr_price);
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
