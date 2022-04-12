package com.hlit.helplinetelecom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class locationAdapter extends RecyclerView.Adapter<locationAdapter.myViewHolder> {
    ArrayList<locationModel> data;
    Context applicationContext;

    public locationAdapter(ArrayList<locationModel> data, Context applicationContext) {
        this.data = data;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_row, parent, false);
        return new locationAdapter.myViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final  locationModel temp = data.get(position);
        holder.date.setText(temp.getDate());
        holder.month.setText(temp.getMonth());
        holder.time.setText(temp.getTime());
        holder.location.setText(temp.getLocation());
        holder.dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.date.getContext(), MapActivity.class);
                intent.putExtra("let",temp.getLatitude());
                intent.putExtra("long",temp.getLongitude());
                holder.date.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView month, date, time, location;
        CardView dateCard;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            month = itemView.findViewById(R.id.month);
            date = itemView.findViewById(R.id.date);
            dateCard = itemView.findViewById(R.id.dateCard);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
        }
    }
}
