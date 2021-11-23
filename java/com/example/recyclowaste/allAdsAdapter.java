package com.example.recyclowaste;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclowaste.model.Advertisment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class allAdsAdapter extends RecyclerView.Adapter<allAdsAdapter.adsViewHolder>{

    private Context context;
    private List<Advertisment> ads;
    private List<String> users;
    private String username;
    private DatabaseReference dbRef;
    private String TelNo;

    public allAdsAdapter(Context context , List<Advertisment> ads , List<String> users){
        this.context = context;
        this.ads = ads;
        this.users = users;
    }

    @NonNull
    @Override
    public adsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.all_ads_card,parent,false);
        adsViewHolder adsView = new adsViewHolder(view);
        return adsView;
    }

    @Override
    public void onBindViewHolder(@NonNull adsViewHolder holder, int position) {
            Advertisment adCurrent = ads.get(position);
            username = users.get(position);
            holder.title.setText(adCurrent.getTitle());
            holder.description.setText(adCurrent.getDescription());

        Log.d("Image Value", "onBindViewHolder: " + adCurrent.getImage().trim() );
            Picasso.get().load(adCurrent.getImage().trim()).into(holder.image);
            holder.price.setText("Rs. "+Float.toString(adCurrent.getPrice()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.quantity_array,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(0);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int quantity = Integer.parseInt((String) adapterView.getItemAtPosition(i)) ;
             //   Float price = Float.valueOf(holder.price.getText().toString());
                Float price = Float.valueOf(adCurrent.getPrice());
                Float total = quantity * price;
                holder.price.setText(total.toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("User").child(username).child(username);
                System.out.print(dbRef);

                Log.d("FTAG", "onClick: "+TelNo);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        System.out.print(snapshot.child("telno").getValue().toString());
                        TelNo = snapshot.child("telno").getValue().toString();


                        Log.d("fTag", "Tel Number: "+TelNo);
                        Uri call = Uri.parse("tel:" + TelNo);
                        Intent intent = new Intent(Intent.ACTION_DIAL,call);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(), "Number not available", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }


    @Override
    public int getItemCount() {
        return ads.size();
    }

    public class adsViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title,description,price;
        Spinner spinner;
        Button contact;
        public adsViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.img_all_ads);
            description = itemView.findViewById(R.id.desc_all_ads);
            title = itemView.findViewById(R.id.title_all_ads);
            price = itemView.findViewById(R.id.price_all_ads);
            spinner = (Spinner)itemView.findViewById(R.id.spinner_all_ads);
            contact = itemView.findViewById(R.id.contact_all_ads);

        }
    }
}
