package com.mobilepos.abc.abilitypos.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//            wasScreenOn = false;
        }
    }
}
