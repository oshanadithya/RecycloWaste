package com.example.recyclowaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclowaste.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    User user;
    TextView tv_name;
    TextView userEmail;
    TextView userTelno;
    DatabaseReference dbref;
    String username;
    FirebaseAuth firebaseAuth;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        tv_name = findViewById(R.id.tv_name);
        userEmail = findViewById(R.id.userEmail);
        userTelno = findViewById(R.id.userTelno);
        firebaseAuth = FirebaseAuth.getInstance();

        username = firebaseAuth.getCurrentUser().getDisplayName();

        Loader loader = new Loader(this);

        loader.showLoadingDialog();
        dbref = FirebaseDatabase.getInstance().getReference().child("User").child(username);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(snapshot.hasChildren()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        user = new User(snap.child("fname").getValue().toString(), snap.child("username").getValue().toString(),
                                snap.child("email").getValue().toString(), snap.child("telno").getValue().toString(),
                                snap.child("password").getValue().toString()
                        );
                        key = snap.getKey().toString();
                        tv_name.setText(user.getFname());
                        userEmail.setText(user.getEmail());
                        userTelno.setText(user.getTelno());
                        break;
                    }
                }
                loader.dismissLoadingDialog();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
            }
        });
    }

    public void openEditProfile(View view){
        Intent editProfile = new Intent(this, EditProfile.class);
        editProfile.putExtra("key", key);
        editProfile.putExtra("user", user);
        startActivity(editProfile);
    }
}