package com.example.recyclowaste;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Button;
import android.widget.Toast;

public class reviews_bookuser extends Reviews {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_bookuser);

        Button submitButtonAdd = (Button) findViewById(R.id.bt_addreviewbook);
        Button submitButtonEdit = (Button) findViewById(R.id.bt_editreviewbook);

        // initiate rating bar and a button
        RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.rb_reviewbook);
        Button submitButton = (Button) findViewById(R.id.bt_ratingbook);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRatingBarCh = rootRef.child("ratings");

        /*findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = simpleRatingBar.getRating();
                mRatingBarCh.child("rating").setValue(String.valueOf(rating));
            }
        });*/

        // perform click event on button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get values and then displayed in a toast
                String totalStars = "Total Stars:: " + simpleRatingBar.getNumStars();
                String rating = "Rating :: " + simpleRatingBar.getRating();
                Toast.makeText(getApplicationContext(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();
            }
        });
        submitButtonAdd.setOnClickListener(new View.OnClickListener() {
            /** Called add review page when the user touches the button */
            @Override
            public void onClick(View view) {
                Intent intentadd = new Intent(getApplicationContext(),reviewAdd.class);
                startActivity(intentadd);
            }
        });

        submitButtonEdit.setOnClickListener(new View.OnClickListener() {
            /** Called edit review page when the user touches the button */
            @Override
            public void onClick(View view) {
                Intent intentedit = new Intent(view.getContext(), review_history.class);
                startActivity(intentedit);
            }
        });

    }



}