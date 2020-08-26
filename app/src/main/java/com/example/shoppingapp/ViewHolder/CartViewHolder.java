package com.example.shoppingapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interface.ItemClickListner;
import com.example.shoppingapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtPrName,txtPrPrice,txtPrQuantity;

    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtPrName = itemView.findViewById(R.id.cart_pr_name);
        txtPrPrice = itemView.findViewById(R.id.cart_pr_price);
        txtPrQuantity = itemView.findViewById(R.id.cart_pr_quantity);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v, getAdapterPosition(), false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
