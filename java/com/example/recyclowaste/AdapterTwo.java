package com.example.recyclowaste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recyclowaste.model.ReviewTwo;
import java.util.ArrayList;

public class AdapterTwo extends RecyclerView.Adapter<AdapterTwo.MyViewHolder> {

    Context context;

    ArrayList<ReviewTwo> list;

    public AdapterTwo(Context context, ArrayList<ReviewTwo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reviewitem_design, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReviewTwo reviewTwo = list.get(position);
        holder.review.setText(reviewTwo.getRev());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView review;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            review = itemView.findViewById(R.id.tvReview);
        }
    }

}
