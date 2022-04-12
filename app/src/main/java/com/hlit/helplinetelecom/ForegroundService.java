package com.hlit.helplinetelecom;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.security.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForegroundService  extends Service {

    private  final  IBinder mBinder = new MyBinder();
    private  static final  String CHANNEL_ID ="2";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();

    }


    private void buildNotification() {
        String stop ="stop";
        PendingIntent broadcastIntnt = PendingIntent.getBroadcast(this,0,new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(getString(R.string.app_name))
                .setContentText("On Duty")
                .setOngoing(true)
                .setContentIntent(broadcastIntnt);

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(false);
            channel.setDescription("You are on Duty");
            channel.setSound(null,null);

            NotificationManager manager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        startForeground(1,builder.build());
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(3000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission == PackageManager.PERMISSION_GRANTED){
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    String location = "Latitude:"+ locationResult.getLastLocation().getLatitude()+
                            "\nLongitude:"+
                            locationResult.getLastLocation().getLongitude();
                    Toast.makeText(ForegroundService.this, location, Toast.LENGTH_SHORT).show();

                    Geocoder geocoder;
                    List<Address> addresses;

                    geocoder = new Geocoder(ForegroundService.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1);
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        String locationadd = address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName;





                        sendLoaction(locationadd, locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

                      /*  MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
                        googleMap.addMarker(markerOptions);

                        addressForm.fullAddress.setText(address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
*/

                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                }
            },null);
        }else {
            stopSelf();
        }



    }

    private void sendLoaction(String locationadd, double latitude, double longitude) {

        User user = SharedPrefManager.getInstance(ForegroundService.this).getUser();

        StringRequest request = new StringRequest(Request.Method.POST, URLs.sendLocation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(ForegroundService.this, response.toString(),Toast.LENGTH_SHORT).show();

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
                param.put("loaction",locationadd);
                param.put("latitude",String.valueOf(latitude));
                param.put("longitude",String.valueOf(longitude));


                return param;
            }
        };

        VolleySingleton.getInstance(ForegroundService.this).addToRequestQueue(request);
    }

    public class MyBinder extends Binder{
        public ForegroundService getService(){
            return ForegroundService.this;
        }


    }

}
