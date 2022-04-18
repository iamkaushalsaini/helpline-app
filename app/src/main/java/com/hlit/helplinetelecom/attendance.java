package com.hlit.helplinetelecom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class attendance extends AppCompatActivity {

    private static final String CHANNEL_ID = "101";
    Button onLoaction;
    String value ="0";
    private static final String TAG ="PushNotification";

    FusedLocationProviderClient client;
    LocationRequest locationRequest;
    ProgressDialog progressDialog;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        id = String.valueOf(user.getId()).trim();
        if(id.equals("5")|| id.equals("6") || id.equals("23")){
            FirebaseMessaging.getInstance().subscribeToTopic("admin");

        }


        onLoaction =(Button)findViewById(R.id.onLoactionBtn);

        if(isLocationServiceRunning()){
            onLoaction.setText("Check Out");
            onLoaction.setBackgroundColor(Color.parseColor("#46DD0F"));
            value ="1";

        }
        CreateNotificationChannel();
        getToken();





        onLoaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(value.equals("0")){


                    if(isGPSenable()){


                        progressDialog = new ProgressDialog(attendance.this);
                        progressDialog.setMessage("Checking In...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        requestAppPermissions();

                        onLoaction.setText("Check Out");
                        onLoaction.setBackgroundColor(Color.parseColor("#46DD0F"));
                        value ="1";

                    }
                    /*if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        enableLocationSetting();
                    }else {
                        requestAppPermissions();

                    }*/

                }else {
                   // stopLocationService();
                    progressDialog = new ProgressDialog(attendance.this);
                    progressDialog.setMessage("Checking Out...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    checkIn();

                    onLoaction.setText("Check In");
                    onLoaction.setBackgroundColor(Color.parseColor("#3079EA"));
                    value ="0";



                }


            }
        });





       /* Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                12,
                23,
                0

        );

        setAlarm(calendar.getTimeInMillis());
*/

        client = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    }

    private  void stopLocationUpdate(){

        client.removeLocationUpdates(locationCallback);
    }

    private void checkIn() {

        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                startLocationUpdates();



            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());




    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if(locationResult == null){
                return;
            }

            loadlocations(locationResult);

            //  getmyLocation(locationResult);


        }
    };

    private void loadlocations(LocationResult locationResult) {
        List<Location> location = locationResult.getLocations();
        stopLocationUpdate();
        LatLng latLng = new LatLng(location.get(0).getLatitude(), location.get(0).getLongitude());

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(attendance.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.get(0).getLatitude(), location.get(0).getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            String locationadd = address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName;

            if(isLocationServiceRunning()){

                checkOut(locationadd, locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

            }else {
                sendLoaction(locationadd, locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkOut(String locationadd, double latitude, double longitude) {

        User user = SharedPrefManager.getInstance(attendance.this).getUser();
        StringRequest request = new StringRequest(Request.Method.POST, URLs.checkOut, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                // Toast.makeText(LocationService.this, response.toString(),Toast.LENGTH_SHORT).show();



                    stopLocationService();


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
                param.put("user_id",String.valueOf(user.getId()));
                param.put("user_name",user.getName().trim());
                param.put("loaction",locationadd);
                param.put("latitude",String.valueOf(latitude));
                param.put("longitude",String.valueOf(longitude));


                return param;
            }
        };

        VolleySingleton.getInstance(attendance.this).addToRequestQueue(request);

    }

    private void sendLoaction(String locationadd, double latitude, double longitude) {


        User user = SharedPrefManager.getInstance(attendance.this).getUser();

        StringRequest request = new StringRequest(Request.Method.POST, URLs.checkIn, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                // Toast.makeText(LocationService.this, response.toString(),Toast.LENGTH_SHORT).show();



                    startLocationService();


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
                param.put("user_id",String.valueOf(user.getId()));
                param.put("user_name",user.getName().trim());
                param.put("loaction",locationadd);
                param.put("latitude",String.valueOf(latitude));
                param.put("longitude",String.valueOf(longitude));


                return param;
            }
        };

        VolleySingleton.getInstance(attendance.this).addToRequestQueue(request);

    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ExeutableService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY, pendingIntent);


    }

    private boolean isGPSenable() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnable){
            return true;
        }else {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,9001);
        }

        return false;
    }

    private void requestAppPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Dexter.withContext(attendance.this)
                    .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    /*.withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if(multiplePermissionsReport.areAllPermissionsGranted()){
                                startService(new Intent(attendance.this,ForegroundService.class));


                            }
                            if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){

                            }


                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();

                        }
                    }).onSameThread()
                    .check();
        {


            }*/

                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            //startService(new Intent(attendance.this,ForegroundService.class));
                           //  startLocationService();

                            checkIn();



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

    }

    private void AlStart() {
        AlarmHandler alarmHandler = new AlarmHandler(attendance.this);
        alarmHandler.cancelAlaram();
        alarmHandler.setAlaramManager();
        Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"set Alaram",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9001){

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(providerEnable){

                Toast.makeText(getApplicationContext(),"Location is enabled",Toast.LENGTH_SHORT).show();

              /*  Intent intent1 = new Intent(addressForm.this, mapActivity.class);
                startActivity(intent1);*/


            }else {

                Toast.makeText(getApplicationContext(),"Location is not enabled",Toast.LENGTH_SHORT).show();


            }
        }
    }


    private boolean isLocationServiceRunning(){
        ActivityManager activityManager =
                (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for (ActivityManager.RunningServiceInfo serviceInfo:
            activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(serviceInfo.service.getClassName())){
                    if(serviceInfo.foreground){
                        return true;
                    }
                }

            }
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constant.ACTION_START_LOCATION_SERVICE);
            startService(intent);

            Toast.makeText(getApplicationContext(),"Check In",Toast.LENGTH_SHORT).show();

        }
    }
    private void stopLocationService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constant.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(getApplicationContext(),"Check Out",Toast.LENGTH_SHORT).show();

        }
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"onComplete Faild to get Token");
                }
                String token = task.getResult();
                Log.d(TAG,"onComplete: "+token);

            }
        });

    }

    private void CreateNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "firebaseNotificationChannel";
            String description = "Receive Firebase Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


}