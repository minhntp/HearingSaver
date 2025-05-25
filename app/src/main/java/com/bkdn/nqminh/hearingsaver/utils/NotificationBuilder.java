package com.bkdn.nqminh.hearingsaver.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import com.bkdn.nqminh.hearingsaver.R;

public class NotificationBuilder {
    private static NotificationBuilder instance;

    private NotificationBuilder() {
    }

    public static NotificationBuilder getInstance() {
        if (instance == null) {
            instance = new NotificationBuilder();
        }
        return instance;
    }

    public Notification getNotification(Context context, PendingIntent pendingIntent) {
        return new Notification.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
//            .setContentTitle(context.getText(R.string.notification_title))
//            .setContentText(context.getText(R.string.notification_message))
            .setSubText(context.getText(R.string.notification_message))
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setContentIntent(pendingIntent)
            .build();
    }
}
