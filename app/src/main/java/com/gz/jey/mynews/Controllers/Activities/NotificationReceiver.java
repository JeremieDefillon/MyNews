package com.gz.jey.mynews.Controllers.Activities;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import static com.gz.jey.mynews.Controllers.Activities.MainActivity.NOTIF_QUERY;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.NOTIF_FILTERS;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.QUERY;

import android.support.v4.app.NotificationCompat;

import com.gz.jey.mynews.R;


public class NotificationReceiver extends BroadcastReceiver{

    public final String NOTIFICATION_CHANNEL_ID = "4565";
    public final String NOTIFICATION_CHANNEL_NAME = "MYNEWS";
    int importance = NotificationManager.IMPORTANCE_LOW;


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager;
        //Notification Channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }else{
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }



        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.putExtra("NotiClick", true);
        repeating_intent.putExtra("QUERY", NOTIF_QUERY);
        repeating_intent.putExtra("FILTERS", NOTIF_FILTERS);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,987,
                repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_n)
                .setContentTitle("My News - "+NOTIF_FILTERS)
                .setContentText("Your query have been upgraded ! search for : " + QUERY)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(987, builder.build());
    }


}
