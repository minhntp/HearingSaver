package com.bkdn.nqminh.hearingsaver.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.services.MyService;

import java.util.ArrayList;
import java.util.List;

public class Operator {
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;
    private ActivityManager activityManager;

    private static Operator instance;

    static public Operator getInstance(Context context) {
        if (instance == null) {
            instance = new Operator();
            instance.sharedPreferences = context.getSharedPreferences(Constants.SETTINGS_DATA, Context.MODE_PRIVATE);
            instance.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            instance.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public void setAllVolumesExceptMedia(boolean isPlugged) {
        if (isPlugged) {
            setVolumeIfEnabled(Constants.CHECKBOX_RING_PLUGGED, AudioManager.STREAM_RING, Constants.SEEKBAR_RING_PLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_NOTIFICATION_PLUGGED, AudioManager.STREAM_NOTIFICATION, Constants.SEEKBAR_NOTIFICATION_PLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_FEEDBACK_PLUGGED, AudioManager.STREAM_SYSTEM, Constants.SEEKBAR_FEEDBACK_PLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_CALL_PLUGGED, AudioManager.STREAM_VOICE_CALL, Constants.SEEKBAR_CALL_PLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_ALARM_PLUGGED, AudioManager.STREAM_ALARM, Constants.SEEKBAR_ALARM_PLUGGED);
        } else {
            setVolumeIfEnabled(Constants.CHECKBOX_RING_UNPLUGGED, AudioManager.STREAM_RING, Constants.SEEKBAR_RING_UNPLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_NOTIFICATION_UNPLUGGED, AudioManager.STREAM_NOTIFICATION, Constants.SEEKBAR_NOTIFICATION_UNPLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_FEEDBACK_UNPLUGGED, AudioManager.STREAM_SYSTEM, Constants.SEEKBAR_FEEDBACK_UNPLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_CALL_UNPLUGGED, AudioManager.STREAM_VOICE_CALL, Constants.SEEKBAR_CALL_UNPLUGGED);
            setVolumeIfEnabled(Constants.CHECKBOX_ALARM_UNPLUGGED, AudioManager.STREAM_ALARM, Constants.SEEKBAR_ALARM_UNPLUGGED);
        }
    }

    public void setOnlyMediaVolume(boolean isPlugged) {
        if (isPlugged) {
            setVolumeIfEnabled(Constants.CHECKBOX_MEDIA_PLUGGED, AudioManager.STREAM_MUSIC, Constants.SEEKBAR_MEDIA_PLUGGED);
        } else {
            setVolumeIfEnabled(Constants.CHECKBOX_MEDIA_UNPLUGGED, AudioManager.STREAM_MUSIC, Constants.SEEKBAR_MEDIA_UNPLUGGED);
        }
    }

    public void setPending(boolean isPending) {
        sharedPreferences.edit().putBoolean(Constants.SHARED_PREFERENCE_PENDING, isPending).apply();
    }

    private final ArrayList<Integer> audioOutputDeviceTypes = new ArrayList<>() {
        {
            add(AudioDeviceInfo.TYPE_WIRED_HEADSET);
            add(AudioDeviceInfo.TYPE_WIRED_HEADPHONES);
            add(AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
        }
    };

    public int countPluggedDevices() {
        int count = 0;

        AudioDeviceInfo[] outputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);

        for (AudioDeviceInfo deviceInfo : outputDevices) {
            if (audioOutputDeviceTypes.contains(deviceInfo.getType())) {
                count++;
            }
        }
        return count;
    }

    public void handlePlugStateChange(Context context, String stateChangeTypeMessage) {
        Log.d(Constants.DEBUG_TAG, "adjusting volumes with message: " + stateChangeTypeMessage);

        // MESSAGE TYPE
        //  wire unplugged
        //  wire plugged
        //  bluetooth disconnected
        //  bluetooth connected
        //  first run: from MyService - on first time enable service OR on boot complete
        // (FOR BOTH WIRED AND BLUETOOTH AUDIO DEVICES)

        // This method will ALWAYS adjust volumes

        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            int pluggedDevices = countPluggedDevices();

            getEditor().putBoolean(Constants.SHARED_PREFERENCE_POSTPONED_PLUG_STATE, (pluggedDevices > 0)).apply();

            // Set Media volume first
            setOnlyMediaVolume(pluggedDevices > 0);

            // Then set other Volumes
            int currentRingerMode = audioManager.getRingerMode();
            boolean isSilentOrVibrate = currentRingerMode == AudioManager.RINGER_MODE_VIBRATE ||
                currentRingerMode == AudioManager.RINGER_MODE_SILENT;

            String silentModeMessage;
            setPending(isSilentOrVibrate);

            if (isSilentOrVibrate) {
                silentModeMessage = Constants.TOAST_POSTPONE;
            } else {
                setAllVolumesExceptMedia(pluggedDevices > 0);
                silentModeMessage = Constants.TOAST_VOLUME_ADJUSTED;
            }

            // Show toast
            String toastMessage = getToastOnReceivedMessage(stateChangeTypeMessage, pluggedDevices)
                + "\n" + silentModeMessage;
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
        }
    }

    private String getToastOnReceivedMessage(String message, int pluggedDevices) {
        String toast;
        switch (message) {
            case Constants.MESSAGE_WIRE_UNPLUGGED:
                toast = Constants.TOAST_JACK_DISCONNECTED;
                break;
            case Constants.MESSAGE_WIRE_PLUGGED:
                toast = Constants.TOAST_JACK_CONNECTED;
                break;
            case Constants.MESSAGE_BLUETOOTH_DISCONNECTED:
                toast = Constants.TOAST_BLUETOOTH_DISCONNECTED;
                break;
            case Constants.MESSAGE_BLUETOOTH_CONNECTED:
                toast = Constants.TOAST_BLUETOOTH_CONNECTED;
                break;
            case Constants.MESSAGE_FIRST_RUN:
                if (pluggedDevices > 1) {
                    toast = pluggedDevices + Constants.TOAST_DEVICES_CONNECTED;
                } else if (pluggedDevices == 1) {
                    toast = Constants.TOAST_ONE_DEVICE_CONNECTED;
                } else {
                    toast = Constants.TOAST_NO_DEVICES_CONNECTED;
                }
                break;
            default:
                toast = "";
        }

        return toast;
    }

    public void adjustOnRingerModeChanged(Context context, int newRingerMode) {

        boolean isPending = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_PENDING, false);

        if (isPending && (newRingerMode == AudioManager.RINGER_MODE_NORMAL)) {
            boolean isPlugged = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_POSTPONED_PLUG_STATE, false);
            setAllVolumesExceptMedia(isPlugged);
            setPending(false);

            Toast.makeText(context, Constants.VOLUME_ADJUSTED_AFTER_POSTPONED, Toast.LENGTH_LONG).show();
        }
    }

    public boolean isServiceRunning() {
        boolean isServiceRunning = false;

        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(1);

        if (!runningServiceInfoList.isEmpty()) {

            String foundServiceClassName = runningServiceInfoList.get(0).service.getClassName();
            String mainServiceClassName = MyService.class.getName();

            if (foundServiceClassName.equals(mainServiceClassName)) {
                isServiceRunning = true;
            }
        }

        return isServiceRunning;
    }

    private void setVolumeIfEnabled(String spIsEnabled, int type, String spVolume) {
        if (sharedPreferences.getBoolean(spIsEnabled, true)) {
            audioManager.setStreamVolume(type, sharedPreferences.getInt(spVolume, Constants.MAX_VOLUME), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
    }
}
