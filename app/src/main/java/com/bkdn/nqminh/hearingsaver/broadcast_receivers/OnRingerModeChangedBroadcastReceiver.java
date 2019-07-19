package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class OnRingerModeChangedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("debug", "ringer mode changed: intent.getAction() = " + intent.getAction());
        if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction())) {
            boolean isSilentOrVibrate = (Operator.getInstance().getAudioManager().getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) ||
                    (Operator.getInstance().getAudioManager().getRingerMode() == AudioManager.RINGER_MODE_SILENT);
            boolean isPendingPlugged = Operator.getInstance().getSharedPreferences().getBoolean(Constants.settingsPendingIsPlugged, true);
            boolean isPending = Operator.getInstance().getSharedPreferences().getBoolean(Constants.settingsPending, false);
            Log.d("debug", "received ringer mode changed: isSilent = " + isSilentOrVibrate + ", is pending = " + isPending +
                    ", is plugged = " + isPendingPlugged);
            if (isPending && !isSilentOrVibrate) {
                Operator.getInstance().setVolumeExceptMedia(isPendingPlugged);
                Operator.getInstance().setPending(false, isPending);
                Toast.makeText(context, Constants.toastVolumeAdjustedPostpone, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
