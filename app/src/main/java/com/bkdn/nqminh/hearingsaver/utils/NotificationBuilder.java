package com.bkdn.nqminh.hearingsaver.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bkdn.nqminh.hearingsaver.R;
import com.bkdn.nqminh.hearingsaver.broadcast_receivers.NotificationDismissedReceiver;

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
        Intent notificationDismissedIntent = new Intent(context, NotificationDismissedReceiver.class);
        PendingIntent dismissedPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationDismissedIntent,
            PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        return new Notification.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
//            .setContentTitle(context.getText(R.string.notification_title))
//            .setContentText(context.getText(R.string.notification_message))
            .setSubText(context.getText(R.string.notification_message))
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(dismissedPendingIntent)
            .build();
    }
}
