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
    static SharedPreferences settings;

    @Override
    public void onReceive(Context context, Intent intent) {

        settings = context.getSharedPreferences(Constants.SETTINGS_DATA, Context.MODE_PRIVATE);
        boolean isServiceEnabled = settings.getBoolean(Constants.SP_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            Log.d(Constants.DEBUG_TAG, "start service from OnDestroyBroadcastReceiver");

            Intent myServiceIntent = new Intent(context, MyService.class);
            myServiceIntent.putExtra(Constants.INTENT_RESTART_SERVICE, true);
            context.startForegroundService(myServiceIntent);
        }
    }
}
