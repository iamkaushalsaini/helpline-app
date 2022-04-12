package com.hlit.helplinetelecom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHandler {
    private Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }
    public void setAlaramManager(){
        Intent intent = new Intent(context, ExeutableService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null){
            long trigetAfter = 180000;
            long trigetEvery = 180000;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,trigetAfter,trigetEvery,sender);

        }


    }

    public void cancelAlaram(){
        Intent intent = new Intent(context, ExeutableService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null){
            alarmManager.cancel(sender);

        }


    }
}
