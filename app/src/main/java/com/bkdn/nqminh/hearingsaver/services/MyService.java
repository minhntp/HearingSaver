package com.bkdn.nqminh.hearingsaver.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.activities.MainActivity;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnDestroyBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnPluggedBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnRingerModeChangedBroadcastReceiver;

@TargetApi(Build.VERSION_CODES.O)
public class MyService extends Service {
//    private static final int NOTIFICATION_ID = 96;
//    private static final String CHANNEL_ID = "CN-HS";
//    private Notification notification;
//    private Notification.Builder mBuilder;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel-hearing-saver";
    private static final String CHANNEL_NAME = "Service is running";
    IntentFilter headsetReceiverFilter, ringerModeReceiverFilter;
    OnPluggedBroadcastReceiver headsetBroadcastReceiver;
    OnRingerModeChangedBroadcastReceiver ringerModeBroadcastReceiver;
    Notification.Builder mBuilder;
    NotificationChannel mNotificationChannel;
    NotificationManager mNotificationManager;

    PendingIntent contentIntent;

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
                .setContentIntent(contentIntent)
                .setChannelId(CHANNEL_ID);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mNotificationChannel);
        startForeground(NOTIFICATION_ID, mBuilder.build());

        headsetReceiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        headsetBroadcastReceiver = new OnPluggedBroadcastReceiver();
        registerReceiver(headsetBroadcastReceiver, headsetReceiverFilter);

        ringerModeReceiverFilter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        ringerModeBroadcastReceiver = new OnRingerModeChangedBroadcastReceiver();
        registerReceiver(ringerModeBroadcastReceiver, ringerModeReceiverFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        unregisterReceiver(headsetBroadcastReceiver);
        unregisterReceiver(ringerModeBroadcastReceiver);

        Intent onDestroyBroadcastReceiverIntent = new Intent(this, OnDestroyBroadcastReceiver.class);
        sendBroadcast(onDestroyBroadcastReceiverIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
