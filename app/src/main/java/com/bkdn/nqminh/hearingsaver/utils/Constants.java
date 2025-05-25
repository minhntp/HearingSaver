package com.bkdn.nqminh.hearingsaver.utils;

public class Constants {
    // ---------------------------------------------------------------------------------------------    
    // SHARED PREFERENCES
    public static final String SETTINGS_DATA = "data";
    public static final String SHARED_PREFERENCE_IS_SERVICE_ENABLED = "enabled";
    public static final String SHARED_PREFERENCE_PENDING = "pending";
    public static final String SHARED_PREFERENCE_IS_FIRST_RUN = "is-first-run";
    public static final String SHARED_PREFERENCE_ADJUSTED_ON_FIRST_RUN = "adjusted-on-first-run";
    public static final String SHARED_PREFERENCE_PREVIOUS_PLUG_STATE = "previous-plug-state ";
    public static final String SHARED_PREFERENCE_POSTPONED_PLUG_STATE = "postponed-plug-state"; // Used for pending adjustments

    public static final String SEEKBAR_RING_PLUGGED = "volume_ring_plugged";
    public static final String SEEKBAR_RING_UNPLUGGED = "volume_ring_unplugged";
    public static final String SEEKBAR_NOTIFICATION_PLUGGED = "volume_notification_plugged";
    public static final String SEEKBAR_NOTIFICATION_UNPLUGGED = "volume_notification_unplugged";
    public static final String SEEKBAR_FEEDBACK_PLUGGED = "volume_feedback_plugged";
    public static final String SEEKBAR_FEEDBACK_UNPLUGGED = "volume_feedback_unplugged";
    public static final String SEEKBAR_CALL_PLUGGED = "volume_call_plugged";
    public static final String SEEKBAR_CALL_UNPLUGGED = "volume_call_unplugged";
    public static final String SEEKBAR_ALARM_PLUGGED = "volume_alarm_plugged";
    public static final String SEEKBAR_ALARM_UNPLUGGED = "volume_alarm_unplugged";
    public static final String SEEKBAR_MEDIA_PLUGGED = "volume_media_plugged";
    public static final String SEEKBAR_MEDIA_UNPLUGGED = "volume_media_unplugged";

    public static final String CHECKBOX_RING_PLUGGED = "enabled_ring_plugged";
    public static final String CHECKBOX_RING_UNPLUGGED = "enabled_ring_unplugged";
    public static final String CHECKBOX_NOTIFICATION_PLUGGED = "enabled_notification_plugged";
    public static final String CHECKBOX_NOTIFICATION_UNPLUGGED = "enabled_notification_unplugged";
    public static final String CHECKBOX_FEEDBACK_PLUGGED = "enabled_feedback_plugged";
    public static final String CHECKBOX_FEEDBACK_UNPLUGGED = "enabled_feedback_unplugged";
    public static final String CHECKBOX_CALL_PLUGGED = "enabled_call_plugged";
    public static final String CHECKBOX_CALL_UNPLUGGED = "enabled_call_unplugged";
    public static final String CHECKBOX_ALARM_PLUGGED = "enabled_alarm_plugged";
    public static final String CHECKBOX_ALARM_UNPLUGGED = "enabled_alarm_unplugged";
    public static final String CHECKBOX_MEDIA_PLUGGED = "enabled_media_plugged";
    public static final String CHECKBOX_MEDIA_UNPLUGGED = "enabled_media_unplugged";
    // ---------------------------------------------------------------------------------------------
    // TOAST MESSAGES
    public static final String TOAST_POSTPONE = "Phone is in silent or vibrate mode\nVolume adjustment postponed";
    public static final String TOAST_VOLUME_ADJUSTED = "Volume adjusted";
    public static final String VOLUME_ADJUSTED_AFTER_POSTPONED = "Volume adjusted after postponed";
    public static final String SETTINGS_SAVED = "Settings saved";
    public static final String TOAST_ENABLE = "Settings saved";
    public static final String TOAST_DISABLE = "Settings saved";

    public static final String TOAST_ON_BOOT_COMPLETED = "Boot completed\nStarting Hearing Saver...";
    public static final String TOAST_JACK_DISCONNECTED = "Headphone jack is disconnected";
    public static final String TOAST_JACK_CONNECTED = "Headphone jack is connected";
    public static final String TOAST_BLUETOOTH_DISCONNECTED = "Bluetooth device is disconnected";
    public static final String TOAST_BLUETOOTH_CONNECTED = "Bluetooth device is connected";
    public static final String TOAST_ONE_DEVICE_CONNECTED = "One device is connected";
    public static final String TOAST_DEVICES_CONNECTED = " devices are connected";
    public static final String TOAST_NO_DEVICES_CONNECTED = "No device is connected";

    // ---------------------------------------------------------------------------------------------
    // SETTINGS
    public static final int DEFAULT_VOLUME_HEADPHONE = 3;
    public static final int DEFAULT_VOLUME_SPEAKER = 13;
    public static final int DEFAULT_VOLUME_SPEAKER_MEDIA = 5;
    public static final int MAX_VOLUME = 15;

    // ---------------------------------------------------------------------------------------------
    // NOTIFICATION
    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "hearing-saver-notification-channel";
    public static final String CHANNEL_NAME = "Main notification channel";
    public static final String CHANNEL_DESCRIPTION = "Shows status of Hearing Saver";

    // ---------------------------------------------------------------------------------------------
    // INTENT MESSAGE
    public static final String INTENT_RESTART_SERVICE = "restart-service-from-destroyed-state";

    // ---------------------------------------------------------------------------------------------
    // TAG
    public static final String DEBUG_TAG = "dbg";

    // MESSAGES BETWEEN CLASSES
    public static final String MESSAGE_WIRE_PLUGGED = "wire-plugged";
    public static final String MESSAGE_WIRE_UNPLUGGED = "wire-unplugged";
    public static final String MESSAGE_BLUETOOTH_CONNECTED = "bluetooth-plugged";
    public static final String MESSAGE_BLUETOOTH_DISCONNECTED = "bluetooth-unplugged";
    public static final String MESSAGE_FIRST_RUN = "first-run";


}
