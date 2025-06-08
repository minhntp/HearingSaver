package com.bkdn.nqminh.hearingsaver.utils;

import static android.media.AudioDeviceInfo.TYPE_AUX_LINE;
import static android.media.AudioDeviceInfo.TYPE_BLE_HEADSET;
import static android.media.AudioDeviceInfo.TYPE_BLE_SPEAKER;
import static android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP;
import static android.media.AudioDeviceInfo.TYPE_BLUETOOTH_SCO;
import static android.media.AudioDeviceInfo.TYPE_HDMI;
import static android.media.AudioDeviceInfo.TYPE_HEARING_AID;
import static android.media.AudioDeviceInfo.TYPE_LINE_ANALOG;
import static android.media.AudioDeviceInfo.TYPE_LINE_DIGITAL;
import static android.media.AudioDeviceInfo.TYPE_USB_ACCESSORY;
import static android.media.AudioDeviceInfo.TYPE_USB_DEVICE;
import static android.media.AudioDeviceInfo.TYPE_USB_HEADSET;
import static android.media.AudioDeviceInfo.TYPE_WIRED_HEADPHONES;
import static android.media.AudioDeviceInfo.TYPE_WIRED_HEADSET;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.services.MyService;

import java.util.List;
import java.util.Map;

public class Operator {
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;
    private ActivityManager activityManager;

    private final static Map<Integer, String> deviceTypes;

    static {
        deviceTypes = Map.ofEntries(
            Map.entry(TYPE_AUX_LINE, "AUX line"),
            Map.entry(TYPE_BLE_HEADSET, "BLE headset"),
            Map.entry(TYPE_BLE_SPEAKER, "BLE speaker"),
            Map.entry(TYPE_BLUETOOTH_SCO, "Bluetooth SCO"),
            Map.entry(TYPE_BLUETOOTH_A2DP, "Bluetooth A2DP"),
            Map.entry(TYPE_HDMI, "HDMI"),
            Map.entry(TYPE_HEARING_AID, "Hearing aid"),
            Map.entry(TYPE_LINE_ANALOG, "Line analog"),
            Map.entry(TYPE_LINE_DIGITAL, "Line digital"),
            Map.entry(TYPE_USB_ACCESSORY, "USB accessory"),
            Map.entry(TYPE_USB_DEVICE, "USB device"),
            Map.entry(TYPE_USB_HEADSET, "USB headset"),
            Map.entry(TYPE_WIRED_HEADPHONES, "Wired headphones"),
            Map.entry(TYPE_WIRED_HEADSET, "Wired headset")
        );
    }

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
            setOneVolume(Constants.CHECKBOX_RING_PLUGGED, AudioManager.STREAM_RING, Constants.SEEKBAR_RING_PLUGGED);
            setOneVolume(Constants.CHECKBOX_NOTIFICATION_PLUGGED, AudioManager.STREAM_NOTIFICATION, Constants.SEEKBAR_NOTIFICATION_PLUGGED);
            setOneVolume(Constants.CHECKBOX_FEEDBACK_PLUGGED, AudioManager.STREAM_SYSTEM, Constants.SEEKBAR_FEEDBACK_PLUGGED);
            setOneVolume(Constants.CHECKBOX_CALL_PLUGGED, AudioManager.STREAM_VOICE_CALL, Constants.SEEKBAR_CALL_PLUGGED);
        } else {
            setOneVolume(Constants.CHECKBOX_RING_UNPLUGGED, AudioManager.STREAM_RING, Constants.SEEKBAR_RING_UNPLUGGED);
            setOneVolume(Constants.CHECKBOX_NOTIFICATION_UNPLUGGED, AudioManager.STREAM_NOTIFICATION, Constants.SEEKBAR_NOTIFICATION_UNPLUGGED);
            setOneVolume(Constants.CHECKBOX_FEEDBACK_UNPLUGGED, AudioManager.STREAM_SYSTEM, Constants.SEEKBAR_FEEDBACK_UNPLUGGED);
            setOneVolume(Constants.CHECKBOX_CALL_UNPLUGGED, AudioManager.STREAM_VOICE_CALL, Constants.SEEKBAR_CALL_UNPLUGGED);
        }
    }

    public void setSilentVolumes(boolean isPlugged) {
        if (isPlugged) {
            setOneVolume(Constants.CHECKBOX_MEDIA_PLUGGED, AudioManager.STREAM_MUSIC, Constants.SEEKBAR_MEDIA_PLUGGED);
            setOneVolume(Constants.CHECKBOX_ALARM_PLUGGED, AudioManager.STREAM_ALARM, Constants.SEEKBAR_ALARM_PLUGGED);
        } else {
            setOneVolume(Constants.CHECKBOX_MEDIA_UNPLUGGED, AudioManager.STREAM_MUSIC, Constants.SEEKBAR_MEDIA_UNPLUGGED);
            setOneVolume(Constants.CHECKBOX_ALARM_UNPLUGGED, AudioManager.STREAM_ALARM, Constants.SEEKBAR_ALARM_UNPLUGGED);
        }
    }

    public void setPending(boolean isPending) {
        sharedPreferences.edit().putBoolean(Constants.SHARED_PREFERENCE_PENDING, isPending).apply();
    }

    public void handlePlugStateChange(Context context) {
        int connectedType = isOutputDeviceConnected(audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS));
        boolean isOutputConnected = (connectedType >= 0);
        String connectedToast = isOutputConnected ?
            "Connected device: " + deviceTypes.get(connectedType) :
            "No output device connected";

        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            setSilentVolumes(isOutputConnected);

            // Set other Volumes
            int currentRingerMode = audioManager.getRingerMode();
            boolean isSilentOrVibrate = currentRingerMode == AudioManager.RINGER_MODE_VIBRATE || currentRingerMode == AudioManager.RINGER_MODE_SILENT;

            StringBuilder silentModeMessage = new StringBuilder(connectedToast).append('\n');
            setPending(isSilentOrVibrate);

            if (isSilentOrVibrate) {
                silentModeMessage.append(Constants.TOAST_POSTPONE);
            } else {
                setAllVolumesExceptMedia(isOutputConnected);
                silentModeMessage.append(Constants.TOAST_VOLUME_ADJUSTED);
            }

            Toast.makeText(context, silentModeMessage, Toast.LENGTH_LONG).show();
        }
    }

    public void adjustOnRingerModeChanged(Context context, int newRingerMode) {
        boolean isPending = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_PENDING, false);

        if (isPending && (newRingerMode == AudioManager.RINGER_MODE_NORMAL)) {
            int connectedType = isOutputDeviceConnected(audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS));
            boolean isOutputConnected = (connectedType >= 0);
            setAllVolumesExceptMedia(isOutputConnected);
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

    public void registerAudioCallback(AudioDeviceCallback callback, Handler handler) {
        audioManager.registerAudioDeviceCallback(callback, handler);
    }

    public void unregisterAudioCallback(AudioDeviceCallback callback) {
        audioManager.unregisterAudioDeviceCallback(callback);
    }

    private void setOneVolume(String spIsEnabled, int type, String spVolume) {
        if (sharedPreferences.getBoolean(spIsEnabled, true)) {
            audioManager.setStreamVolume(type, sharedPreferences.getInt(spVolume, Constants.MAX_VOLUME), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
    }

    private int isOutputDeviceConnected(AudioDeviceInfo[] devices) {
        for (AudioDeviceInfo device : devices) {
            if (device.isSink()) {
                int type = device.getType();
                if (deviceTypes.containsKey(type)) {
                    return type;
                }
            }
        }
        return -1;
    }
}
