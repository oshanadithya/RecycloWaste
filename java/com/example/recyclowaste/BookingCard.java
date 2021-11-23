package com.example.recyclowaste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BookingCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_card);
    }

    public void openBookPickup(View view) {
        Intent bookPickup = new Intent(this, BookPickup.class);

        startActivity(bookPickup);

    }
    public void openMyBookings(View view) {
        Intent myBookings = new Intent(this, MyBookings.class);

        startActivity(myBookings);

    }
}
