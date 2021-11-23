
package com.example.recyclowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }

    }

    public void openBookPickup(View view) {
        Intent bookPickup = new Intent(this, BookPickup.class);

        startActivity(bookPickup);


    }
    public void openMyBookings(View view) {
        Intent myBookings = new Intent(this, MyBookings.class);

        startActivity(myBookings);

    }

    public void openUserProfile(View view) {
        Intent userProfile = new Intent(this, UserProfile.class);

        startActivity(userProfile);
    }

    public void logout(View view) {
        firebaseAuth.signOut();
        Intent logout = new Intent(this, Login.class);

        startActivity(logout);
    }

    public void openHomeMarketplace(View view) {
            Intent homeMarketplace = new Intent(this, HomeMarketplace.class);

        startActivity(homeMarketplace);
    }
}
