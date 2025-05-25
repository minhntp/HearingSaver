package com.bkdn.nqminh.hearingsaver.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.activities.MainActivity;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnBluetoothBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnPluggedBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnRingerModeChangeBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class MyService extends Service {
    IntentFilter headsetReceiverFilter, bluetoothReceiverFilter, ringerModeReceiverFilter;
    OnPluggedBroadcastReceiver headsetBroadcastReceiver;
    OnRingerModeChangeBroadcastReceiver ringerModeBroadcastReceiver;
    OnBluetoothBroadcastReceiver bluetoothBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        buildAndShowNotification();
        adjustVolumesOnFirstRun();
        registerBroadcastReceivers();
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

        // Build notification
        Notification.Builder notificationBuilder = new Notification.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setContentIntent(notificationPendingIntent)
            .setOngoing(true);

        // Build channel
        NotificationChannel channel = new NotificationChannel(
            Constants.CHANNEL_ID,
            Constants.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_MIN);
        channel.setDescription(Constants.CHANNEL_DESCRIPTION);

        // Create/Register channel
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        // Show notification
//        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build());
        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build());
    }

    private void adjustVolumesOnFirstRun() {
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();

        boolean isFirstRun = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_FIRST_RUN, true);
        boolean adjustedOnFirstRun = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_ADJUSTED_ON_FIRST_RUN, false);

        if (isFirstRun && !adjustedOnFirstRun) {
            Operator.getInstance(context).handlePlugStateChange(context, Constants.MESSAGE_FIRST_RUN);
            sharedPreferences.edit().putBoolean(Constants.SHARED_PREFERENCE_ADJUSTED_ON_FIRST_RUN, true).apply();
        }
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void registerBroadcastReceivers() {
        // HEADSET_PLUG Broadcast Receiver
        headsetReceiverFilter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);
        headsetBroadcastReceiver = new OnPluggedBroadcastReceiver();

        // BLUETOOTH Broadcast Receiver
        bluetoothReceiverFilter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        bluetoothBroadcastReceiver = new OnBluetoothBroadcastReceiver();

        // RINGER_MODE_CHANGED Broadcast Receiver
        ringerModeReceiverFilter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        ringerModeBroadcastReceiver = new OnRingerModeChangeBroadcastReceiver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(headsetBroadcastReceiver, headsetReceiverFilter, Context.RECEIVER_NOT_EXPORTED);
            registerReceiver(bluetoothBroadcastReceiver, bluetoothReceiverFilter, Context.RECEIVER_NOT_EXPORTED);
            registerReceiver(ringerModeBroadcastReceiver, ringerModeReceiverFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(headsetBroadcastReceiver, headsetReceiverFilter);
            registerReceiver(bluetoothBroadcastReceiver, bluetoothReceiverFilter);
            registerReceiver(ringerModeBroadcastReceiver, ringerModeReceiverFilter);
        }
    }

    public void unregisterBroadcastReceivers() {
        unregisterReceiver(headsetBroadcastReceiver);
        unregisterReceiver(ringerModeBroadcastReceiver);
        unregisterReceiver(bluetoothBroadcastReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.DEBUG_TAG, "Service onDestroy()");
        unregisterBroadcastReceivers();
//        Intent onDestroyBroadcastReceiverIntent = new Intent(this, OnDestroyBroadcastReceiver.class);
//        sendBroadcast(onDestroyBroadcastReceiverIntent);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
