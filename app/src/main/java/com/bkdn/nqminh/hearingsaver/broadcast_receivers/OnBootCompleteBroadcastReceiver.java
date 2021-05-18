package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.services.MyService;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class OnBootCompleteBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();

            boolean isDisabled = sharedPreferences.getBoolean(Constants.isDisabled, false);

            if (!isDisabled) {
                Toast.makeText(context, Constants.on_boot, Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constants.firstRunMyService, true);
                editor.putBoolean(Constants.firstRunPlug, true);
                editor.apply();

                Intent myServiceIntent = new Intent(context, MyService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(myServiceIntent);
                } else {
                    context.startService(myServiceIntent);
                }
            }
        }
    }
}
