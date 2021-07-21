package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.media.AudioManager;

import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

/**
 * Created by nqminh on 11/09/2018.
 */

public class OnPluggedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // AudioManager.ACTION_HEADSET_PLUG is also sent
        // when an app register a broadcast receiver
        // that uses AudioManager.ACTION_HEADSET_PLUG

        // DECIDE whether should call Operator's handlePlugStateChange() to adjust volume or not

        if ((intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG))) {

            SharedPreferences sharedPreferences = Operator.getInstance(context).getSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            int currentPlugState = intent.getIntExtra("state", -1);

            boolean isFirstRun = sharedPreferences.getBoolean(Constants.SP_IS_FIRST_RUN, false);

            if(isFirstRun) {
                // Set volumes
                Operator.getInstance(context).handlePlugStateChange(context, Constants.MESSAGE_FIRST_RUN);
                // Set isFirstRun to false
                editor.putBoolean(Constants.SP_IS_FIRST_RUN, false);
                // Save current plug state to SharedPreferences
                editor.putInt(Constants.SP_PREVIOUS_PLUG_STATE, currentPlugState);
            } else {
                // if current plug state != shared preference plug state:
                int previousPlugState = sharedPreferences.getInt(Constants.SP_PREVIOUS_PLUG_STATE, -1);
                if(currentPlugState != previousPlugState) {
                    String message;
                    if(currentPlugState == 1) {
                        message = Constants.MESSAGE_WIRE_PLUGGED;
                    } else {
                        message = Constants.MESSAGE_WIRE_UNPLUGGED;
                    }
                    //   set volumes
                    Operator.getInstance(context).handlePlugStateChange(context, message);
                    //   save current plug state to shared preferences
                    editor.putInt(Constants.SP_PREVIOUS_PLUG_STATE, currentPlugState);
                }
            }

            editor.apply();

//            int state = intent.getIntExtra("state",-1);
//            int savedState = Operator.getInstance(context).getHeadsetState();
//
//            if(isFirstRun) {
//                sharedPreferences.edit().putBoolean(Constants.SP_FIRST_RUN_1, false).apply();
//            } else if (savedState != -1 && state != savedState){
//                int messageType = state == 0 ? 0 : 1;
//                Operator.getInstance(context).handlePlugStateChange(context, messageType);
//            }
//            Operator.getInstance(context).setHeadsetState(state);
        }
    }

}
