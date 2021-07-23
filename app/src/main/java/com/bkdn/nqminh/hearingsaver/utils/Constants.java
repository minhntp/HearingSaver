package com.bkdn.nqminh.hearingsaver.utils;

public class Constants {
    // ---------------------------------------------------------------------------------------------    
    // SHARED PREFERENCES
    public static final String SETTINGS_DATA = "data";
    public static final String SP_IS_SERVICE_ENABLED = "enabled";
    public static final String SETTINGS_PENDING = "pending";
    public static final String SKB_RING_PLUGGED = "volume1";
    public static final String SKB_RING_UNPLUGGED = "volume2";
    public static final String SKB_NOTI_PLUGGED = "volume3";
    public static final String SKB_NOTI_UNPLUGGED = "volume4";
    public static final String SKB_FEEDBACK_PLUGGED = "volume5";
    public static final String SKB_FEEDBACK_UNPLUGGED = "volume6";
    public static final String SKB_MEDIA_PLUGGED = "volume7";
    public static final String SKB_MEDIA_UNPLUGGED = "volume8";
    public static final String CB_RING_PLUGGED = "check1";
    public static final String CB_RING_UNPLUGGED = "check2";
    public static final String CB_NOTI_PLUGGED = "check3";
    public static final String CB_NOTI_UNPLUGGED = "check4";
    public static final String CB_FEEDBACK_PLUGGED = "check5";
    public static final String CB_FEEDBACK_UNPLUGGED = "check6";
    public static final String CB_MEDIA_PLUGGED = "check7";
    public static final String CB_MEDIA_UNPLUGGED = "check8";
    public static final String SP_HEADSET_STATE = "headset-state";
    public static final String SP_IS_FIRST_RUN = "is-first-run";
    public static final String SP_ADJUSTED_ON_FIRST_RUN = "adjusted-on-first-run";
    public static final String SP_PREVIOUS_PLUG_STATE = "previous-plug-state ";
    public static final String SP_POSTPONED_PLUG_STATE = "postponed-plug-state"; // Used for pending adjustments
    public static final String STATE_PLUGGED = "state-plugged";
    public static final String STATE_UNPLUGGED = "state-unplugged";

    // ---------------------------------------------------------------------------------------------
    // TOAST MESSAGES
    public static final String TOAST_POSTPONE = "Phone is in silent or vibrate mode.\nPostponed adjustment!";
    public static final String TOAST_VOLUME_ADJUSTED = "Volume adjusted!";
    public static final String VOLUME_ADJUSTED_AFTER_POSTPONED = "Volume adjusted after postponed!";
    public static final String SETTINGS_SAVED = "Settings saved!";
    public static final String TOAST_ENABLE = "Settings saved.\nService enabled!";
    public static final String TOAST_DISABLE = "Settings saved.\nService disabled!";

    public static final String TOAST_ON_BOOT_COMPLETED = "Boot completed!\nStarting Hearing Saver...";
    public static final String TOAST_ACTIVITY_DESTROYED = "Main Activity is destroyed!";
    public static final String TOAST_SERVICE_DESTROYED = "Service is destroyed!";
    public static final String TOAST_RESTART_SERVICE = "Restarting Hearing Saver...";
    public static final String TOAST_JACK_DISCONNECTED = "Headphone jack is disconnected.";
    public static final String TOAST_JACK_CONNECTED = "Headphone jack is connected.";
    public static final String TOAST_BLUETOOTH_DISCONNECTED = "Bluetooth device is disconnected.";
    public static final String TOAST_BLUETOOTH_CONNECTED = "Bluetooth device is connected.";
    public static final String TOAST_ONE_DEVICE_CONNECTED = "One device is connected.";
    public static final String TOAST_DEVICES_CONNECTED = " devices are connected.";
    public static final String TOAST_NO_DEVICES_CONNECTED = "No device is connected.";

    // ---------------------------------------------------------------------------------------------
    // SETTINGS
    public static final int DEFAULT_VOLUME_HEADPHONE = 3;
    public static final int DEFAULT_VOLUME_SPEAKER = 13;
    public static final int DEFAULT_VOLUME_SPEAKER_MEDIA = 5;

    // ---------------------------------------------------------------------------------------------
    // NOTIFICATION
    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "hearing-saver-notification-channel";
    public static final String CHANNEL_NAME = "Main notification channel";
    public static final String CHANNEL_DESCRIPTION = "Shows status of Hearing Saver";
    public static final String NOTIFICATION_TITLE = "Hearing Saver";
    public static final String NOTIFICATION_MESSAGE = "Service is running";
    public static final String NOTIFICATION_TICKER = "Hearing Saver is running";

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
