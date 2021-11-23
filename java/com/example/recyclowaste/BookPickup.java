package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recyclowaste.model.Booking;
import com.example.recyclowaste.model.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class BookPickup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner dropdown;
    String domestic[] = new String[]{"Plastic", "Wood", "Paper", "Organic", "Glass", "Metal"};
    String medical[] = new String[]{"Syringes", "Gloves", "Needles", "Liquids", "Bandages", "Drugs"};
    ArrayList<String> includes;
    Chip waste1;
    Chip waste2;
    Chip waste3;
    Chip waste4;
    Chip waste5;
    Chip waste6;
    Button btnDate;
    Button btnTime;
    TextView inptlocation;
    String loc;
    Booking booking;
    UserLocation userlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseReference dbref;
    SimpleDateFormat dateFormat;
    SimpleDateFormat timeFormatDigital;
    Loader loader;
    String pickupDate;
    String pickupTime;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pickup);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormatDigital = new SimpleDateFormat("HH:mm");
        loader = new Loader(this);

        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        userlocation = new UserLocation(sharedPreferences.getString("locality", ""), Double.parseDouble(sharedPreferences.getString("latitude", "0")),
                Double.parseDouble(sharedPreferences.getString("longitude", "0")));

        waste1 = (Chip)findViewById(R.id.waste1);
        waste2 = (Chip)findViewById(R.id.waste2);
        waste3 = (Chip)findViewById(R.id.waste3);
        waste4 = (Chip)findViewById(R.id.waste4);
        waste5 = (Chip)findViewById(R.id.waste5);
        waste6 = (Chip)findViewById(R.id.waste6);
        inptlocation = findViewById(R.id.inputLocation);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);

        Calendar calendar =  Calendar.getInstance();

        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);

        btnDate.setText(dateFormat.format(calendar.getTime()));
        pickupDate = dateFormat.format(calendar.getTime());
        pickupTime = timeFormatDigital.format(calendar.getTime());
        btnTime.setText(TimeFormatter.ampmTime(calendar));
        if(userlocation.getLocality() != "") {
            inptlocation.setText(userlocation.getLocality());
        }


        includes = new ArrayList<String>();

        dropdown = findViewById(R.id.typeSpinner);

        String[] items = new String[]{"Domestic Waste", "Medical Waste"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }

    public void clickConfirm(View view) {
        try {
            if(userlocation.getLocality() == "") {
                Toast.makeText(getApplicationContext(), "Please Give Your Location", Toast.LENGTH_SHORT).show();
            }
            else {
                String drvr[] = new String[1];
                TreeMap<Double, String> distances = new TreeMap<>();

                loader.showLoadingDialog();
                dbref = FirebaseDatabase.getInstance().getReference().child("Drivers");
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()) {
                            float[] dist = new float[1];
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                if((Boolean)snap.child("available").getValue() == true) {
                                    Location.distanceBetween(Double.parseDouble(snap.child("location").child("latitude").getValue().toString()),
                                            Double.parseDouble(snap.child("location").child("longitude").getValue().toString()), userlocation.getLatitude(), userlocation.getLongitude(), dist);
                                    distances.put((double)dist[0], snap.child("fullName").getValue().toString());
                                }
                            }

                            if(!distances.isEmpty()) {
                                Map.Entry<Double, String> selected = distances.firstEntry();
                                drvr[0] = selected.getValue();
                                boolean containsIsEmpty = true;


                                String type = dropdown.getSelectedItem().toString();
                                String driver = drvr[0];
                                loc = inptlocation.getText().toString();

                                if (waste1.isChecked()) {
                                    includes.add(waste1.getText().toString());
                                    containsIsEmpty = false;
                                }
                                if (waste2.isChecked()) {
                                    includes.add(waste2.getText().toString());
                                    containsIsEmpty = false;
                                }
                                if (waste3.isChecked()) {
                                    includes.add(waste3.getText().toString());
                                    containsIsEmpty = false;
                                }
                                if (waste4.isChecked()) {
                                    includes.add(waste4.getText().toString());
                                    containsIsEmpty = false;
                                }
                                if (waste5.isChecked()) {
                                    includes.add(waste5.getText().toString());
                                    containsIsEmpty = false;
                                }
                                if (waste6.isChecked()) {
                                    includes.add(waste6.getText().toString());
                                    containsIsEmpty = false;
                                }

                                if(!containsIsEmpty) {
                                    String strIncludes = new String();

                                    for (String item : includes) {
                                        strIncludes = strIncludes + item + ",";
                                    }

                                    booking = new Booking(driver, type, userlocation, pickupDate, pickupTime, strIncludes, calculatePayment());

                                    Intent payment = new Intent(BookPickup.this, BookingPayment.class);
                                    payment.putExtra("booking", booking);
                                    startActivity(payment);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Please provide necessary details!", Toast.LENGTH_LONG).show();
                                }


                            }
                            else {
                                Toast.makeText(getApplicationContext(), "No drivers available! Please try again later!", Toast.LENGTH_LONG).show();
                            }

                        }
                        loader.dismissLoadingDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }


        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }

    }

    public void itemSelected(){
        if(dropdown.getSelectedItem() == "Domestic Waste"){
            waste1.setText(domestic[0]);
            waste2.setText(domestic[1]);
            waste3.setText(domestic[2]);
            waste4.setText(domestic[3]);
            waste5.setText(domestic[4]);
            waste6.setText(domestic[5]);
        }
        else if(dropdown.getSelectedItem() == "Medical Waste"){
            waste1.setText(medical[0]);
            waste2.setText(medical[1]);
            waste3.setText(medical[2]);
            waste4.setText(medical[3]);
            waste5.setText(medical[4]);
            waste6.setText(medical[5]);
        }

    }

    public void onClickLocation (View view) {
            checkLocationPermission();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(BookPickup.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

                            userlocation = new UserLocation(addresses.get(0).getLocality(), addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("locality", userlocation.getLocality());
                            editor.putString("latitude", String.valueOf(userlocation.getLatitude()));
                            editor.putString("longitude", String.valueOf(userlocation.getLongitude()));
                            editor.commit();

                            loc = addresses.get(0).getLocality();
                            inptlocation.setText(loc);

                        }catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    }

    public void checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(BookPickup.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(BookPickup.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
        else {
            ActivityCompat.requestPermissions(BookPickup.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            checkLocationPermission();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemSelected();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onClickDate (View view) {
        Calendar calendar =  Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DATE, date);
                pickupDate = dateFormat.format(cal.getTime());
                btnDate.setText(dateFormat.format(cal.getTime()));
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    public void onClickTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String am_pm;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, min);
                pickupTime = timeFormatDigital.format(cal.getTime());
                btnTime.setText(TimeFormatter.ampmTime(cal));

            }
        }, HOUR, MINUTE, false);

        timePickerDialog.show();
    }

    public double calculatePayment() {
        double payment = 0;

        if(dropdown.getSelectedItem() == "Domestic Waste"){
            payment += 150;
            if (waste1.isChecked()) {
                payment += 10;
            }
            if (waste2.isChecked()) {
                payment += 10;
            }
            if (waste3.isChecked()) {
                payment += 10;
            }
            if (waste4.isChecked()) {
                payment += 10;
            }
            if (waste5.isChecked()) {
                payment += 10;
            }
            if (waste6.isChecked()) {
                payment += 10;
            }
        }
        else if(dropdown.getSelectedItem() == "Medical Waste"){
            payment += 250;
            if (waste1.isChecked()) {
                payment += 20;
            }
            if (waste2.isChecked()) {
                payment += 20;
            }
            if (waste3.isChecked()) {
                payment += 20;
            }
            if (waste4.isChecked()) {
                payment += 20;
            }
            if (waste5.isChecked()) {
                payment += 20;
            }
            if (waste6.isChecked()) {
                payment += 20;
            }
        }

        return payment;
    }

}