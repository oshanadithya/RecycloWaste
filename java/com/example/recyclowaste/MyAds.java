package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclowaste.model.Advertisment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAds extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdapterMyAds adapter;
    private DatabaseReference dbRef;
    private List<Advertisment> ads;
    private List<String> keys;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth checkUser;
    private String username;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads2);

 /*       checkUser = FirebaseAuth.getInstance();
        if (checkUser.getCurrentUser() == null) {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }*/
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = checkUser.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(MyAds.this,Login.class);
                    startActivity(intent);

                }
            }
        };

        recyclerView = findViewById(R.id.MyAdsRecyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this ));

        ads = new ArrayList<>();
        keys = new ArrayList<>();

        username = firebaseAuth.getCurrentUser().getDisplayName();
        Log.d("FTAG", "onCreate: d"+username);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Advertisment").child(username);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren() ){
                    ads.add(new Advertisment(snap.child("title").getValue().toString(), snap.child("description").getValue().toString(), snap.child("image").getValue().toString()
                    ,Float.parseFloat(snap.child("price").getValue().toString()), Integer.parseInt(snap.child("quantity").getValue().toString())));

                    keys.add(snap.getKey().toString());
                }

                adapter = new AdapterMyAds(MyAds.this,ads,keys);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }







    /*public void onClickEdit(View view){
        Intent intent = new Intent(MyAds.this , EditAd.class);
        intent.putExtra("title","Sample Title");
        intent.putExtra("Description","Sample Description");
        intent.putExtra("price","10");
        intent.putExtra("quantity","4");
        intent.putExtra(Intent.EXTRA_STREAM , imageURI);
        intent.putExtra("key","-MkGmb-IdBBKNZ27tqKI");
        startActivity(intent);
    }*/
}