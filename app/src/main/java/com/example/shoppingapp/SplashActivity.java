package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity
{
    //Animation topA,bottomA;
    ImageView img;
    TextView s_title,title_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        img = (ImageView) findViewById(R.id.splash_img);

//        topA = AnimationUtils.loadAnimation(this,R.anim.top_animation);
//        bottomA = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
//
//        s_title = (TextView) findViewById(R.id.title);
//        title_txt = (TextView) findViewById(R.id.title_text);
//
//        img.setAnimation(topA);
//        s_title.setAnimation(bottomA);
//        title_txt.setAnimation(bottomA);
//        SystemClock.sleep(4000);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainIntent);
                Toast.makeText(SplashActivity.this, "Welcome to Compras BD", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
