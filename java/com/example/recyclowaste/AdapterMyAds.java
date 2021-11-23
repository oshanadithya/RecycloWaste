package com.example.recyclowaste;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclowaste.model.Advertisment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterMyAds extends RecyclerView.Adapter<AdapterMyAds.ItemViewHolder> {

    private Context context;
    private List<Advertisment> ads;
    private List<String> keys;
    private String username;
    private FirebaseAuth firebaseAuth;



    public AdapterMyAds(Context context , List<Advertisment> ads , List<String> keys){
        this.context = context;
        this.ads = ads;
        this.keys = keys;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.ads_item_card,parent,false);
        firebaseAuth = FirebaseAuth.getInstance();
        username = firebaseAuth.getCurrentUser().getDisplayName();
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Advertisment adCurrent = ads.get(position);
        holder.description.setText(adCurrent.getDescription());
        holder.title.setText(adCurrent.getTitle());

        holder.price.setText(Float.toString(adCurrent.getPrice()));
        Picasso.get().load(adCurrent.getImage().trim()).into(holder.image);
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent data = new Intent(context,EditAd.class);
                data.putExtra("key",keys.get(position));
                Log.d("ADebugTag", "Value: " + keys.get(position));
                context.startActivity(data);
                ((MyAds)context).finish();

            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("Advertisment").child(username);
                deleteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(keys.get(position))){
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Advertisment").child(username).child(keys.get(position));
                            AlertDialog.Builder confirm = new AlertDialog.Builder(context);
                            confirm.setTitle("Deletion Confirmation");
                            confirm.setMessage("Are you sure you want to delete this item?");
                            confirm.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dbRef.removeValue();
                                    removeItemAt(position);
                                }
                            }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alert = confirm.create();
                            alert.show();

                            Toast.makeText(context.getApplicationContext(), "Ad Deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context.getApplicationContext(), "No source to Delete!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
    public void removeItemAt(int position) {
        ads.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, ads.size());
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public EditText description;
        public EditText price;
        public ImageView image;
        public TextView title;
        public Button btn_edit;
        public Button btn_delete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.et_productDesc);
            image = itemView.findViewById(R.id.img_productMyAds);
           price = itemView.findViewById(R.id.et_itemPrice);
           title = itemView.findViewById(R.id.tv_MyAds_Title);
           btn_edit = itemView.findViewById(R.id.btn_edit_myAds);
           btn_delete = itemView.findViewById(R.id.btn_delete_myAds);
        }
    }
}
