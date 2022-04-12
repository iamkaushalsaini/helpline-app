package com.hlit.helplinetelecom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class attendance extends AppCompatActivity {

    Button onLoaction;
    String value ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        onLoaction =(Button)findViewById(R.id.onLoactionBtn);

        if(isLocationServiceRunning()){
            onLoaction.setText("Check Out");
            onLoaction.setBackgroundColor(Color.parseColor("#46DD0F"));
            value ="1";

        }


        onLoaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(value.equals("0")){


                    if(isGPSenable()){

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
                    stopLocationService();

                    onLoaction.setText("Check In");
                    onLoaction.setBackgroundColor(Color.parseColor("#3079EA"));
                    value ="0";


                }


            }
        });




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
                             startLocationService();



                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();

                        }
                    }).check();
        }else{

            Dexter.withContext(attendance.this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)

                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            //startService(new Intent(attendance.this,ForegroundService.class));
                            startLocationService();



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


}