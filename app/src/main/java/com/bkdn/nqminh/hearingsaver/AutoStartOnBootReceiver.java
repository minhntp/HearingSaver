package com.bkdn.nqminh.hearingsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class AutoStartOnBootReceiver extends BroadcastReceiver {

    static SharedPreferences settings;

    @Override
    public void onReceive(Context context, Intent intent) {
        settings = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (!settings.getBoolean("disable", true)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, MyService.class));
                } else {
                    context.startService(new Intent(context, MyService.class));
                }
            }
        }
    }
}
