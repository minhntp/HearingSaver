package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.utils.Operator;
import com.bkdn.nqminh.hearingsaver.utils.Constants;

/**
 * Created by nqminh on 11/09/2018.
 */

public class OnPluggedBroadcastReceiver extends BroadcastReceiver {

    public static boolean isPlugged = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
            int pluggedState = intent.getIntExtra("state", -1);
            isPlugged = (pluggedState != 0);
            boolean isSilentOrVibrate = (Operator.getInstance().getAudioManager().getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) ||
                    (Operator.getInstance().getAudioManager().getRingerMode() == AudioManager.RINGER_MODE_SILENT);
            Log.d("debug", "on plugged broadcast received: isSilentOrVibrate = " + isSilentOrVibrate);

            Operator.getInstance().setPending(isSilentOrVibrate, isPlugged);
            Operator.getInstance().setMediaVolume(isPlugged);

            if (isSilentOrVibrate) {
                Toast.makeText(context, Constants.toastPostpone, Toast.LENGTH_SHORT).show();
            } else {
                Operator.getInstance().setVolumeExceptMedia(isPlugged);
                Toast.makeText(context, Constants.toastVolumeAdjusted, Toast.LENGTH_SHORT).show();
            }
        }
    }


}
