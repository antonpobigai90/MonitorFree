package com.monitorfree.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.monitorfree.UserModel.AddMonitor;

import java.util.ArrayList;

/**
 * Created by fabio on 24/01/2016.
 */
public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");

//        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLockTag");
//        wakeLock.acquire();
//
//        Intent newIntent = new Intent(context, BService.class);
//        context.startService(newIntent);
    }

}
