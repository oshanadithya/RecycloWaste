package com.example.recyclowaste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class reviewAdd extends AppCompatActivity {
    EditText writereview;
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        writereview = (EditText) findViewById(R.id.et_wr);
        upload = (Button) findViewById(R.id.button_upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });
    }

    private void processinsert(){
        Map<String, Object> map=new HashMap<>();
        map.put("Review",writereview.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("review").child("Oshan").push()
        .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                writereview.setText("");
                Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Could not insert", Toast.LENGTH_LONG).show();
            }
        });
    }
}