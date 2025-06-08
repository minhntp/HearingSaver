package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bkdn.nqminh.hearingsaver.services.MyService;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.bkdn.nqminh.hearingsaver.NOTIFICATION_DISMISSED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myServiceIntent = new Intent(context, MyService.class);
        myServiceIntent.setAction(ACTION);
        context.startForegroundService(myServiceIntent);
    }
}
