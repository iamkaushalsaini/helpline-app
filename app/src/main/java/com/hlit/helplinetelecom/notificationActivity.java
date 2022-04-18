package com.hlit.helplinetelecom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class notificationActivity extends AppCompatActivity {
    RecyclerView notificationView;
    ArrayList<notificationListModel> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationView = (RecyclerView)findViewById(R.id.notificationView);
        notificationView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

        loadNotifications();
    }

    private void loadNotifications() {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.getAllNotification, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0; i<array.length(); i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        notificationList.add(new notificationListModel(
                           jsonObject.getString("name"),
                           jsonObject.getString("date"),
                           jsonObject.getString("time"),
                                jsonObject.getString("status")
                        ));

                    }

                    notificationListAdapter adapter = new notificationListAdapter(notificationList,getApplicationContext());
                    notificationView.setAdapter(adapter);



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
}