package com.example.recyclowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Successful extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);
    }

    public void seeBookings(View view) {
        Intent myBookings = new Intent(this, MyBookings.class);
        startActivity(myBookings);
    }
}