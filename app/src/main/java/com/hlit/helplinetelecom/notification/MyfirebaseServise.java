package com.hlit.helplinetelecom.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hlit.helplinetelecom.MainActivity;
import com.hlit.helplinetelecom.MapActivity;
import com.hlit.helplinetelecom.R;
import com.hlit.helplinetelecom.attendance;

public class MyfirebaseServise extends FirebaseMessagingService {

    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101" ;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
       // super.onMessageReceived(message);

        /*Log.d(TAG,"onMassageReceived "+message.getNotification().getTitle());
        Log.d(TAG,"onMassageReceived "+message.getNotification().getBody());*/

        showNotifications(message.getNotification().getTitle(),message.getNotification().getBody());
    }

    private void showNotifications(String title, String massage){
        Intent intent = new Intent(this, attendance.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(massage)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(1,builder.build());


    }
}
