package com.hlit.helplinetelecom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    ArrayList<locationModel> dateList = new ArrayList<>();
    String id, let, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        let = intent.getStringExtra("let");
        lon = intent.getStringExtra("long");


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // getmyLocation();
                        openMap(let, lon);

                        //loadLocations();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }

    private void loadLocations() {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.loadMarksLocation, new Response.Listener<String>() {
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

                    //openMap(dateList);

                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("userId",id);
                return param;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }


    private  void openMap(String let, String lon){

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                Double lat,lot;

                lat = Double.valueOf(let);
                lot = Double.valueOf(lon);
                LatLng latLng = new LatLng(lat,lot);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                Geocoder geocoder;
                List<Address> addresses;

                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat, lot,1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    String fullAdd = address+" "+city+" "+state+" "+postalCode+" "+knownName;



                        // BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.color.green);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(fullAdd);
                        googleMap.addMarker(markerOptions);



                    // shopAddress.setText(address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName);


                } catch (IOException e) {
                    e.printStackTrace();
                }






                //  Double lat = 28.840190 , lot = 78.748310;

                // LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
               // LatLng latLng = new LatLng(lat,lot);

               // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));







            }
        });


    }
}