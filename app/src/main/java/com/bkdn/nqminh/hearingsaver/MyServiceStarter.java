package com.bkdn.nqminh.hearingsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class MyServiceStarter extends BroadcastReceiver {
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {
        settings = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (!settings.getBoolean("disable", true)) {
//            context.startService(new Intent(context, MyService.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MyService.class));
            } else {
                context.startService(new Intent(context, MyService.class));
            }
        } else {
        }
    }
}
