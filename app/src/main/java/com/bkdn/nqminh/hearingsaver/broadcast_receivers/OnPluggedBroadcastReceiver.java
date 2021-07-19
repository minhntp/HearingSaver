package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;

import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

/**
 * Created by nqminh on 11/09/2018.
 */

public class OnPluggedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // AudioManager.ACTION_HEADSET_PLUG is also sent when an app register a broadcast receiver
        // that uses AudioManager.ACTION_HEADSET_PLUG
        if ((intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG))) {

            SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();
            boolean isFirstRunPlug = sharedPreferences.getBoolean(Constants.firstRunPlug, true);
            int state = intent.getIntExtra("state",0);
            int savedState = Operator.getInstance(context).getHeadsetState();

            if(isFirstRunPlug) {
                sharedPreferences.edit().putBoolean(Constants.firstRunPlug, false).apply();
            } else if (savedState != -1 && state != savedState){
                int messageType = state == 0 ? 0 : 1;
                Operator.getInstance(context).adjustOnPlugging(context, messageType);
            }
            Operator.getInstance(context).setHeadsetState(state);
        }
    }

}
