package com.example.recyclowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class adPosted extends AppCompatActivity {

    ImageView imgTick;
    Button viewAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_posted);

        imgTick = findViewById(R.id.img_tick);
        Glide.with(this).load(R.drawable.tick).into(imgTick);


        viewAds = findViewById(R.id.btn_ViewAds);
        viewAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , MyAds.class);
                startActivity(intent);
                finish();
            }
        });
    }
}