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

public class locateUserAdapter extends RecyclerView.Adapter<locateUserAdapter.myviewholder> {

    ArrayList<userModel> data;
    Context applicationContext;

    public locateUserAdapter(ArrayList<userModel> data, Context applicationContext) {
        this.data = data;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row, parent, false);
        return new locateUserAdapter.myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        final userModel temp = data.get(position);

        holder.name.setText(temp.getName());
        holder.email.setText(temp.getEmail());
        holder.mobile.setText(temp.getMobile());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.name.getContext(),SingleEmployeLocation.class);
                intent.putExtra("id",temp.getId());
                holder.name.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView name, email, mobile;
        CardView card;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.name);
            email =itemView.findViewById(R.id.email);
            mobile =itemView.findViewById(R.id.mobile);
            card =itemView.findViewById(R.id.employeeCard);
        }
    }
}
