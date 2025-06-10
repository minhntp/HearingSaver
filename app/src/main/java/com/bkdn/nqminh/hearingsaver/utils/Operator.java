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
    private static final Map<Integer, String> DEVICE_TYPES;
    private static Operator instance;
    private final SharedPreferences mSharedPreferences;
    private final AudioManager mAudioManager;
    private final ActivityManager mActivityManager;
    private String lastDeviceAddress = "";
    private long lastStateChangeUnplugged = 0;

    static {
        DEVICE_TYPES = Map.ofEntries(
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

    private Operator(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.SETTINGS_DATA, Context.MODE_PRIVATE);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    static public Operator getInstance(Context context) {
        if (instance == null) {
            instance = new Operator(context);
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
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

    public void handlePlugStateChange(Context context) {
        if (!isServiceEnabled()) {
            return;
        }

        AudioDeviceInfo connectedDevice = getConnectedDevice(mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS));
        boolean isOutputConnected;

        if (connectedDevice != null) {
            if (lastDeviceAddress.equals(connectedDevice.getAddress())) {
                return;
            }
            lastDeviceAddress = connectedDevice.getAddress();
            isOutputConnected = true;
        } else {
            lastDeviceAddress = "";

            long now = System.currentTimeMillis();
            if ((now - lastStateChangeUnplugged) < 1000L) {
                return;
            }

            lastStateChangeUnplugged = now;
            isOutputConnected = false;
        }

        String connectedToast = isOutputConnected ?
            "Connected device: " + DEVICE_TYPES.get(connectedDevice.getType()) :
            "No output device connected";

        setSilentVolumes(isOutputConnected);

        // Set other Volumes
        int currentRingerMode = mAudioManager.getRingerMode();
        boolean isSilentOrVibrate = (currentRingerMode == AudioManager.RINGER_MODE_VIBRATE) ||
            (currentRingerMode == AudioManager.RINGER_MODE_SILENT);

        StringBuilder silentModeMessage = new StringBuilder(connectedToast).append('\n');
        setPending(isSilentOrVibrate);

        if (isSilentOrVibrate) {
            silentModeMessage.append(Constants.TOAST_POSTPONE);
        } else {
            setAllVolumesExceptMedia(isOutputConnected);
            silentModeMessage.append(Constants.TOAST_VOLUME_ADJUSTED);
        }

        Toast.makeText(context, silentModeMessage, Toast.LENGTH_SHORT).show();
    }

    public void adjustOnRingerModeChanged(Context context, int newRingerMode) {
        boolean isPending = getPending();

        if (isPending && (newRingerMode == AudioManager.RINGER_MODE_NORMAL)) {
            AudioDeviceInfo connectedDevice = getConnectedDevice(mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS));
            boolean isOutputConnected = (connectedDevice != null);
            setAllVolumesExceptMedia(isOutputConnected);
            setPending(false);

            Toast.makeText(context, Constants.VOLUME_ADJUSTED_AFTER_POSTPONED, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isServiceRunning() {
        boolean isServiceRunning = false;

        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = mActivityManager.getRunningServices(1);

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
        mAudioManager.registerAudioDeviceCallback(callback, handler);
    }

    public void unregisterAudioCallback(AudioDeviceCallback callback) {
        mAudioManager.unregisterAudioDeviceCallback(callback);
    }

    private void setOneVolume(String spIsEnabled, int type, String spVolume) {
        if (mSharedPreferences.getBoolean(spIsEnabled, true)) {
            mAudioManager.setStreamVolume(type, mSharedPreferences.getInt(spVolume, Constants.MAX_VOLUME), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
    }

    private boolean getPending() {
        return mSharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_PENDING, false);
    }

    private void setPending(boolean isPending) {
        mSharedPreferences.edit().putBoolean(Constants.SHARED_PREFERENCE_PENDING, isPending).apply();
    }

    private boolean isServiceEnabled() {
        return mSharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);
    }

    private AudioDeviceInfo getConnectedDevice(AudioDeviceInfo[] devices) {
        for (AudioDeviceInfo device : devices) {
            if (device.isSink()) {
                int type = device.getType();
                if (DEVICE_TYPES.containsKey(type)) {
                    return device;
                }
            }
        }
        return null;
    }
}
