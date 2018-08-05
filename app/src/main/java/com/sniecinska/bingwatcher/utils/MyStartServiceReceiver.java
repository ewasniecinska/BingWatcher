package com.sniecinska.bingwatcher.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ewasniecinska on 04.08.2018.
 */

public class MyStartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Util.scheduleJob(context);
    }
}