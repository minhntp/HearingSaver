package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.bkdn.nqminh.hearingsaver.services.MyService;
import com.bkdn.nqminh.hearingsaver.utils.Constants;

public class OnDestroyBroadcastReceiver extends BroadcastReceiver {
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {
        settings = context.getSharedPreferences(Constants.settingsData, Context.MODE_PRIVATE);
        if (!settings.getBoolean(Constants.settingsDisabled, false)) {
            Intent myServiceIntent = new Intent(context, MyService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(myServiceIntent);
            } else {
                context.startService(myServiceIntent);
            }
        }
    }
}
