package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.services.MyService;
import com.bkdn.nqminh.hearingsaver.utils.Constants;

public class OnDestroyBroadcastReceiver extends BroadcastReceiver {
    static SharedPreferences settings;

    @Override
    public void onReceive(Context context, Intent intent) {

        settings = context.getSharedPreferences(Constants.SETTINGS_DATA, Context.MODE_PRIVATE);
        boolean isServiceEnabled = settings.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            Log.d(Constants.DEBUG_TAG, "start service from OnDestroyBroadcastReceiver");

            // Build and start service along with showing notification
            Intent myServiceIntent = new Intent(context, MyService.class);
            myServiceIntent.putExtra(Constants.INTENT_RESTART_SERVICE, true);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myServiceIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new Notification.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getText(R.string.notification_title))
                .setContentText(context.getText(R.string.notification_message))
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .build();

            ((Service) context).startForeground(Constants.NOTIFICATION_ID, notification);
        }
    }
}
