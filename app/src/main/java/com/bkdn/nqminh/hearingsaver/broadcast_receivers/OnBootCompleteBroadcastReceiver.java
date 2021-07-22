package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.services.MyService;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class OnBootCompleteBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            // If service is enabled:
            //   acts like first time of enabling the service:
            //   Set shared preferences: IS_FIRST_RUN to true
            //   Then start service
            //   Let service and other broadcast receivers handle everything
            // If service is disabled:
            //   do nothing

            SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();

            boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SP_IS_SERVICE_ENABLED, false);

            if(isServiceEnabled) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constants.SP_IS_FIRST_RUN, true);
                editor.putBoolean(Constants.SP_ADJUSTED_ON_FIRST_RUN, false);
                editor.apply();

                Intent myServiceIntent = new Intent(context, MyService.class);
                context.startForegroundService(myServiceIntent);

                Toast.makeText(context, Constants.TOAST_ON_BOOT_COMPLETED, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
