package com.bkdn.nqminh.hearingsaver.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.widget.Toast;

public class Operator {
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;

    private int headsetState = -1;

    private static Operator instance;

    static public Operator getInstance(Context context) {
        if (instance == null) {
            instance = new Operator();
//            instance.sharedPreferences = HearingSaver.getAppContext().getSharedPreferences(Constants.settingsData, Context.MODE_PRIVATE);
            instance.sharedPreferences = context.getSharedPreferences(Constants.settingsData, Context.MODE_PRIVATE);

//            instance.audioManager = (AudioManager) HearingSaver.getAppContext().getSystemService(Context.AUDIO_SERVICE);
            instance.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        return instance;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public int getHeadsetState() {
        return headsetState;
    }

    public void setHeadsetState(int headsetState) {
        this.headsetState = headsetState;
    }

    public void setVolumeExceptMedia(boolean isPlugged) {
        if (isPlugged) {
            if (sharedPreferences.getBoolean(Constants.check1, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_RING,
                        sharedPreferences.getInt(Constants.volume1, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check3, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION,
                        sharedPreferences.getInt(Constants.volume3, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check5, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_SYSTEM,
                        sharedPreferences.getInt(Constants.volume5, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        } else {
            if (sharedPreferences.getBoolean(Constants.check2, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_RING,
                        sharedPreferences.getInt(Constants.volume2, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check4, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION,
                        sharedPreferences.getInt(Constants.volume4, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.check6, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_SYSTEM,
                        sharedPreferences.getInt(Constants.volume6, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
    }

    public void setMediaVolume(boolean isPlugged) {
        if (isPlugged) {
            if (sharedPreferences.getBoolean(Constants.check7, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        sharedPreferences.getInt(Constants.volume7, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        } else {
            if (sharedPreferences.getBoolean(Constants.check8, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        sharedPreferences.getInt(Constants.volume8, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
    }

    //    public void setPending(boolean isPending, boolean isPlugged) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(Constants.settingsPending, isPending);
//        editor.putBoolean(Constants.settingsPendingIsPlugged, isPlugged);
//        editor.apply();
//    }
    public void setPending(boolean isPending) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.settingsPending, isPending);
        editor.apply();
    }

    public boolean isPluggedIn() {
        for (AudioDeviceInfo deviceInfo : audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)) {
            switch (deviceInfo.getType()) {
                case AudioDeviceInfo.TYPE_WIRED_HEADSET:
                case AudioDeviceInfo.TYPE_WIRED_HEADPHONES:
                case AudioDeviceInfo.TYPE_BLUETOOTH_SCO:
                    return true;
            }
        }
        return false;
    }

    // message type:
    //  0: wire unplugged
    //  1: wire plugged
    //  2: bluetooth disconnected
    //  3: bluetooth connected
    //  4: from MyService - first run from enabling service or on boot complete
    public void adjustOnPlugging(Context context, int messageType) {
        // (FOR BOTH WIRED AND BLUETOOTH AUDIO DEVICES)

        boolean isDisabled = sharedPreferences.getBoolean(Constants.isDisabled, false);

        if (!isDisabled) {
            // Set Media Volume
                /*
                boolean isPluggedIn = isPluggedIn();

                getEditor().putBoolean(Constants.isPluggedIn, isPluggedIn);

                if (isPluggedIn) {
                    Toast.makeText(context, Constants.plugged, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, Constants.unplugged, Toast.LENGTH_SHORT).show();
                }
                */
            boolean isPluggedIn = isPluggedIn();

            getEditor().putBoolean(Constants.isPluggedIn, isPluggedIn);

            switch (messageType) {
                case 0:
                    Toast.makeText(context, Constants.message0, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, Constants.message1, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, Constants.message2, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, Constants.message3, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    if (isPluggedIn) {
                        Toast.makeText(context, Constants.message41, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, Constants.message42, Toast.LENGTH_SHORT).show();
                    }
            }

            setMediaVolume(isPluggedIn);

            // Set Other Volumes
            int currentRingerMode = audioManager.getRingerMode();
            boolean isSilentOrVibrate = currentRingerMode == AudioManager.RINGER_MODE_VIBRATE ||
                    currentRingerMode == AudioManager.RINGER_MODE_SILENT;

            setPending(isSilentOrVibrate);

            if (isSilentOrVibrate) {
                Toast.makeText(context, Constants.toastPostpone, Toast.LENGTH_LONG).show();
            } else {
                setVolumeExceptMedia(isPluggedIn);
                Toast.makeText(context, Constants.toastVolumeAdjusted, Toast.LENGTH_SHORT).show();
            }
        }
//        }
    }

    public void adjustOnRingerModeChanged(Context context) {
        int currentRingerMode = Operator.getInstance(context).getAudioManager().getRingerMode();
        boolean isSilentOrVibrate = currentRingerMode == AudioManager.RINGER_MODE_VIBRATE ||
                currentRingerMode == AudioManager.RINGER_MODE_SILENT;

//            boolean isPlugged = Operator.getInstance(context).getAudioManager().isWiredHeadsetOn();
        boolean isPlugged = sharedPreferences.getBoolean(Constants.isPluggedIn, false);

        boolean isPending = sharedPreferences.getBoolean(Constants.settingsPending, false);

        if (isPending && !isSilentOrVibrate) {
            setVolumeExceptMedia(isPlugged);
            setPending(false);
            Toast.makeText(context, Constants.toastVolumeAdjustedPostpone, Toast.LENGTH_SHORT).show();
        }
    }
}
