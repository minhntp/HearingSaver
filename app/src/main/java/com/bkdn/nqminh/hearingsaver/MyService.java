package com.bkdn.nqminh.hearingsaver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.O)
public class MyService extends Service {
//    private static final int NOTIFICATION_ID = 96;
//    private static final String CHANNEL_ID = "CN-HS";
//    private Notification notification;
//    private Notification.Builder mBuilder;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel-hearing-saver";
    private static final String CHANNEL_NAME = "Service is running";
    IntentFilter receiverFilter;
    MyBroadcastReceiver receiverBroadcast;
    Context appCxt;
    Notification.Builder mBuilder;
    NotificationChannel mNotificationChannel;
    NotificationManager mNotificationManager;

    PendingIntent contentIntent;

    public MyService(Context applicationContext) {
        appCxt = applicationContext;
        Log.d("HERE", "Here I am!");
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);

        contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle("Service is running")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(contentIntent)
                .setChannelId(CHANNEL_ID);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mNotificationChannel);

        startForeground(NOTIFICATION_ID, mBuilder.build());
        Toast.makeText(getApplicationContext(), "Notification created!", Toast.LENGTH_SHORT)
                .show();
        receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        receiverBroadcast = new MyBroadcastReceiver();
        registerReceiver(receiverBroadcast, receiverFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForeground(NOTIFICATION_ID, notification);
        super.onStartCommand(intent, flags, startId);


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
