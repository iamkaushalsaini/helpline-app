package com.hlit.helplinetelecom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class view_all_detail_adapter  extends RecyclerView.Adapter<view_all_detail_adapter.myviewholder> {
    ArrayList<view_all_detail_model> data;
    Context applicationContext;


    public view_all_detail_adapter(ArrayList<view_all_detail_model> data, Context applicationContext) {
        this.data = data;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public view_all_detail_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.role_market_list, parent, false);
        return new view_all_detail_adapter.myviewholder(view);

        //parent.getContext()
    }

    @Override
    public void onBindViewHolder(@NonNull view_all_detail_adapter.myviewholder holder, int position) {


        final view_all_detail_model temp = data.get(position);
        holder.date.setText(data.get(position).getMdate());
        holder.time.setText(data.get(position).getTt());
        holder.name.setText(temp.getCname());
        holder.number.setText(temp.getCmobile());
        holder.address.setText(temp.getCaddress());
        holder.product.setText(temp.getCproduct());
        holder.nextMeetDate.setText(temp.getNextmdate());
        holder.status.setText(temp.getCstatus());
        holder.action.setText(temp.getActaken());
        holder.customerType.setText(temp.getCus_type());

       /* if(temp.getCstatus().trim().equals("hot")){
            holder.status.setTextColor(Color.YELLOW);
        } else if (temp.getCstatus().trim().equals("warm")){
            holder.status.setTextColor(Color.GREEN);

        }*/
        holder.makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = holder.number.getText().toString().trim();
                requestPermmision(number, holder);


            }
        });



    }
    private  void requestPermmision(String number, myviewholder holder) {
        Dexter.withContext(holder.name.getContext())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        dileCall(number, holder);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    private void dileCall(String number, myviewholder holder) {
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:"+number));
        holder.name.getContext().startActivity(intentCall);


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {


        TextView date, time, name, number, address, product, nextMeetDate, status,action, customerType;
        ImageButton makeCall;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            address = itemView.findViewById(R.id.address);
            product = itemView.findViewById(R.id.product);
            nextMeetDate = itemView.findViewById(R.id.nextMeetingDate);
            status = itemView.findViewById(R.id.status);
            action = itemView.findViewById(R.id.actionTaken);
            customerType = itemView.findViewById(R.id.customerType);
            makeCall = itemView.findViewById(R.id.makeCall);


        }
    }

}

