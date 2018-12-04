package com.bkdn.nqminh.hearingsaver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;

public class MyService extends Service {
    private static final int NOTIFICATION_ID = 96;
    private static final String CHANNEL_ID = "CN-HS";
    private Notification notification;
    private Notification.Builder mBuilder;
    IntentFilter receiverFilter;
    MyBroadcastReceiver receiver;

    NotificationManager myNotificationManager;
    Notification.Builder myNotificationBuilder;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        myNotificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myNotificationBuilder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            myNotificationBuilder = new Notification.Builder(getApplicationContext());
        }

        notification = myNotificationBuilder
                .setSmallIcon(R.drawable.ear_icon)
                .setContentTitle("Hearing Saver")
                .setContentText("Service is running. Your ears are protected!")
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        myNotificationManager.notify(NOTIFICATION_ID, notification);

        receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, receiverFilter);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
