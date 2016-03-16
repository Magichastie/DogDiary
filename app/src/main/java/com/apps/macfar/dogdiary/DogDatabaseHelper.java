package com.apps.macfar.dogdiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * Created by William on 25-Feb-16.
 */
public class DogDatabaseHelper {
    SQLiteDatabase db;
    Context context;

    public static final Long FIVE_MINUTES = 5 * 60 * 1000l;

    public DogDatabaseHelper(Context c) {
        context = c;
        db = context.openOrCreateDatabase("DogDiary", Context.MODE_PRIVATE, null);
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Event(action VARCHAR, time INTEGER)");
        }
        catch (Exception e) {
            Log.e(MainActivity.TAG, e.getMessage());
        }
    }

    public EventInfo addEvent(String action) {
        Cursor res = db.rawQuery("Select * from Event where action = ? order by time desc LIMIT 1", new String[]{action});
        EventInfo last = null;
        if(res.moveToFirst())
            last = new EventInfo(res.getString(0), res.getLong(1));

        EventInfo ev = new EventInfo();
        if(last != null) {
            Long diff = ev.time.getTime() - last.time.getTime();
            if(diff < FIVE_MINUTES)
                return null;
        }

        ev.action = action;

        ContentValues cv = new ContentValues();
        cv.put("action", ev.action);
        cv.put("time", ev.time.getTime());
        db.insert("Event", null, cv);

        return ev;
    }
}
