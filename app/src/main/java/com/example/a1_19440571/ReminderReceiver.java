package com.example.a1_19440571;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.a1_19440571.R;

import java.util.Date;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int Unique_Integer_Number;

        //--Get date from previous intent
        Bundle bundle = intent.getExtras();
        String nameTXT = bundle.getString("nameTXT");
        String locationTXT = bundle.getString("locationTXT");
        String eventdescriptionTXT = bundle.getString("eventdescriptionTXT");
        String start_time = bundle.getString("start_time");
        String end_time = bundle.getString("end_time");

        //--Create notification using the inserted event description
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"My Notification");
        builder.setContentTitle(nameTXT);
        builder.setContentText(start_time + " - " + end_time + " | " + locationTXT + " | " + eventdescriptionTXT );
        builder.setSmallIcon(R.drawable.remind_blue_icon);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        //--Generate a unique number for each notification id
        Unique_Integer_Number =  (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        managerCompat.notify(Unique_Integer_Number,builder.build());
    }
}
