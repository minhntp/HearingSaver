package com.bkdn.nqminh.hearingsaver.utils;

public class Constants {
    // ---------------------------------------------------------------------------------------------    
    // SHARED PREFERENCES
    public static final String SETTINGS_DATA = "data";
    public static final String IS_SERVICE_ENABLED = "enabled";
    public static final String SETTINGS_PENDING = "pending";
    public static final String IS_PLUGGED_IN = "is-plugged-in";
    public static final String FIRST_RUN_0 = "first-run-0";
    public static final String FIRST_RUN_1 = "first-run-1";
    public static final String VOLUME_1 = "volume1";
    public static final String VOLUME_2 = "volume2";
    public static final String VOLUME_3 = "volume3";
    public static final String VOLUME_4 = "volume4";
    public static final String VOLUME_5 = "volume5";
    public static final String VOLUME_6 = "volume6";
    public static final String VOLUME_7 = "volume7";
    public static final String VOLUME_8 = "volume8";
    public static final String CHECK_1 = "check1";
    public static final String CHECK_2 = "check2";
    public static final String CHECK_3 = "check3";
    public static final String CHECK_4 = "check4";
    public static final String CHECK_5 = "check5";
    public static final String CHECK_6 = "check6";
    public static final String CHECK_7 = "check7";
    public static final String CHECK_8 = "check8";

    // ---------------------------------------------------------------------------------------------
    // TOAST MESSAGES
    public static final String TOAST_POSTPONE = "Phone is in silent or vibrate mode.\nPostponed adjustment!";
    public static final String TOAST_VOLUME_ADJUSTED = "Volume adjusted!";
    public static final String VOLUME_ADJUSTED_AFTER_POSTPONED = "Volume adjusted after postponed!";
    public static final String SETTINGS_SAVED = "Settings saved!";
    public static final String TOAST_ENABLE = "Settings saved.\nService enabled!";
    public static final String TOAST_DISABLE = "Settings saved.\nService disabled!";

    public static final String TOAST_ON_BOOT = "Boot completed!\nStarting Hearing Saver...";
    public static final String TOAST_ACTIVITY_DESTROYED = "Main Activity is destroyed!";
    public static final String TOAST_SERVICE_DESTROYED = "Service is destroyed!";
    public static final String TOAST_RESTART_SERVICE = "Restarting Hearing Saver...";
    public static final String TOAST_JACK_DISCONNECTED = "Headphone jack is disconnected!";
    public static final String TOAST_JACK_CONNECTED = "Headphone jack is connected!";
    public static final String TOAST_BLUETOOTH_DISCONNECTED = "Bluetooth device is disconnected!";
    public static final String TOAST_BLUETOOTH_CONNECTED = "Bluetooth device is connected!";
    public static final String TOAST_ONE_DEVICE_CONNECTED = "One device is connected!";
    public static final String TOAST_DEVICES_CONNECTED = " devices are connected!";
    public static final String TOAST_NO_DEVICES_CONNECTED = "No device is connected!";

    // ---------------------------------------------------------------------------------------------
    // SETTINGS
    public static final int DEFAULT_VOLUME_HEADPHONE = 3;
    public static final int DEFAULT_VOLUME_SPEAKER = 13;

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
    public static final String DEBUG_TAG = "debug";



}
