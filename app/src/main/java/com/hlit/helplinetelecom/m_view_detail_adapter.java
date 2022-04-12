package com.hlit.helplinetelecom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class m_view_detail_adapter extends RecyclerView.Adapter<m_view_detail_adapter.myviewholder> {
    ArrayList<m_view_detail_model> data;
    Context applicationContext;


    public m_view_detail_adapter(ArrayList<m_view_detail_model> data, Context applicationContext) {
        this.data = data;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public m_view_detail_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketing_list, parent, false);
        return new m_view_detail_adapter.myviewholder(view);

        //parent.getContext()
    }

    @Override
    public void onBindViewHolder(@NonNull m_view_detail_adapter.myviewholder holder, int position) {


        final m_view_detail_model temp = data.get(position);
        holder.date.setText(data.get(position).getMdate());
        holder.gmail.setText(data.get(position).getRole());


        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.date.getContext(), view_all_detail.class);
                intent.putExtra("role", temp.getRole());
                intent.putExtra("date", temp.getMdate());
                holder.date.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {


        TextView date, gmail;
        Button viewBtn;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            gmail = itemView.findViewById(R.id.gmail);
            viewBtn = itemView.findViewById(R.id.viewDetail);


        }
    }

}



