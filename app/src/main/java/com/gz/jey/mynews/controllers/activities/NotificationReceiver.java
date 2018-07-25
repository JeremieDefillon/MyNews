package com.gz.jey.mynews.controllers.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gz.jey.mynews.R;
import com.gz.jey.mynews.model.NewsSection;
import com.gz.jey.mynews.model.Result;
import com.gz.jey.mynews.utils.ApiStreams;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationReceiver.class.getSimpleName();

    public final String NOTIFICATION_CHANNEL_ID = "4565";
    public final String NOTIFICATION_CHANNEL_NAME = "MYNEWS";
    int importance = NotificationManager.IMPORTANCE_LOW;
    private String LASTURL;

    // Activity
    private Disposable disposable;
    private ArrayList<Result> results;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle extra = intent.getExtras();

        LASTURL = extra.getString("LASTURL");
        String query = extra.getString("NOTIF_QUERY");
        String fquery = extra.getString("NOTIF_FILTERS");
        String begin = "";
        String end = "";
        disposable = ApiStreams.streamFetchASearch(query, fquery, begin, end)
                .subscribeWith(new DisposableObserver<NewsSection>() {
                    @Override
                    public void onNext(NewsSection results) {
                        UpdateNews(results, context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {}
                });
    }


    private void UpdateNews(NewsSection news, Context context){
        Log.d(TAG, "UPDATE => " + String.valueOf(news.getResults().size()));
        results = new ArrayList<>();
        results.addAll(news.getResults());
        if(results.size()!=0) {
            if(!results.get(0).getUrl().equals(LASTURL)){
                LASTURL = results.get(0).getUrl();
                BuildNotification(context);
            }
        }else
            Log.d(TAG, "NOTHING");
    }

    private void disposeWhenDestroy(){
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    private void BuildNotification(Context context){
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
        repeating_intent.putExtra("QueryURL", results.get(0).getUrl());
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,987,
                repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_n)
                .setContentTitle("My News - " + results.get(0).getSection())
                .setContentText("Yours search found a New Article !")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(results.get(0).getTitle()))
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(987, builder.build());
    }


}
