package com.example.shoppingapp.Category;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shoppingapp.R;

public class CategoryListActivity extends AppCompatActivity
{
    private ImageView backBtn;
    private Button cMenBtn, cWomenBtn, cKidsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        backBtn = (ImageView) findViewById(R.id.arrow);
        cMenBtn = (Button) findViewById(R.id.men_btn);
        cWomenBtn = (Button) findViewById(R.id.women_btn);
        cKidsBtn = (Button) findViewById(R.id.kids_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryListActivity.super.onBackPressed();
            }
        });

        cMenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListActivity.this, MenActivity.class);
                startActivity(intent);
            }
        });

        cWomenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListActivity.this, WomenActivity.class);
                startActivity(intent);
            }
        });

        cKidsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListActivity.this, KidsActivity.class);
                startActivity(intent);
            }
        });

        //btn click korle home page er moto data ashbe ekhane,catagory wise
    }
}
