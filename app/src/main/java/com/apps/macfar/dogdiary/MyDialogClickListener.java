package com.apps.macfar.dogdiary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by William on 25-Feb-16.
 */
public class MyDialogClickListener implements DialogInterface.OnClickListener {
    EventInfo ev;
    SQLiteDatabase db;

    MyDialogClickListener(EventInfo e, SQLiteDatabase d) {
        ev = e;
        db = d;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        AlertDialog alertDialog = (AlertDialog) dialog;
        EditText actionEdit = (EditText)alertDialog.findViewById(R.id.actionEdit);
        EditText timeEdit = (EditText)alertDialog.findViewById(R.id.timeEdit);

        ev.action = actionEdit.getText().toString();

        Long timeReference = ev.time.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(ev.time);
        int hour = Integer.parseInt(timeEdit.getText().toString()) / 100;
        int minute = Integer.parseInt(timeEdit.getText().toString()) % 100;
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        ev.time = cal.getTime();

        ContentValues cv = new ContentValues();
        cv.put("action", ev.action);
        cv.put("time", ev.time.getTime());
        db.update("Event", cv, "time = ?", new String[] { Long.toString(timeReference) });



        dialog.dismiss();
    }
}
