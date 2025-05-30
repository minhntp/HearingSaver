package com.bkdn.nqminh.hearingsaver.utils;

import static android.media.AudioDeviceInfo.TYPE_AUX_LINE;
import static android.media.AudioDeviceInfo.TYPE_BLE_HEADSET;
import static android.media.AudioDeviceInfo.TYPE_BLE_SPEAKER;
import static android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP;
import static android.media.AudioDeviceInfo.TYPE_BLUETOOTH_SCO;
import static android.media.AudioDeviceInfo.TYPE_BUILTIN_EARPIECE;
import static android.media.AudioDeviceInfo.TYPE_BUILTIN_MIC;
import static android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER;
import static android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER_SAFE;
import static android.media.AudioDeviceInfo.TYPE_HDMI;
import static android.media.AudioDeviceInfo.TYPE_HEARING_AID;
import static android.media.AudioDeviceInfo.TYPE_LINE_ANALOG;
import static android.media.AudioDeviceInfo.TYPE_LINE_DIGITAL;
import static android.media.AudioDeviceInfo.TYPE_TELEPHONY;
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
import android.os.Looper;
import android.widget.Toast;

import com.bkdn.nqminh.hearingsaver.services.MyService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Operator {
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;
    private ActivityManager activityManager;

    private final Set<Integer> builtInSpeakerTypes = Set.of(TYPE_BUILTIN_EARPIECE, TYPE_BUILTIN_SPEAKER, TYPE_BUILTIN_SPEAKER_SAFE, TYPE_BUILTIN_MIC, TYPE_TELEPHONY);

    private final static Map<Integer, String> deviceTypes;

    static {
        deviceTypes = Map.ofEntries(
            Map.entry(TYPE_WIRED_HEADSET, "Wired headset"),
            Map.entry(TYPE_WIRED_HEADPHONES, "Wired headphones"),
            Map.entry(TYPE_LINE_ANALOG, "Line analog"),
            Map.entry(TYPE_LINE_DIGITAL, "Line digital"),
            Map.entry(TYPE_BLUETOOTH_SCO, "Bluetooth SCO"),
            Map.entry(TYPE_BLUETOOTH_A2DP, "Bluetooth A2DP"),
            Map.entry(TYPE_HDMI, "HDMI"),
            Map.entry(TYPE_USB_DEVICE, "USB device"),
            Map.entry(TYPE_USB_ACCESSORY, "USB accessory"),
            Map.entry(TYPE_AUX_LINE, "AUX line"),
            Map.entry(TYPE_USB_HEADSET, "USB headset"),
            Map.entry(TYPE_HEARING_AID, "Hearing aid"),
            Map.entry(TYPE_BLE_HEADSET, "BLE headset"),
            Map.entry(TYPE_BLE_SPEAKER, "BLE speaker")
        );
    }

    private static Operator instance;

    static public Operator getInstance(Context context) {
        if (instance == null) {
            instance = new Operator();
            instance.sharedPreferences = context.getSharedPreferences(Constants.SETTINGS_DATA, Context.MODE_PRIVATE);
            instance.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            instance.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            instance.registerAudioCallbacks(context);
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

    public void setMediaVolume(boolean isPlugged) {
        if (isPlugged) {
            setVolumeIfEnabled(Constants.CHECKBOX_MEDIA_PLUGGED, AudioManager.STREAM_MUSIC, Constants.SEEKBAR_MEDIA_PLUGGED);
        } else {
            setVolumeIfEnabled(Constants.CHECKBOX_MEDIA_UNPLUGGED, AudioManager.STREAM_MUSIC, Constants.SEEKBAR_MEDIA_UNPLUGGED);
        }
    }

    public void setPending(boolean isPending) {
        sharedPreferences.edit().putBoolean(Constants.SHARED_PREFERENCE_PENDING, isPending).apply();
    }

    public void handlePlugStateChange(Context context) {
        boolean isOutputConnected = isOutputDeviceConnected(context, audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS));

        boolean isServiceEnabled = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_IS_SERVICE_ENABLED, false);

        if (isServiceEnabled) {
            setMediaVolume(isOutputConnected);

            // Set other Volumes
            int currentRingerMode = audioManager.getRingerMode();
            boolean isSilentOrVibrate = currentRingerMode == AudioManager.RINGER_MODE_VIBRATE || currentRingerMode == AudioManager.RINGER_MODE_SILENT;

            String silentModeMessage;
            setPending(isSilentOrVibrate);

            if (isSilentOrVibrate) {
                silentModeMessage = Constants.TOAST_POSTPONE;
            } else {
                setAllVolumesExceptMedia(isOutputConnected);
                silentModeMessage = Constants.TOAST_VOLUME_ADJUSTED;
            }

            Toast.makeText(context, silentModeMessage, Toast.LENGTH_LONG).show();
        }
    }

    public void adjustOnRingerModeChanged(Context context, int newRingerMode) {
        boolean isPending = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_PENDING, false);

        if (isPending && (newRingerMode == AudioManager.RINGER_MODE_NORMAL)) {
            setAllVolumesExceptMedia(isOutputDeviceConnected(context, audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)));
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

    private void registerAudioCallbacks(Context context) {
        AudioDeviceCallback audioDeviceCallback = new AudioDeviceCallback() {
            @Override
            public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                super.onAudioDevicesAdded(addedDevices);
                handlePlugStateChange(context);
            }

            @Override
            public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                super.onAudioDevicesRemoved(removedDevices);
                handlePlugStateChange(context);
            }
        };

        audioManager.registerAudioDeviceCallback(audioDeviceCallback, new Handler(Looper.getMainLooper()));
    }

    private boolean isOutputDeviceConnected(Context context, AudioDeviceInfo[] devices) {
        for (AudioDeviceInfo device : devices) {
            if (device.isSink()) {
                int type = device.getType();
                if (!builtInSpeakerTypes.contains(type)) {
                    Toast.makeText(context, "Connected device: " + deviceTypes.get(type), Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }

        Toast.makeText(context, "No output device connected", Toast.LENGTH_SHORT).show();
        return false;
    }
}
