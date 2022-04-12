package com.hlit.helplinetelecom;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocationService extends Service {

   public static String action ="On";


    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                //Toast.makeText(LocationService.this, String.valueOf(latitude))
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(LocationService.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    String locationadd = address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName;

                 //   Toast.makeText(LocationService.this, locationadd,Toast.LENGTH_SHORT).show();

                  //
                   if(action.equals("Of")){
                       saveInLocal(locationadd, locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());


                   }else {
                       sendLoaction(locationadd, locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

                   }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    };

    private void saveInLocal(String locationadd, double latitude, double longitude) {

        String date;
        String time;
        String month;
        String month_year;
        String year;
        String time_stamp;
        String location;
        String Slatitude;
        String Slongitude;
        String day;
        String note;


        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();

        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calendar.getTime()).toString();

        simpleDateFormat = new SimpleDateFormat("LLLL");
        month = simpleDateFormat.format(calendar.getTime()).toString();

        simpleDateFormat = new SimpleDateFormat("MM-YYYY");
        month_year = simpleDateFormat.format(calendar.getTime()).toString();

        simpleDateFormat = new SimpleDateFormat("yyyy");
        year = simpleDateFormat.format(calendar.getTime()).toString();

        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time_stamp = simpleDateFormat.format(calendar.getTime()).toString();

        simpleDateFormat = new SimpleDateFormat("E");
        day = simpleDateFormat.format(calendar.getTime()).toString();

        User user = SharedPrefManager.getInstance(LocationService.this).getUser();
        int userID =  user.getId();


        DbHandler dbHandler = new DbHandler(LocationService.this);
        String res=dbHandler.addRecord(userID, date, time, month, month_year, year, time_stamp,locationadd,String.valueOf(latitude),String.valueOf(longitude),day,"Net OFF");
        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();

    }

   /* public static   boolean isGpsEnabled(Context context){
        ContentResolver contentResolver = context.getContentResolver();

        int mode = Settings.Secure.getInt(
                contentResolver, Settings.Secure.LOCATION_MODE,Settings.Secure.LOCATION_MODE_OFF);
        if(mode != Settings.Secure.LOCATION_MODE_OFF){
            return true;
        }else {
            return false;
        }

    }*/



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Helpline Telecom");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("On Duty");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Helpline Telecom"
                        , NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("location on");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constant.LOCATION_SERVICE_ID, builder.build());


        ExeutableService networkChangeListener = new ExeutableService();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        IntentFilter filter2 = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(networkChangeListener, filter2);






    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(false);
        stopSelf();
      /*  Intent intent = new Intent(this, LocationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        AlarmManager alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            if(action != null){
                if(action.equals(Constant.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();

                }else if(action.equals(Constant.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();

                }

            }

        }



        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void sendLoaction(String locationadd, double latitude, double longitude) {

        User user = SharedPrefManager.getInstance(LocationService.this).getUser();

        StringRequest request = new StringRequest(Request.Method.POST, URLs.sendLocation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Toast.makeText(LocationService.this, response.toString(),Toast.LENGTH_SHORT).show();

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

        VolleySingleton.getInstance(LocationService.this).addToRequestQueue(request);
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();

        Intent myIntent = new Intent(getApplicationContext(),LocationService.class);
        myIntent.setAction(Constant.ACTION_START_LOCATION_SERVICE);
        startService(myIntent);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),0,myIntent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND,10);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Intent myIntent = new Intent(getApplicationContext(),LocationService.class);
        myIntent.setAction(Constant.ACTION_START_LOCATION_SERVICE);
        startService(myIntent);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),0,myIntent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND,10);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();

    }*/
}
