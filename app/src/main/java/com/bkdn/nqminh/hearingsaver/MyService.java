package com.bkdn.nqminh.hearingsaver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class MyService extends Service {
//    private static final int NOTIFICATION_ID = 96;
//    private static final String CHANNEL_ID = "CN-HS";
//    private Notification notification;
//    private Notification.Builder mBuilder;

    IntentFilter receiverFilter;
    MyBroadcastReceiver receiverBroadcast;
    Context appCxt;

    NotificationCompat.Builder mBuilder;
    private static final String CHANNEL_ID = "CN-HS";
    PendingIntent contentIntent;

    public MyService(Context applicationContext) {
        appCxt = applicationContext;
        Log.d("HERE","Here I am!");
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle("Service is running")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent);

        startForeground(1, mBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForeground(NOTIFICATION_ID, notification);
        super.onStartCommand(intent, flags, startId);

        receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        receiverBroadcast = new MyBroadcastReceiver();
        registerReceiver(receiverBroadcast, receiverFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent myServiceStaterIntent = new Intent(this, MyServiceStarter.class);
        unregisterReceiver(receiverBroadcast);
        sendBroadcast(myServiceStaterIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
