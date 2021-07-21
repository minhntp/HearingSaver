package com.bkdn.nqminh.hearingsaver.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.activities.MainActivity;
import com.bkdn.nqminh.hearingsaver.services.MyService;

import java.util.ArrayList;
import java.util.List;

public class Operator {
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;
    private ActivityManager activityManager;

    private int headsetState = -1;

    private static Operator instance;

    static public Operator getInstance(Context context) {
        if (instance == null) {
            instance = new Operator();
//            instance.sharedPreferences = HearingSaver.getAppContext().getSharedPreferences(Constants.settingsData, Context.MODE_PRIVATE);
            instance.sharedPreferences = context.getSharedPreferences(Constants.SETTINGS_DATA, Context.MODE_PRIVATE);

//            instance.audioManager = (AudioManager) HearingSaver.getAppContext().getSystemService(Context.AUDIO_SERVICE);
            instance.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            instance.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

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
            if (sharedPreferences.getBoolean(Constants.CHECK_1, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_RING,
                        sharedPreferences.getInt(Constants.VOLUME_1, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.CHECK_3, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION,
                        sharedPreferences.getInt(Constants.VOLUME_3, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.CHECK_5, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_SYSTEM,
                        sharedPreferences.getInt(Constants.VOLUME_5, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        } else {
            if (sharedPreferences.getBoolean(Constants.CHECK_2, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_RING,
                        sharedPreferences.getInt(Constants.VOLUME_2, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.CHECK_4, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION,
                        sharedPreferences.getInt(Constants.VOLUME_4, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
            if (sharedPreferences.getBoolean(Constants.CHECK_6, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_SYSTEM,
                        sharedPreferences.getInt(Constants.VOLUME_6, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
    }

    public void setOnlyMediaVolume(boolean isPlugged) {
        if (isPlugged) {
            if (sharedPreferences.getBoolean(Constants.CHECK_7, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        sharedPreferences.getInt(Constants.VOLUME_7, 100),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        } else {
            if (sharedPreferences.getBoolean(Constants.CHECK_8, true)) {
                audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        sharedPreferences.getInt(Constants.VOLUME_8, 100),
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
        editor.putBoolean(Constants.SETTINGS_PENDING, isPending);
        editor.apply();
    }

    private final ArrayList<Integer> audioOutputDeviceTypes = new ArrayList<Integer>() {
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

    public void adjustOnPlugStateChanged(Context context, int messageType) {
        // MESSAGE TYPE
        //  0: wire unplugged
        //  1: wire plugged
        //  2: bluetooth disconnected
        //  3: bluetooth connected
        //  4: from MyService - first run from enabling service or on boot complete
        // (FOR BOTH WIRED AND BLUETOOTH AUDIO DEVICES)

        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            int pluggedDevices = countPluggedDevices();

            getEditor().putFloat(Constants.IS_PLUGGED_IN, pluggedDevices);

            switch (messageType) {
                case 0:
                    Toast.makeText(context, Constants.TOAST_JACK_DISCONNECTED, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, Constants.TOAST_JACK_CONNECTED, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, Constants.TOAST_BLUETOOTH_DISCONNECTED, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, Constants.TOAST_BLUETOOTH_CONNECTED, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    if (pluggedDevices > 1) {
                        Toast.makeText(context,
                                pluggedDevices + Constants.TOAST_DEVICES_CONNECTED
                                , Toast.LENGTH_SHORT).show();
                    } else if (pluggedDevices == 1) {
                        Toast.makeText(context, Constants.TOAST_ONE_DEVICE_CONNECTED, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, Constants.TOAST_NO_DEVICES_CONNECTED, Toast.LENGTH_SHORT).show();
                    }
            }

            setOnlyMediaVolume(pluggedDevices > 0);

            // Set Other Volumes
            int currentRingerMode = audioManager.getRingerMode();
            boolean isSilentOrVibrate = currentRingerMode == AudioManager.RINGER_MODE_VIBRATE ||
                    currentRingerMode == AudioManager.RINGER_MODE_SILENT;

            setPending(isSilentOrVibrate);

            if (isSilentOrVibrate) {
                Toast.makeText(context, Constants.TOAST_POSTPONE, Toast.LENGTH_LONG).show();
            } else {
                setVolumeExceptMedia(pluggedDevices > 0);
                Toast.makeText(context, Constants.TOAST_VOLUME_ADJUSTED, Toast.LENGTH_SHORT).show();
            }
        }
//        }
    }

    public void adjustOnRingerModeChanged(Context context) {
        int currentRingerMode = Operator.getInstance(context).getAudioManager().getRingerMode();
        boolean isSilentOrVibrate = currentRingerMode == AudioManager.RINGER_MODE_VIBRATE ||
                currentRingerMode == AudioManager.RINGER_MODE_SILENT;

//            boolean isPlugged = Operator.getInstance(context).getAudioManager().isWiredHeadsetOn();
        boolean isPlugged = sharedPreferences.getBoolean(Constants.IS_PLUGGED_IN, false);

        boolean isPending = sharedPreferences.getBoolean(Constants.SETTINGS_PENDING, false);

        if (isPending && !isSilentOrVibrate) {
            setVolumeExceptMedia(isPlugged);
            setPending(false);
            Toast.makeText(context, Constants.VOLUME_ADJUSTED_AFTER_POSTPONED, Toast.LENGTH_SHORT).show();
        }
    }

    public Intent getRunningIntent() {

        Intent runningIntent = null;

        // Get the root activity of the task that your activity is running in
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);

        if (tasks.size() > 0) {
            ComponentName foundActivity = tasks.get(0).baseActivity;

            String classNameOfFoundTask = foundActivity.getClassName();
            String classNameOfMainActivity = MainActivity.class.getName();

            if (classNameOfFoundTask.equals(classNameOfMainActivity)) {
                runningIntent = new Intent();
                runningIntent.setComponent(foundActivity);
                // Set the action and category so it appears that the app is being launched
                runningIntent.setAction(Intent.ACTION_MAIN);
                runningIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            }
        }

        return runningIntent;
    }

    public boolean isServiceRunning() {
        boolean isServiceRunning = false;

        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(1);

        if ((runningServiceInfoList.size() > 0)) {

            String foundServiceClassName = runningServiceInfoList.get(0).service.getClassName();
            String mainServiceClassName = MyService.class.getName();

            if (foundServiceClassName.equals(mainServiceClassName)) {
                isServiceRunning = true;
            }
        }

        return isServiceRunning;
    }
}
