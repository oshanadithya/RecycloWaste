package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recyclowaste.model.Review;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import java.util.Map;
import java.util.regex.Pattern;
import 	java.util.regex.Matcher;

public class Reviews extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button addTip;
    private int cID = 0;
    private int courseID = 0;

    Review review;
    TextView tipView;
    EditText customamount;
    Button hundred, fifty, fivehundred, add, pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        tipView = (TextView) findViewById(R.id.tv_tipView);
        customamount = (EditText) findViewById(R.id.nd_customamount);
        hundred = (Button) findViewById(R.id.bt_hundred);
        fifty = (Button) findViewById(R.id.bt_fifty);
        fivehundred = (Button) findViewById(R.id.bt_fhundred);
        add = (Button) findViewById(R.id.bt_addpay);
        pay = (Button) findViewById(R.id.bt_done);

        Double inputTip;

        Button submitButtonBooking = (Button) findViewById(R.id.bt_forbooking);
        Button submitButtonDriver = (Button) findViewById(R.id.bt_fordriver);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });



        hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tip = customamount.getText().toString();
                String currenttip = 100+tip;
                String pattern = "([0-9]{4})(.)([0-2]{2})";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(currenttip);
                if (m.matches()){System.out.println("Valid price");} else {System.out.println("Invalid price");}
                tipView.setText("Rs "+currenttip);
            }
        });

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tip = customamount.getText().toString();
                String currenttip = 50+tip;
                String pattern = "([0-9]{4})(.)([0-2]{2})";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(currenttip);
                if (m.matches()){System.out.println("Valid price");} else {System.out.println("Invalid price");}
                tipView.setText("Rs "+currenttip);
            }
        });

        fivehundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tip = customamount.getText().toString();
                String currenttip = 500+tip;
                String pattern = "([0-9]{4})(.)([0-2]{2})";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(currenttip);
                if (m.matches()){System.out.println("Valid price");} else {System.out.println("Invalid price");}
                tipView.setText("Rs "+currenttip);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get values from texteditor to textviewer
                String tip = customamount.getText().toString();
                String currenttip = tip;
                String pattern = "([0-9]{4})(.)([0-2]{2})";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(currenttip);
                if (m.matches()){System.out.println("Valid price");} else {System.out.println("Invalid price");}
                tipView.setText("Rs "+currenttip);
            }
        });


        // perform click event on button booking
        submitButtonBooking.setOnClickListener(new View.OnClickListener() {
            /** Called booking page when the user touches the button */
            @Override
            public void onClick(View view) {
                openBookUser();
            }
        });

        // perform click event on button driver
        submitButtonDriver.setOnClickListener(new View.OnClickListener() {
            /** Called driver page when the user touches the button */
            @Override
            public void onClick(View view) {
                openDriver();
            }
        });

    }

    private void processinsert(){
        Map<String, Object> map=new HashMap<>();
        map.put("Tip",tipView.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("tip").push()
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                tipView.setText("");
                Toast.makeText(getApplicationContext(), "Paid Successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Payment Unsuccessful", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openBookUser() {
        Intent intentb = new Intent(this, reviews_bookuser.class);
        startActivity(intentb);
    }

    private void openDriver() {
        Intent intentd = new Intent(this, Reviews_Driver.class);
        startActivity(intentd);
    }


}
