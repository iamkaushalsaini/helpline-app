package com.hlit.helplinetelecom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

public class btnEvents extends AppCompatActivity {

    String fragmentName;
    Fragment  temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn_events);

        Intent intent = getIntent();
        fragmentName = intent.getStringExtra("fragmentName");



        switch (fragmentName)
        {
            case "m_add_detail":
                temp = new m_add_detail();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();
                break;

            case "m_view_detail":
                temp = new m_view_detail();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();
                break;

            case "locateUser":
                temp = new loacteUsers();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();
                break;
        }





    }
}