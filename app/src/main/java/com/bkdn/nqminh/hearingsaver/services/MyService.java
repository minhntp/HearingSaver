package com.bkdn.nqminh.hearingsaver.services;

import android.annotation.TargetApi;
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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.activities.MainActivity;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnBluetoothBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnDestroyBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnPluggedBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnRingerModeChangedBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

@TargetApi(Build.VERSION_CODES.O)
public class MyService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "01";
    private static final String CHANNEL_NAME = "Hearing Saver Service";
    private static final String CHANNEL_DESCRIPTION = "Hearing Saver Service status";
    IntentFilter headsetReceiverFilter, bluetoothReceiverFilter, ringerModeReceiverFilter;
    OnPluggedBroadcastReceiver headsetBroadcastReceiver;
    OnRingerModeChangedBroadcastReceiver ringerModeBroadcastReceiver;
    OnBluetoothBroadcastReceiver bluetoothBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the NotificationChannel, but only on API 26+ (Android 8.0) because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MIN);
            channel.setDescription(CHANNEL_DESCRIPTION);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        // Create an explicit intent for opening an Activity when notification in clicked
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle("Service is running")
//                .setContentText("...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        // Adjust volume if this is the first run
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();
        boolean isFirstRunMyService = sharedPreferences.getBoolean(Constants.firstRunMyService, false);
        if (isFirstRunMyService) {
            Operator.getInstance(context).adjustOnPlugging(context, 4);
            sharedPreferences.edit().putBoolean(Constants.firstRunMyService, false).apply();
        }

        // HEADSET_PLUG BroadcastReceiver
        headsetReceiverFilter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);
        headsetBroadcastReceiver = new OnPluggedBroadcastReceiver();
        registerReceiver(headsetBroadcastReceiver, headsetReceiverFilter);
//        Toast.makeText(context, "registered for plug", Toast.LENGTH_SHORT).show();

        // BLUETOOTH BroadcastReceiver
        bluetoothReceiverFilter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        bluetoothBroadcastReceiver = new OnBluetoothBroadcastReceiver();
        registerReceiver(bluetoothBroadcastReceiver, bluetoothReceiverFilter);
//        Toast.makeText(context, "registered for bluetooth", Toast.LENGTH_SHORT).show();

        // RINGER_MODE_CHANGED BroadcastReceiver
        ringerModeReceiverFilter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        ringerModeBroadcastReceiver = new OnRingerModeChangedBroadcastReceiver();
        registerReceiver(ringerModeBroadcastReceiver, ringerModeReceiverFilter);
//        Toast.makeText(context, "registered for ringer mode", Toast.LENGTH_SHORT).show();
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
        unregisterReceiver(bluetoothBroadcastReceiver);

        Intent onDestroyBroadcastReceiverIntent = new Intent(this, OnDestroyBroadcastReceiver.class);
        sendBroadcast(onDestroyBroadcastReceiverIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
