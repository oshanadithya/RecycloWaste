package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recyclowaste.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    EditText fname,username,email,telNo,pass,cpass;
    Button submit;
    DatabaseReference dbRef;
    User user;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fname = findViewById(R.id.etFname);
        username = findViewById(R.id.etUsername);
        email = findViewById(R.id.etEmail);
        telNo = findViewById(R.id.etTelno);
        pass = findViewById(R.id.etPwd);
        cpass = findViewById(R.id.etCnfrmPwd);
        submit = findViewById(R.id.SignUp_btn);
        user = new User();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


    }
    public void submitClick (View view){
        Toast.makeText(getApplicationContext(), "Submit Clicked", Toast.LENGTH_SHORT).show();
        String uName = username.getText().toString().trim();
        dbRef = FirebaseDatabase.getInstance().getReference().child("User").child(uName);


        if(TextUtils.isEmpty(fname.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            fname.setError("Field is Empty!");
        }

        if(TextUtils.isEmpty(uName)){
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
            username.setError("Field is Empty!");
        }

         if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            email.setError("Field is Empty!");
        }

         if(TextUtils.isEmpty(telNo.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter your Telephone Number", Toast.LENGTH_SHORT).show();
            telNo.setError("Field is Empty!");
        }

         if(TextUtils.isEmpty(pass.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            pass.setError("Field is Empty!");
        }

         if(TextUtils.isEmpty(cpass.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please confirm pass", Toast.LENGTH_SHORT).show();
            cpass.setError("Field is Empty!");
        }
         if(!pass.getText().toString().equals(cpass.getText().toString())  ){
                Toast.makeText(getApplicationContext(), "Passwords not matching!", Toast.LENGTH_SHORT).show();
                pass.setError("Please input a different password");
                pass.setText("");
                cpass.setText("");
           //     validations(uName);
                }
        else{
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(uName)){
                            Toast.makeText(getApplicationContext(), "Username Already Exists!", Toast.LENGTH_SHORT).show();
                            username.setError("Username already taken!");
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Setting names", Toast.LENGTH_SHORT).show();
                            user.setUsername(uName.trim());
                            user.setEmail(email.getText().toString().trim());
                            user.setFname(fname.getText().toString().trim());
                            user.setPassword(pass.getText().toString().trim());
                            user.setTelno(telNo.getText().toString().trim());


                            PerformAuth(user);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
 /*           dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(uName)){
                        Toast.makeText(getApplicationContext(), "Username Already Exists!", Toast.LENGTH_SHORT).show();
                        username.setError("Username already taken!");
                    }
                    else{

                        user.setUsername(uName.trim());
                        user.setEmail(email.getText().toString().trim());
                        user.setFname(fname.getText().toString().trim());
                        user.setPassword(pass.getText().toString().trim());
                        user.setTelno(telNo.getText().toString().trim());


                        PerformAuth(user);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            });*/




/*        if(!pass.getText().toString().equals(cpass.getText().toString())  ){
            Toast.makeText(getApplicationContext(), "Passwords not matching!", Toast.LENGTH_SHORT).show();
            pass.setError("Please input a different password");
            pass.setText("");
            cpass.setText("");
            validations(uName);
        }
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uName)){
                    Toast.makeText(getApplicationContext(), "Username Already Exists!", Toast.LENGTH_SHORT).show();
                    username.setError("Username already taken!");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Setting names", Toast.LENGTH_SHORT).show();
                    user.setUsername(uName.trim());
                    user.setEmail(email.getText().toString().trim());
                    user.setFname(fname.getText().toString().trim());
                    user.setPassword(pass.getText().toString().trim());
                    user.setTelno(telNo.getText().toString().trim());


                        PerformAuth(user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    private void PerformAuth(User user) {
        String mailAuth = email.getText().toString().trim();
        String password = pass.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(mailAuth,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserProfileChangeRequest setUsername = new UserProfileChangeRequest.Builder().setDisplayName(user.getUsername()).build();

                    mUser.updateProfile(setUsername).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d("Set Username :", "Success");
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(),"Registration Succesful" , Toast.LENGTH_SHORT).show();
                    dbRef.child(user.getUsername()).setValue(user);
                    //Intent to sign in
                    Intent intent = new Intent(getApplicationContext() , Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter a password with 6 characters or more", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error"+e , Toast.LENGTH_LONG).show();
                Log.d("Ftag", "onFailure: "+e);
            }
        });
    }


  /*  public void validations(String uName){

        if(TextUtils.isEmpty(fname.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            fname.setError("Field is Empty!");
        }

        else if(TextUtils.isEmpty(uName)){
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
            username.setError("Field is Empty!");
        }

        else if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            email.setError("Field is Empty!");
        }

        else if(TextUtils.isEmpty(telNo.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter your Telephone Number", Toast.LENGTH_SHORT).show();
            telNo.setError("Field is Empty!");
        }

        else if(TextUtils.isEmpty(pass.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            pass.setError("Field is Empty!");
        }

        else if(TextUtils.isEmpty(cpass.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please confirm pass", Toast.LENGTH_SHORT).show();
            cpass.setError("Field is Empty!");
        }

    }*/



}