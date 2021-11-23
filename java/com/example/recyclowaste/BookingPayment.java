package com.example.recyclowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclowaste.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class BookingPayment extends AppCompatActivity {
    DatabaseReference dbref;
    Booking booking;
    TextView payment;
    String username;
    RadioButton radiocard;
    RadioButton radiopickup;
    DecimalFormat df;
    Loader loader;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_payment);
        radiocard= findViewById(R.id.radioCard);
        radiopickup = findViewById(R.id.radioPickup);
        df = new DecimalFormat("####0.00");
        loader = new Loader(this);
        firebaseAuth = FirebaseAuth.getInstance();

        payment = findViewById(R.id.payment);
        Intent i = getIntent();
        username = firebaseAuth.getCurrentUser().getDisplayName();

        booking = (Booking)i.getSerializableExtra("booking");
        payment.setText(String.valueOf("LKR " + df.format(booking.getPayment())));

    }

    public void clickConfirm(View view) {
        if(radiocard.isChecked()) {
            showDialog();
        }
        else {
            insertBooking();
        }

    }

    public void showDialog() {
        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.payment_dialog_layout);

        TextView amount = dialog.findViewById(R.id.tv_amount);
        Button btnPay = dialog.findViewById(R.id.btn_pay);
        EditText cardNo = dialog.findViewById(R.id.et_card_number);
        EditText expiry = dialog.findViewById(R.id.et_expiry);
        EditText code = dialog.findViewById(R.id.et_code);
        CheckBox terms = dialog.findViewById(R.id.cb_terms);

        amount.setText("Amount (LKR) " + String.valueOf(df.format(booking.getPayment())));

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(cardNo.getText())  && !TextUtils.isEmpty(expiry.getText())
                        && !TextUtils.isEmpty(code.getText())) {
                    if(terms.isChecked()) {
                        loader.showLoadingDialog();
                        insertBooking();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "You have to agree to terms and conditions", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Please provide all the details!", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();
    }

    public void insertBooking() {
        dbref = FirebaseDatabase.getInstance().getReference().child("Booking").child(username);
        try{
            dbref.push().setValue(booking);
            Intent details = new Intent(BookingPayment.this, BookingDetails.class);
            startActivity(details);
            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
            Intent success = new Intent(BookingPayment.this, Successful.class);
            startActivity(success);
        }catch (Exception e) {
            e.printStackTrace();
        }
        loader.dismissLoadingDialog();
    }
}