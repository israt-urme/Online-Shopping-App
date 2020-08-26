package com.example.shoppingapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapp.HomeActivity;
import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;

public class AdminCategoryActivity extends AppCompatActivity
{
    private ImageView men,women,kids,shoes;
    private ImageView bag,jewel,watch,glass;
    private ImageView sport,medicine, grocery,device;
    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Toast.makeText(this,"welcome to admin panel.",Toast.LENGTH_SHORT).show();

        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });

        men=(ImageView) findViewById(R.id.men);
        women=(ImageView) findViewById(R.id.women);
        kids=(ImageView) findViewById(R.id.kids);
        shoes=(ImageView) findViewById(R.id.shoe);

        bag=(ImageView) findViewById(R.id.bag);
        jewel=(ImageView) findViewById(R.id.jewel);
        watch=(ImageView) findViewById(R.id.watch);
        glass=(ImageView) findViewById(R.id.glass);

        sport=(ImageView) findViewById(R.id.sport);
        medicine=(ImageView) findViewById(R.id.medicine);
        grocery=(ImageView) findViewById(R.id.grocery);
        device=(ImageView) findViewById(R.id.device);

        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("Category","men");
                startActivity(intent);
            }
        });

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","women");
                startActivity(intent);
            }
        });

        kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","kids");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","shoes");
                startActivity(intent);
            }
        });

        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","bags");
                startActivity(intent);
            }
        });

        jewel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","jewellery");
                startActivity(intent);
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","watches");
                startActivity(intent);
            }
        });

        glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","glasses");
                startActivity(intent);
            }
        });

        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","devices");
                startActivity(intent);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","sports");
                startActivity(intent);
            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","medicines");
                startActivity(intent);
            }
        });

        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("Category","groceries");
                startActivity(intent);
            }
        });

    }
}
