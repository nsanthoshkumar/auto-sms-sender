package com.nsanthoshkumar.autotextsender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by nsanthoshkumar on 4/17/16.
 */
public class UserDBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Users.db";
    public static final String TBNAME = "Reminders";

    public UserDBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TBNAME + "(id integer primary key AUTOINCREMENT, mobile_number text, message text," +
                "due_time text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TBNAME);
        onCreate(db);
    }

    public long insertReminder(String mobileNumber, String message, String dueTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mobile_number", mobileNumber);
        contentValues.put("message", message);
        contentValues.put("due_time", dueTime);
        return db.insert(TBNAME, null, contentValues);
    }

    public Cursor getReminder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TBNAME + " where id=" + id + "", null);
        return res;
    }

    public Integer deleteReminder(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBNAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllReminders() {

        ArrayList<String> rem_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TBNAME, null);
        res.moveToFirst();
        try {
            while (res.isAfterLast() == false) {
                rem_list.add(
                        res.getString(res.getColumnIndex("id")) + "\t" +
                                res.getString(res.getColumnIndex("mobile_number")) + "\t" +
                                res.getString(res.getColumnIndex("message")) + "\t" +
                                res.getString(res.getColumnIndex("due_time"))
                );
                res.moveToNext();
            }
        } catch (Exception e) {
            Log.d("Exception", "" + e);
        } finally {
            db.close();
        }

        return rem_list;
    }
}
