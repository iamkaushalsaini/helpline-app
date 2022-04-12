package com.hlit.helplinetelecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends BackableFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView phone_number,userEmail,userFullname;
    Button btnLogout;
    ProgressDialog waitForLogut;
    ShapeableImageView userImg;
    String encodedImage, username, email;
    Bitmap bitmap;
    ImageView profileImg;


    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userEmail = view.findViewById(R.id.textViewEmail);
        userFullname = view.findViewById(R.id.textViewFullname);
        phone_number = view.findViewById(R.id.textViewphoneNumber);
        btnLogout = view.findViewById(R.id.buttonLogout);

        User user = SharedPrefManager.getInstance(view.getContext()).getUser();

        username = user.getName();
        email = user.getEmail();




        userEmail.setText(user.getEmail());
        userFullname.setText(user.getName());
        phone_number.setText(user.getMobile());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(v.equals(btnLogout)){
                    SharedPrefManager.getInstance(getActivity()).logout();

                    waitForLogut = new ProgressDialog(getContext());
                    waitForLogut.setMessage("Please wait...");
                    // progressDialog.setTitle();
                    waitForLogut.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    waitForLogut.show();
                    waitForLogut.setCancelable(false);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                waitForLogut.dismiss();
                                Intent intent1 = new Intent(getContext(),login.class);
                                startActivity(intent1);


                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }).start();

                }

            }
        });


        return view;
    }

    @Override
    protected void onBackButtonPressed() {
        if(SharedPrefManager.getInstance(getContext()).isLoggedIn()){
            Fragment fragment = new home();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment ).commit();


        }else {


        }




    }
}