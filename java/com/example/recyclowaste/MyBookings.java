package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recyclowaste.model.Booking;
import com.example.recyclowaste.model.UserLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class MyBookings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView myBookingsView;
    Adapter adapter;
    ArrayList<Booking> bookingArray;
    ArrayList<String> keyArray;
    ArrayList<Booking> list;
    ArrayList<String> keys;
    DatabaseReference dbref;
    Spinner dropdown;
    String username;
    Loader loader;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        myBookingsView = findViewById(R.id.myBookingsView);
        myBookingsView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth  = FirebaseAuth.getInstance();

        dropdown = findViewById(R.id.sortSpinner);

        String[] items = new String[]{"Newest", "Oldest"};
        loader = new Loader(this);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter2);
        dropdown.setOnItemSelectedListener(this);

        username = firebaseAuth.getCurrentUser().getDisplayName();




        list = new ArrayList<>();
        keys = new ArrayList<>();
        bookingArray = new ArrayList<>();
        keyArray = new ArrayList<>();

        loader.showLoadingDialog();
        dbref = FirebaseDatabase.getInstance().getReference().child("Booking").child(username);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getBookings(snapshot);
                sortByNewest();
                loader.dismissLoadingDialog();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
            }
        });


    }

    public void getBookings(DataSnapshot snapshot) {
        if(snapshot.hasChildren()) {
            for(DataSnapshot snap : snapshot.getChildren()) {
                UserLocation location = new UserLocation(snap.child("location").child("locality").getValue().toString(),
                        Double.parseDouble(snap.child("location").child("latitude").getValue().toString()),
                        Double.parseDouble(snap.child("location").child("longitude").getValue().toString()));

                bookingArray.add(new Booking(snap.child("driver").getValue().toString(), snap.child("type").getValue().toString(), location
                        , snap.child("date").getValue().toString(), snap.child("time").getValue().toString(), snap.child("includes").getValue().toString(),
                        Double.parseDouble(snap.child("payment").getValue().toString())));

                keyArray.add(snap.getKey().toString());
            }
        }
    }

    public void sortByNewest() {

        list.clear();
        keys.clear();

        for(int i = bookingArray.size()-1; i >= 0; i--) {
            list.add(bookingArray.get(i));
        }

        for(int i = keyArray.size()-1; i >= 0; i--) {
            keys.add(keyArray.get(i));
        }

        adapter = new Adapter(MyBookings.this, list, keys);
        myBookingsView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void sortByOldest() {
        list.clear();
        keys.clear();

        for(int i = 0; i < bookingArray.size();i++) {
            list.add(bookingArray.get(i));
        }

        for(int i = 0; i < keyArray.size(); i++) {
            keys.add(keyArray.get(i));
        }

        adapter = new Adapter(MyBookings.this, list, keys);
        myBookingsView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(dropdown.getSelectedItem() == "Newest"){
           sortByNewest();
        }
        else if(dropdown.getSelectedItem() == "Oldest"){
            sortByOldest();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}