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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

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
    IntentFilter headsetReceiverFilter, bluetoothReceiverFilter, ringerModeReceiverFilter;
    OnPluggedBroadcastReceiver headsetBroadcastReceiver;
    OnRingerModeChangedBroadcastReceiver ringerModeBroadcastReceiver;
    OnBluetoothBroadcastReceiver bluetoothBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // -----------------------------------------------------------------------------------------
        // NOTIFICATION

        // Intent for opening MainActivity when notification is clicked
        Intent notificationIntent = Operator.getInstance(getApplicationContext()).getRunningIntent();
        if (notificationIntent == null) {
            notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        PendingIntent notificationPendingIntent = PendingIntent
                .getActivity(this, 0, notificationIntent, 0);

        // Build notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(Constants.NOTIFICATION_TITLE)
                .setContentText(Constants.NOTIFICATION_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(notificationPendingIntent)
                .setOngoing(true)
                .setSubText(Constants.NOTIFICATION_MESSAGE);

        // Build channel
        NotificationChannel channel = new NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(Constants.CHANNEL_DESCRIPTION);

        // Create/Register channel
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        // Show notification
//        NotificationManagerCompat.from(this).notify(Constants.NOTIFICATION_ID, notificationBuilder.build());
        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build());

        //------------------------------------------------------------------------------------------
        // ADJUST VOLUME ON FIRST RUN
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();

        boolean isFirstRun = sharedPreferences.getBoolean(Constants.SP_IS_FIRST_RUN, false);

        if (isFirstRun) {
            Operator.getInstance(context).handlePlugStateChange(context, Constants.MESSAGE_FIRST_RUN);
            // Keep isFirstRun to be true, so that HeadsetPluggedListener will not
            // adjust the volume again
        }

        // HEADSET_PLUG Broadcast Receiver
        headsetReceiverFilter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);
        headsetBroadcastReceiver = new OnPluggedBroadcastReceiver();
        registerReceiver(headsetBroadcastReceiver, headsetReceiverFilter);

        // BLUETOOTH Broadcast Receiver
        bluetoothReceiverFilter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        bluetoothBroadcastReceiver = new OnBluetoothBroadcastReceiver();
        registerReceiver(bluetoothBroadcastReceiver, bluetoothReceiverFilter);

        // RINGER_MODE_CHANGED Broadcast Receiver
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

        Toast.makeText(this, Constants.TOAST_SERVICE_DESTROYED, Toast.LENGTH_SHORT).show();

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
