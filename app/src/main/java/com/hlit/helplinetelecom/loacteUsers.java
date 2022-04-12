package com.hlit.helplinetelecom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loacteUsers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loacteUsers extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView employeeView;
    ArrayList<userModel> employList = new ArrayList<>();

    public loacteUsers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loacteUsers.
     */
    // TODO: Rename and change types and number of parameters
    public static loacteUsers newInstance(String param1, String param2) {
        loacteUsers fragment = new loacteUsers();
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
        View view = inflater.inflate(R.layout.fragment_loacte_users, container, false);
        employeeView =(RecyclerView)view.findViewById(R.id.locateUsers);
        employeeView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        loadEmployees();

        return view;
    }

    private void loadEmployees() {

        StringRequest request = new StringRequest(Request.Method.POST, URLs.loadEmployees, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        employList.add(new userModel(
                                jsonObject.getString("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("mobile")

                        ));

                    }
                    locateUserAdapter locateUserAdapter = new locateUserAdapter(employList,getContext());
                    employeeView.setAdapter(locateUserAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}