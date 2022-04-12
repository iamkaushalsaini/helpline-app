package com.hlit.helplinetelecom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button pending_payments, view_complaint, Create_Estimate, view_Estimate,
            Create_Quotation, m_add_detail, m_view_details, create_po,
            view_po, add_customer, customer_account, add_reminder, attendance, locateUser;
    String id;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {
        home fragment = new home();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pending_payments = (Button)view.findViewById(R.id.pending_payments);
        view_complaint = (Button)view.findViewById(R.id.view_complaint);
        Create_Estimate = (Button)view.findViewById(R.id.Create_Estimate);
        view_Estimate = (Button)view.findViewById(R.id.view_Estimate);
        Create_Quotation = (Button)view.findViewById(R.id.Create_Quotation);
        m_add_detail = (Button)view.findViewById(R.id.m_add_detail);
        m_view_details = (Button)view.findViewById(R.id.m_view_details);
        create_po = (Button)view.findViewById(R.id.create_po);
        view_po = (Button)view.findViewById(R.id.view_po);
        add_customer = (Button)view.findViewById(R.id.add_customer);
        customer_account = (Button)view.findViewById(R.id.customer_account);
        add_reminder = (Button)view.findViewById(R.id.add_reminder);
        attendance = (Button)view.findViewById(R.id.attendance);
        locateUser = (Button)view.findViewById(R.id.locateUser);

        User user = SharedPrefManager.getInstance(getContext()).getUser();

        id    = String.valueOf(user.getId());

        if(id.equals("5")|| id.equals("6") || id.equals("23")){
            locateUser.setVisibility(View.VISIBLE);
        }



        m_add_detail.setOnClickListener(new View.OnClickListener() {
            String fragmentNmae = "m_add_detail";
            @Override
            public void onClick(View v) {
                moveIntent(fragmentNmae);
            }
        });

        m_view_details.setOnClickListener(new View.OnClickListener() {
            String fragmentNmae = "m_view_detail";
            @Override
            public void onClick(View v) {

                moveIntent(fragmentNmae);
            }
        });

        locateUser.setOnClickListener(new View.OnClickListener() {
            String fragmentNmae = "locateUser";
            @Override
            public void onClick(View v) {
                moveIntent(fragmentNmae);


            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), com.hlit.helplinetelecom.attendance.class);
                startActivity(intent);
            }
        });




        return view;
    }

    private void moveIntent(String fragmentName) {
        Intent intent = new Intent(getContext(),btnEvents.class);
        intent.putExtra("fragmentName", fragmentName);
        startActivity(intent);
    }
}