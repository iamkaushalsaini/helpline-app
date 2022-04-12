package com.hlit.helplinetelecom;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExeutableService  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent intent2 = new Intent(context, LocationService.class);
            intent2.setAction(Constant.ACTION_START_LOCATION_SERVICE);
            context.startService(intent2);
            //  context.startService(new Intent(context,ForegroundService.class));

            Toast.makeText(context,"Check In",Toast.LENGTH_SHORT).show();
            // Toast.makeText(context,"Ok",Toast.LENGTH_SHORT).show();

        }

        if(!common.isConnectedToInternet(context)){
            Toast.makeText(context,"net off",Toast.LENGTH_SHORT).show();

            LocationService.action = "Of";



        }else{
            LocationService.action = "On";
            User user = SharedPrefManager.getInstance(context).getUser();
             String userId =  String.valueOf(user.getId());

            ArrayList<LocalLocationModel> locationList = new ArrayList<>();
            locationList.clear();

            Cursor cursor = new DbHandler(context).readAllData(userId);
            while (cursor.moveToNext()){

                LocalLocationModel locationModel = new LocalLocationModel(cursor.getString(0),
                        cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),
                        cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10));

                locationList.add(locationModel);
            }

            Gson gson = new Gson();

           String locations = gson.toJson(locationList);
           if(!locationList.isEmpty()){

              // Toast.makeText(context,locations,Toast.LENGTH_SHORT).show();
               sendFromLocalToServer(context, locations);

           }





        }


        if(intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)){
            ContentResolver contentResolver = context.getContentResolver();
            int mode = Settings.Secure.getInt(
                    contentResolver, Settings.Secure.LOCATION_MODE,Settings.Secure.LOCATION_MODE_OFF);
            if(mode != Settings.Secure.LOCATION_MODE_OFF){
                Toast.makeText(context, "Location On", Toast.LENGTH_SHORT).show();

            }else {

                Toast.makeText(context, "Location off", Toast.LENGTH_SHORT).show();

            }

        }



       /// if(intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION))



    }

    private void sendFromLocalToServer(Context context, String locations) {

///        Toast.makeText(context,locations.toString(),Toast.LENGTH_SHORT).show();
        User user = SharedPrefManager.getInstance(context).getUser();
        String userId =  String.valueOf(user.getId());

        StringRequest request = new StringRequest(Request.Method.POST, URLs.sendFromLocalToServer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                if(response.trim().equals("successful")){
                    DbHandler dbHandler = new DbHandler(context);
                    dbHandler.deleteCartAll();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("locationsList",locations);
                param.put("userId",userId);
                return param;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);


    }


}
