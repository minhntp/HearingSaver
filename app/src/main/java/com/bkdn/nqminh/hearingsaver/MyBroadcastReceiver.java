package com.bkdn.nqminh.hearingsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nqminh on 11/09/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    AudioManager audioManager;
    static SharedPreferences settings;
    Context mContext;

    boolean silent;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        settings = context.getSharedPreferences("data", Context.MODE_PRIVATE);

        if((audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) ||
                (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT)) {
            silent = true;
        } else {
            silent = false;
        }
        readStateAndChangeVolumes(intent);
    }

    private void readStateAndChangeVolumes(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case (0):
                    if(!silent) {
                        Log.d("TAG", "Headset unplugged!");
                        if (settings.getBoolean("check2", true)) {
                            audioManager.setStreamVolume(
                                    AudioManager.STREAM_RING,
                                    settings.getInt("volume2", 100),
                                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        }
                        if (settings.getBoolean("check4", true)) {
                            audioManager.setStreamVolume(
                                    AudioManager.STREAM_NOTIFICATION,
                                    settings.getInt("volume4", 100),
                                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        }
                        if (settings.getBoolean("check6", true)) {
                            audioManager.setStreamVolume(
                                    AudioManager.STREAM_SYSTEM,
                                    settings.getInt("volume6", 100),
                                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        }
                    }
                    if (settings.getBoolean("check8", true)) {
                        audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                settings.getInt("volume8", 100),
                                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    }
                    break;
                case (1):
                    if(!silent) {
                        Log.d("TAG", "Headset plugged!");
                        if (settings.getBoolean("check1", true)) {
                            audioManager.setStreamVolume(
                                    AudioManager.STREAM_RING,
                                    settings.getInt("volume1", 100),
                                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        }
                        if (settings.getBoolean("check3", true)) {
                            audioManager.setStreamVolume(
                                    AudioManager.STREAM_NOTIFICATION,
                                    settings.getInt("volume3", 100),
                                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        }
                        if (settings.getBoolean("check5", true)) {
                            audioManager.setStreamVolume(
                                    AudioManager.STREAM_SYSTEM,
                                    settings.getInt("volume5", 100),
                                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                        }
                    }
                    if (settings.getBoolean("check7", true)) {
                        audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                settings.getInt("volume7", 100),
                                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    }
                    break;
                default:
                    Log.d("TAG", "Error!");
            }
        }
        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
    }

}
