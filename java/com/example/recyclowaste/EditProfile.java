
package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recyclowaste.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditProfile extends AppCompatActivity {
    EditText fname;
    EditText email;
    EditText telno;
    User user;
    DatabaseReference dbref;
    String key;
    FirebaseAuth firebaseAuth;
    FirebaseUser usr;
    AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        usr = firebaseAuth.getCurrentUser();

        fname = findViewById(R.id.inptFname);
        email = findViewById(R.id.inptEmail);
        telno = findViewById(R.id.inptTelno);

        user = (User) intent.getSerializableExtra("user");
        key = intent.getStringExtra("key");
        credential = EmailAuthProvider
                .getCredential(user.getEmail(), user.getPassword());


        fname.setText(user.getFname());
        email.setText(user.getEmail());
        telno.setText(user.getTelno());

    }

    public void onSave(View view) {
        if(!TextUtils.isEmpty(fname.getText()) && !TextUtils.isEmpty(email.getText())
        && !TextUtils.isEmpty(telno.getText())){
            user.setFname(fname.getText().toString());
            user.setEmail(email.getText().toString());
            user.setTelno(telno.getText().toString());

            DatabaseReference dbref  = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUsername()).child(key);
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChildren()){
                        usr.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
                                        usr.updateEmail(user.getEmail())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            dbref.child("fname").setValue(user.getFname());
                                                            dbref.child("email").setValue(user.getEmail());
                                                            dbref.child("telno").setValue(user.getTelno());

                                                            Toast.makeText(getApplicationContext(), "Successfull!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                });

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Please give all the details!", Toast.LENGTH_SHORT).show();
        }

    }
}
