package com.hlit.helplinetelecom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class view_all_detail extends AppCompatActivity {

    RecyclerView customerView;
    ProgressDialog progressDialog;
    ArrayList<view_all_detail_model> customertList = new ArrayList<>();
    String role, dateText;
    TextView date, totalCount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_detail);

        customerView = (RecyclerView)findViewById(R.id.customerList);
        customerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));


        date = (TextView)findViewById(R.id.date);
        totalCount = (TextView)findViewById(R.id.totalCount);


        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        dateText = intent.getStringExtra("date");
        date.setText(dateText);

        viewAllCustomerDetails();


    }

    private void viewAllCustomerDetails() {
        progressDialog = new ProgressDialog(view_all_detail.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        StringRequest request = new StringRequest(Request.Method.POST, URLs.view_cus_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                // Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        customertList.add(new view_all_detail_model(
                                jsonObject1.getString("mdate"),
                                jsonObject1.getString("cname"),
                                jsonObject1.getString("cmobile"),
                                jsonObject1.getString("caddress"),
                                jsonObject1.getString("cproduct"),
                                jsonObject1.getString("nextmdate"),
                                jsonObject1.getString("cstatus"),
                                jsonObject1.getString("actaken"),
                                jsonObject1.getString("tt"),
                                jsonObject1.getString("cus_type")

                        ));

                    }

                    view_all_detail_adapter adapter = new view_all_detail_adapter(customertList,getApplicationContext());
                    customerView.setAdapter(adapter);
                    totalCount.setText("Total:"+customertList.size());




                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("role", role);
                param.put("date", dateText);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }
}