package com.hlit.helplinetelecom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notificationListAdapter  extends RecyclerView.Adapter<notificationListAdapter.myviewHolder> {
    ArrayList<notificationListModel> data;
    Context applicationContext;

    public notificationListAdapter(ArrayList<notificationListModel> data, Context applicationContext) {
        this.data = data;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public notificationListAdapter.myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row,parent,false);
        return new notificationListAdapter.myviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewHolder holder, int position) {

        final notificationListModel temp = data.get(position);
        holder.name.setText(temp.getName());
        holder.status.setText(temp.getStatus());
        holder.dateTime.setText(temp.getDate()+"||"+temp.getTime());



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myviewHolder extends RecyclerView.ViewHolder {
        TextView name, status, dateTime;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            dateTime = itemView.findViewById(R.id.dateTime);
        }
    }
}
