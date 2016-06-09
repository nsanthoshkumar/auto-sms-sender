package com.nsanthoshkumar.autotextsender;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    UserDBHelper reminderDb;
    TimePicker timePicker;
    private PendingIntent pendingIntent;
    private Intent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderDb = new UserDBHelper(this);
        alarmIntent = new Intent(MainActivity.this, AlarmTrigger.class);
        setContentView(R.layout.activity_main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    public void saveAndSend(View view) {
        EditText mobileNum = (EditText) findViewById(R.id.mobileNum);
        EditText msgText = (EditText) findViewById(R.id.msgText);
        EditText freqNum = (EditText) findViewById(R.id.freqNum);


        String mNum = mobileNum.getText().toString();
        String mText = msgText.getText().toString();
        String tPicker = null;
        //TODO Need to understand for lower versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tPicker = String.format("%d:%d", timePicker.getHour(), timePicker.getMinute());
        }
        long idInserted = reminderDb.insertReminder(mNum, mText, tPicker);

        Intent intent = new Intent(MainActivity.this, DisplayReminder.class);
        startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            enableAlarmForMsg(mNum, mText, timePicker.getHour(), timePicker.getMinute(), idInserted, Integer.valueOf(freqNum.getText().toString()));
        }
        //SmsManager sM = SmsManager.getDefault();
        //sM.sendTextMessage(mNum,null,mText, null, null);


    }

    private void enableAlarmForMsg(String mNum, String mText, int hour, int minute, long idInserted, int freqNum) {
        alarmIntent.putExtra("mNumber", mNum);
        alarmIntent.putExtra("msgText", mText);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (int) (idInserted), alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Log.d("I m Heree!!", "" + calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Log.d("after setting!!", "" + calendar.getTimeInMillis());
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * freqNum, pendingIntent);
    }

    public void displayList(View view) {
        Intent intent = new Intent(MainActivity.this, DisplayReminder.class);
        startActivity(intent);
    }
}
