package com.hlit.helplinetelecom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    SearchView searchView;




    //inflating menus ......
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem notification = menu.findItem(R.id.notification);


        return super.onCreateOptionsMenu(menu);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new home()).commit();


        }else {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);


        }

        navigationView = (NavigationView)findViewById(R.id.navmenu);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        searchView = (SearchView)findViewById(R.id.tempsearch);






        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setTitle("AC BAZAAAR");


        ColorStateList iconColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#3079EA"),
                        Color.parseColor("#3079EA")
                }
        );


        //  loadCartItems();










      /*  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("key",query);
                Fragment fragment = new searchFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                fragment.setArguments(bundle);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/
       /* if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            *//*navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.navigation_menu);*//*
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);

        }else {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
        }*/


        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment temp;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:

                        temp = new home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();
                        break;

                    case R.id.profile:

                        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                            temp = new profile();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();

                        }else {

                            Intent intent = new Intent(MainActivity.this, login.class);
                            startActivity(intent);
                        }


                        break;

                   /* case R.id.profile:

                        logout();
                        break;

                    case R.id.login:

                        Intent intent = new Intent(MainActivity.this , register.class);
                        startActivity(intent);
                        break;*/

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        // on click listener on Toolbar menus
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                   case R.id.notification:
                       User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                      String id = String.valueOf(user.getId()).trim();
                       if(id.equals("5")|| id.equals("6") || id.equals("23")){
                           Intent intent = new Intent(MainActivity.this, notificationActivity.class);
                           startActivity(intent);
                       }

                        break;



                }


                return true;
            }
        });


    }



}