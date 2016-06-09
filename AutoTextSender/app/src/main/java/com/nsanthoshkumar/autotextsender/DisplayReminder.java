package com.nsanthoshkumar.autotextsender;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayReminder extends AppCompatActivity {
    TableLayout tableLayout;
    UserDBHelper dbHelper;
    ArrayList<String> reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reminder);
        tableLayout = (TableLayout) findViewById(R.id.reminderTable);
        dbHelper = new UserDBHelper(DisplayReminder.this);
        reminders = new ArrayList<String>();
        reminders = dbHelper.getAllReminders();
        for (int i = 0; i < reminders.size(); i++) {
            final String[] items = reminders.get(i).split("\t");
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(DisplayReminder.this);
            textView.setTextSize(12);
            textView.setText(items[0]);
            row.addView(textView);

            TextView tName = new TextView(DisplayReminder.this);
            textView.append(" " + items[1]);
            tName.setTextSize(12);
            row.addView(tName);

            TextView tNumber = new TextView(DisplayReminder.this);
            textView.append(" " + items[2]);
            tNumber.setTextSize(10);
            row.addView(tNumber);

            TextView tDueTime = new TextView(DisplayReminder.this);
            textView.append(" " + items[3] + " ");
            tDueTime.setTextSize(10);
            row.addView(tDueTime);

            Button btn = new Button(DisplayReminder.this);
            btn.setText("Delete");
            btn.setId(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent delIntent = new Intent(DisplayReminder.this, AlarmTrigger.class);
                    PendingIntent delPendingIntent = PendingIntent.getBroadcast(DisplayReminder.this, Integer.parseInt(items[0]), delIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    delPendingIntent.cancel();
                    alarmMgr.cancel(delPendingIntent);
                    dbHelper.deleteReminder(Integer.parseInt(items[0]));
                    // System.out.println("v.getid is:- " + v.getId());
                }
            });
            row.addView(btn);
            tableLayout.addView(row);


        }

    }

}

