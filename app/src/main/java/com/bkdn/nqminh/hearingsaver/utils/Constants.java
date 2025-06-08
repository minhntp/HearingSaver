package com.bkdn.nqminh.hearingsaver.utils;

public class Constants {
    // ---------------------------------------------------------------------------------------------    
    // SHARED PREFERENCES
    public static final String SETTINGS_DATA = "data";
    public static final String SHARED_PREFERENCE_IS_SERVICE_ENABLED = "enabled";
    public static final String SHARED_PREFERENCE_PENDING = "pending";
    public static final String SHARED_PREFERENCE_IS_FIRST_RUN = "is-first-run";
    public static final String SHARED_PREFERENCE_ADJUSTED_ON_FIRST_RUN = "adjusted-on-first-run";

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
    public static final String TOAST_POSTPONE = "Silent mode, postponed";
    public static final String TOAST_VOLUME_ADJUSTED = "Volumes adjusted";
    public static final String VOLUME_ADJUSTED_AFTER_POSTPONED = "Volumes adjusted after postponed";
    public static final String SETTINGS_SAVED = "Settings saved";

    public static final String TOAST_ON_BOOT_COMPLETED = "Boot completed\nStarting Hearing Saver...";

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
    // TAG
    public static final String DEBUG_TAG = "dbg";

}
