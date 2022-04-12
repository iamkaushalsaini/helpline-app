package com.hlit.helplinetelecom;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link m_add_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class m_add_detail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText date, name, cutomerType, number, address, product, nextMeatingDate, status, actionTaken;
    ImageButton loadCalender;
    Button submit;
    int Cyear, Cmonth, Cday;
    int Chour, Cmint;
    TextView time;
    ProgressDialog progressDialog;
    String email;

    public m_add_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment m_add_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static m_add_detail newInstance(String param1, String param2) {
        m_add_detail fragment = new m_add_detail();
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
        View view = inflater.inflate(R.layout.fragment_m_add_detail, container, false);
        date = (EditText)view.findViewById(R.id.currentDate);
        name = (EditText)view.findViewById(R.id.name);
        cutomerType = (EditText)view.findViewById(R.id.customer_type);
        number = (EditText)view.findViewById(R.id.number);
        address = (EditText)view.findViewById(R.id.address);
        product = (EditText)view.findViewById(R.id.product);
        nextMeatingDate = (EditText)view.findViewById(R.id.nextMeatingDate);
        status = (EditText)view.findViewById(R.id.status);
        actionTaken = (EditText)view.findViewById(R.id.action_taken);
        //time = (TextView) view.findViewById(R.id.time);

        loadCalender = (ImageButton)view.findViewById(R.id.loadCalender);
        submit = (Button)view.findViewById(R.id.submit);


        date.setText(getCurrentDate());

        User user = SharedPrefManager.getInstance(view.getContext()).getUser();
        email = user.getEmail();







        loadCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                Cyear = calendar.get(Calendar.YEAR);
                Cmonth = calendar.get(Calendar.MONTH);
                Cday = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nextMeatingDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                    }
                },Cyear,Cmonth,Cday);
                datePickerDialog.show();



            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitMarketDetails();
            }
        });


        return view;
    }

    private String getCurrentTime(){
        return new SimpleDateFormat("hh:mm:ss a",Locale.getDefault()).format(new Date());
    }

    private String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(new Date());
    }

    private void submitMarketDetails() {

        final String currentDate = date.getText().toString();
        final String nameEdittext = name.getText().toString();
        final String customerTypeEdittext = cutomerType.getText().toString();
        final String numberEdittext = number.getText().toString();
        final String addressEdittext = address.getText().toString();
        final String productEdittext = product.getText().toString();
        final String nextMeatingDateEdittext = nextMeatingDate.getText().toString();
        final String statusEdittext = status.getText().toString();
        final String actionTakenEditText = actionTaken.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(currentDate)) {
            date.setError("Please enter the Today Date");
            date.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nameEdittext)) {
            name.setError("Please enter the Name");
            name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(customerTypeEdittext)) {
            cutomerType.setError("Please enter the Customer Type");
            cutomerType.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(numberEdittext)) {
            number.setError("Please enter the Number");
            number.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(addressEdittext)) {
            address.setError("Please enter the Address");
            address.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(productEdittext)) {
            product.setError("Please enter the Product");
            product.requestFocus();
            return;
        }
       /* if (TextUtils.isEmpty(nextMeatingDateEdittext)) {
            nextMeatingDate.setError("Please enter the Next Meeting date");
            nextMeatingDate.requestFocus();
            return;
        }*/

        /*if (TextUtils.isEmpty(statusEdittext)) {
            status.setError("Please enter the Status");
            status.requestFocus();
            return;
        }*/

       /* if (TextUtils.isEmpty(actionTakenEditText)) {
            actionTaken.setError("Please enter the Action");
            actionTaken.requestFocus();
            return;
        }*/

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("submitting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        StringRequest request = new StringRequest(Request.Method.POST, URLs.submitMarketDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response.trim().equals("Submitted")) {
                    AlertDialog.Builder alertdilog = new AlertDialog.Builder(getContext());
                    alertdilog.setTitle(response.toString());
                    alertdilog.setCancelable(false);
                    // alertdilog.setMessage(warrantyPdfName);

                    alertdilog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           // dialog.dismiss();
                            getActivity().finish();

                        }
                    });

                    AlertDialog alertDialogshow = alertdilog.create();
                    alertDialogshow.show();
                }else {
                    Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("date",currentDate);
                param.put("name", nameEdittext);
                param.put("customerType", customerTypeEdittext);
                param.put("number", numberEdittext);
                param.put("address", addressEdittext);
                param.put("product", productEdittext);
                param.put("nextMeetingDate", nextMeatingDateEdittext);
                param.put("status", statusEdittext);
                param.put("actionTaken", actionTakenEditText);
                param.put("time",getCurrentTime());
                param.put("email",email);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);



    }


}