package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class OnRingerModeChangedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
            Operator.getInstance(context).adjustOnRingerModeChanged(context);
        }
    }
}
