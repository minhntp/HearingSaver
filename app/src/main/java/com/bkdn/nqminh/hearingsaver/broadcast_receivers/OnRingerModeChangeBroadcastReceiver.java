package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class OnRingerModeChangeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {

            // silent: 0, vibrate: 1, normal: 2
            int newRingerMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1);

            Operator.getInstance(context).adjustOnRingerModeChanged(context, newRingerMode);
        }
    }
}
