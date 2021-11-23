package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button btn_signIn;
    EditText email , password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView btn_Google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_Signin_Email);
        password = findViewById(R.id.et_Sign_NewPass);
        btn_signIn = findViewById(R.id.btn_SIgnIn);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        btn_Google = findViewById(R.id.btn_Google);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

        btn_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GoogleSignInActivivity.class);
                startActivity(intent);
            }
        });
    }


    private void performLogin(){
        String emailLog = email.getText().toString().trim();
        String passLog = password.getText().toString().trim();

        //Add validations


        mAuth.signInWithEmailAndPassword(emailLog,passLog).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_SHORT).show();
                    //Intent to Homepage
                    Intent intent = new Intent(getApplicationContext(),BookPickup.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    email.setError("Check again");
                    password.setError("Check Again");
                }
            }
        });
    }

    public void Signup(View view){
        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }
}