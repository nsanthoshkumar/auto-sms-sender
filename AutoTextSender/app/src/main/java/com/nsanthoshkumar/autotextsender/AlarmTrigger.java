package com.nsanthoshkumar.autotextsender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by nsanthoshkumar on 6/4/16.
 */
public class AlarmTrigger extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        System.out.println("" + intent);
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        Log.d("Alarm time now", "" + calendar.getTimeInMillis());
        Log.d("Alarm Trigger!!", intent.getStringExtra("msgText"));
        SmsManager sM = SmsManager.getDefault();
        sM.sendTextMessage(intent.getStringExtra("mNumber"), null, intent.getStringExtra("msgText"), null, null);
    }
}
