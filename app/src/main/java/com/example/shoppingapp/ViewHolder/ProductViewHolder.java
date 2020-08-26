package com.example.shoppingapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interface.ItemClickListner;
import com.example.shoppingapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDetails,txtProductPrice;
    public ImageView imgView;
    public ItemClickListner listner;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imgView=(ImageView) itemView.findViewById(R.id.product_image);
        txtProductName=(TextView) itemView.findViewById(R.id.product_name1);
        txtProductDetails=(TextView) itemView.findViewById(R.id.product_details1);
        txtProductPrice=(TextView) itemView.findViewById(R.id.product_price1);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner=listner;

    }

    @Override
    public void onClick(View v)
    {
        listner.onClick(v, getAdapterPosition(),false);
    }

}
