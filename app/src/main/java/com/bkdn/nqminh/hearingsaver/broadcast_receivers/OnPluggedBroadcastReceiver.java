package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.utils.Operator;
import com.bkdn.nqminh.hearingsaver.utils.Constants;

/**
 * Created by nqminh on 11/09/2018.
 */

public class OnPluggedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ((intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG))) {

            SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();
            boolean isFirstRunPlug = sharedPreferences.getBoolean(Constants.firstRunPlug, true);

            if(isFirstRunPlug) {
                sharedPreferences.edit().putBoolean(Constants.firstRunPlug, false).apply();
            } else {
                int state = intent.getIntExtra("state",0);
                int messageType = state == 0 ? 0 : 1;
                Operator.getInstance(context).adjustOnPlugging(context, messageType);
            }
        }
    }

}
