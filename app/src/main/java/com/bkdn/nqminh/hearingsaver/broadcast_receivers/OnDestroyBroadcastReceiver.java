package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.services.MyService;
import com.bkdn.nqminh.hearingsaver.utils.Constants;

public class OnDestroyBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.bkdn.nqminh.hearingsaver.ON_SERVICE_DESTROYED";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences(Constants.SETTINGS_DATA, Context.MODE_PRIVATE);
        boolean isServiceEnabled = settings.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            Log.d(Constants.DEBUG_TAG, "start service from OnDestroyBroadcastReceiver");
            Toast.makeText(context, "Service is killed by System. Restarting...", Toast.LENGTH_SHORT).show();

            // Build and start service along with show notification
            Intent myServiceIntent = new Intent(context, MyService.class);
            myServiceIntent.setAction(ACTION);
            context.startForegroundService(myServiceIntent);
        }
    }
}
