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
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bkdn.nqminh.hearingsaver.activities.MainActivity;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.NotificationDismissedReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnDestroyBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.OnRingerModeChangeBroadcastReceiver;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.NotificationBuilder;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private static final String AUDIO_CALLBACK_THREAD = "HearingSaver_AudioDeviceCallback_HandlerThread";
    private static Notification mNotification;

    private Context mContext;
    private OnRingerModeChangeBroadcastReceiver mRingerModeBroadcastReceiver;
    private AudioDeviceCallback mAudioDeviceCallback;
    private HandlerThread mAudioCallbackHandlerThread;
    private Handler mAudioCallbackHandler;

    public static boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mAudioCallbackHandlerThread = new HandlerThread(AUDIO_CALLBACK_THREAD);
        mAudioCallbackHandlerThread.start();
        mAudioCallbackHandler = new Handler(mAudioCallbackHandlerThread.getLooper());

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
        mNotification = NotificationBuilder.getInstance().getNotification(this, notificationPendingIntent);

        // Create/Register channel
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        showForegroundNotification();
    }

    private void showForegroundNotification() {
        startForeground(Constants.NOTIFICATION_ID, mNotification);
    }

    private void adjustVolumesOnFirstRun() {
        Context context = mContext;
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
        IntentFilter mRingerModeReceiverFilter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        mRingerModeBroadcastReceiver = new OnRingerModeChangeBroadcastReceiver();

        registerReceiver(mRingerModeBroadcastReceiver, mRingerModeReceiverFilter, Context.RECEIVER_NOT_EXPORTED);
    }

    public void unregisterBroadcastReceivers() {
        unregisterReceiver(mRingerModeBroadcastReceiver);
    }

    private void registerAudioCallbacks() {
        if (mAudioDeviceCallback == null) {
            mAudioDeviceCallback = new AudioDeviceCallback() {
                @Override
                public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                    Log.d(TAG, "onAudioDevicesAdded");
                    super.onAudioDevicesAdded(addedDevices);
                    Operator.getInstance(mContext).handlePlugStateChange(mContext);
                }

                @Override
                public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                    Log.d(TAG, "onAudioDevicesRemoved");
                    super.onAudioDevicesRemoved(removedDevices);
                    Operator.getInstance(mContext).handlePlugStateChange(mContext);
                }
            };
        }

        Operator.getInstance(mContext).registerAudioCallback(mAudioDeviceCallback, mAudioCallbackHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String action = intent.getAction();
        if (action == null) {
            action = "";
        }
        switch (action) {
            case NotificationDismissedReceiver.ACTION:
            case OnDestroyBroadcastReceiver.ACTION:
                showForegroundNotification();
                break;
            default:
                registerAudioCallbacks();
                buildAndShowNotification();
                adjustVolumesOnFirstRun();
                registerBroadcastReceivers();
                break;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.DEBUG_TAG, "Service onDestroy()");
        Toast.makeText(mContext, "Service onDestroy", Toast.LENGTH_SHORT).show();
        unregisterBroadcastReceivers();

        Operator.getInstance(mContext).unregisterAudioCallback(mAudioDeviceCallback);
        if (mAudioCallbackHandler != null) {
            mAudioCallbackHandler.removeCallbacksAndMessages(null);
            mAudioCallbackHandler = null;
        }
        if (mAudioCallbackHandlerThread != null) {
            mAudioCallbackHandlerThread.quitSafely();
            mAudioCallbackHandlerThread = null;
        }

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
