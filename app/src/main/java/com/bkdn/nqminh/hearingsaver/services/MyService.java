package com.bkdn.nqminh.hearingsaver.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bkdn.nqminh.hearingsaver.activities.MainActivity;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnDestroyBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnRingerModeChangeBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.NotificationBuilder;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class MyService extends Service {
    public static boolean isRunning = false;

    IntentFilter ringerModeReceiverFilter;
    OnRingerModeChangeBroadcastReceiver ringerModeBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        buildAndShowNotification();
        adjustVolumesOnFirstRun();
        registerBroadcastReceivers();

        isRunning = true;
    }

    private void buildAndShowNotification() {
        // Intent for bring up MainActivity when notification is clicked
        // If MainActivity is in background => bring it to front
        // If not => start it.
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN); // Important
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER); // Important
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent notificationPendingIntent = PendingIntent
            .getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build Notification Channel
        NotificationChannel channel = new NotificationChannel(
            Constants.CHANNEL_ID,
            Constants.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_MIN);
        channel.setDescription(Constants.CHANNEL_DESCRIPTION);

        // Build Notification
        Notification notification = NotificationBuilder.getInstance().getNotification(this, notificationPendingIntent);

        // Create/Register channel
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        // Show notification
        startForeground(Constants.NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
    }

    private void adjustVolumesOnFirstRun() {
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();

        boolean isFirstRun = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_FIRST_RUN, true);
        boolean adjustedOnFirstRun = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_ADJUSTED_ON_FIRST_RUN, false);

        if (isFirstRun && !adjustedOnFirstRun) {
            Operator.getInstance(context).handlePlugStateChange(context);
            sharedPreferences.edit().putBoolean(Constants.SHARED_PREFERENCE_ADJUSTED_ON_FIRST_RUN, true).apply();
        }
    }

    private void registerBroadcastReceivers() {
        // RINGER_MODE_CHANGED Broadcast Receiver
        ringerModeReceiverFilter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        ringerModeBroadcastReceiver = new OnRingerModeChangeBroadcastReceiver();

        registerReceiver(ringerModeBroadcastReceiver, ringerModeReceiverFilter, Context.RECEIVER_NOT_EXPORTED);
    }

    public void unregisterBroadcastReceivers() {
        unregisterReceiver(ringerModeBroadcastReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.DEBUG_TAG, "Service onDestroy()");
        Toast.makeText(this, "HearingSaver: Service is killed", Toast.LENGTH_SHORT).show();
        unregisterBroadcastReceivers();

        Intent onDestroyBroadcastReceiverIntent = new Intent(this, OnDestroyBroadcastReceiver.class);
        sendBroadcast(onDestroyBroadcastReceiverIntent);

        isRunning = false;

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
