package com.bkdn.nqminh.hearingsaver.broadcast_receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bkdn.nqminh.hearingsaver.utils.Constants;
import com.bkdn.nqminh.hearingsaver.utils.Operator;

public class OnBluetoothBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {

            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);

            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Operator.getInstance(context)
                    .handlePlugStateChange(context, Constants.MESSAGE_BLUETOOTH_CONNECTED);
            } else if (state == BluetoothAdapter.STATE_DISCONNECTED) {
                Operator.getInstance(context)
                    .handlePlugStateChange(context, Constants.MESSAGE_BLUETOOTH_DISCONNECTED);
            }
        }
    }
}
