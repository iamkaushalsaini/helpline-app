package com.hlit.helplinetelecom;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link m_view_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class m_view_detail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView m_details;
    ProgressDialog progressDialog;

    ArrayList<m_view_detail_model> marketList = new ArrayList<>();

    public m_view_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment m_view_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static m_view_detail newInstance(String param1, String param2) {
        m_view_detail fragment = new m_view_detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_m_view_detail, container, false);
        m_details = (RecyclerView)view.findViewById(R.id.marketList);
        m_details.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));


        viewMarketDetails();

        return view;
    }

    private void viewMarketDetails() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        StringRequest request = new StringRequest(Request.Method.POST, URLs.view_m_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                // Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        marketList.add(new m_view_detail_model(
                                jsonObject1.getString("mdate"),
                                jsonObject1.getString("role")

                        ));

                    }

                    m_view_detail_adapter adapter = new m_view_detail_adapter(marketList, getContext());
                    m_details.setAdapter(adapter);



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });/* {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();

                param.put("cate_id", id);
                param.put("shortType", shortType);
                return param;
            }
        };*/
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);


    }
}