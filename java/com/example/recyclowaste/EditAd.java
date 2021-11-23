package com.example.recyclowaste;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.recyclowaste.model.Advertisment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditAd extends AppCompatActivity {

    TextView et_price;
    TextView et_quantity;
    TextView et_title;
    TextView et_description;
    ImageView imgAd;
    Advertisment ad;
    DatabaseReference dbRef;
    String key;
    String username;
    FirebaseAuth firebaseAuth;
    Button btnChoooseImage;
    Uri mImageUri;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ad);

        ad = new Advertisment();
        et_price = findViewById(R.id.post_prdtPrice);
        et_quantity = findViewById(R.id.et_post_prdtQuantity);
        et_title = findViewById(R.id.et_post_prdtTitle);
        et_description = findViewById(R.id.et_post_prdt_Desc);
        imgAd = findViewById(R.id.img_displayProductEdit);
        firebaseAuth = FirebaseAuth.getInstance();
        username = firebaseAuth.getCurrentUser().getDisplayName();
        btnChoooseImage = findViewById(R.id.btn_edit_addImage);

        Intent intent = getIntent();

        key = intent.getStringExtra("key");

        Log.d("ADebugTag", "Value: " + key);
        //imgAd.setImageURI();

       dbRef = FirebaseDatabase.getInstance().getReference().child("Advertisment").child(username).child(key);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    et_title.setText(snapshot.child("title").getValue().toString());
                    et_quantity.setText(snapshot.child("quantity").getValue().toString());
                    et_description.setText(snapshot.child("description").getValue().toString());
                    et_price.setText(snapshot.child("price").getValue().toString());
                    Picasso.get().load(snapshot.child("image").getValue().toString().trim()).into(imgAd);

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error in fetching data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ChooseImage(View view){
        openFileChooser();
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResult.launch(intent);


    }

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        mImageUri = data.getData();

                        imgAd.setImageURI(mImageUri);

                    }
                }
            }
    );

    public void editAd(View view){

        DatabaseReference updRef = FirebaseDatabase.getInstance().getReference().child("Advertisment").child(username).child(key);
        storageRef = FirebaseStorage.getInstance().getReference().child("Advertisment");
        updRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                  /*  ad.setTitle(et_title.getText().toString().trim());
                    ad.setQuantity(Integer.parseInt(et_quantity.getText().toString().trim()));
                    ad.setPrice(Float.parseFloat(et_price.getText().toString().trim()));
                    ad.setDescription(et_description.getText().toString().trim());*/
                }
                else{
                    Toast.makeText(getApplicationContext(), "Data can not be fetched!", Toast.LENGTH_SHORT).show();
                }
                if(mImageUri != null){

                    StorageReference fileReference = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

                    fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Advertisment ad = new Advertisment(et_title.getText().toString().trim(), et_description.getText().toString().trim(), uri.toString(), Float.parseFloat(et_price.getText().toString().trim()), Integer.parseInt(et_quantity.getText().toString().trim()));
                               //     dbRef.push().setValue(ad);
                                    dbRef.child("description").setValue(ad.getDescription());
                                    dbRef.child("image").setValue(ad.getImage());
                                    dbRef.child("price").setValue(ad.getPrice());
                                    dbRef.child("quantity").setValue(ad.getQuantity());
                                    dbRef.child("title").setValue(ad.getTitle());
                                }
                            });
                        }
                    });




                Toast.makeText(getApplicationContext(),"Advertisment Updated!",Toast.LENGTH_SHORT).show();
                dbRef = FirebaseDatabase.getInstance().getReference().child("Advertisment").child(username).child(key);
                dbRef.setValue(ad);
                /*Intent intent = new Intent(getApplicationContext() , adPosted.class);
                startActivity(intent);*/

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error in updating!",Toast.LENGTH_SHORT).show();
            }
        });


    }

        private String getFileExtension(Uri uri){
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}