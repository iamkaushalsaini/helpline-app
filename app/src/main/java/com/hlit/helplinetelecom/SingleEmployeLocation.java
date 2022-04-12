package com.hlit.helplinetelecom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleEmployeLocation extends AppCompatActivity {
    RecyclerView dateView;
    ArrayList<locationModel> dateList = new ArrayList<>();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_employe_location);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        dateView =(RecyclerView)findViewById(R.id.datesView);
        dateView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));


        loadDates();
    }

    private void loadDates() {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.loadLocationDates, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        dateList.add(new locationModel(
                                jsonObject.getString("id"),
                                jsonObject.getString("user_id"),
                                jsonObject.getString("date"),
                                jsonObject.getString("time"),
                                jsonObject.getString("month"),
                                jsonObject.getString("month_year"),
                                jsonObject.getString("year"),
                                jsonObject.getString("time_stamp"),
                                jsonObject.getString("location"),
                                jsonObject.getString("latitude"),
                                jsonObject.getString("longitude"),
                                jsonObject.getString("day")

                        ));
                    }

                   locationAdapter locationAdapter = new locationAdapter(dateList, getApplicationContext());
                    dateView.setAdapter(locationAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("userId", id);
                return param;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
}