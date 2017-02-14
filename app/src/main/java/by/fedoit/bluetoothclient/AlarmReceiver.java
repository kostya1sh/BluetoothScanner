package by.fedoit.bluetoothclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by kostya on 30.09.2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("AlarmReceiver", "received alarm !");

        Calendar calendar = Calendar.getInstance();


        // notify activity to start discovering
        Intent dataToActivity = new Intent("mainActivity");
        dataToActivity.putExtra("newAlarmTime", calendar.getTime().getTime() + 15000);
        context.sendBroadcast(dataToActivity);
    }
}
