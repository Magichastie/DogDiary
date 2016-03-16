package com.apps.macfar.dogdiary;

import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "DogDiary";

    ListView list;
    myArrayAdapter adapter;
    ArrayList<EventInfo> events;
    DogDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        list = (ListView) findViewById(R.id.listView);
        events = new ArrayList<EventInfo>();
        adapter = new myArrayAdapter(this, events);
        list.setAdapter(adapter);
        registerForContextMenu(list);

        init();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menulist, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        EventInfo ev;
        switch(item.getItemId()) {
            case R.id.edit:
                ev = adapter.getItem(info.position);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Edit item");
                View dialogView = View.inflate(this, R.layout.edit_dialog, null);
                EditText act = (EditText) dialogView.findViewById(R.id.actionEdit);
                EditText time = (EditText) dialogView.findViewById(R.id.timeEdit);

                act.setText(ev.action);
                SimpleDateFormat df = new SimpleDateFormat("HHmm");
                time.setText(df.format(ev.time));

                builder.setView(dialogView)
                        .setPositiveButton("Yes", new MyDialogClickListener(ev, db.db))
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                return true;
            case R.id.delete:
                ev = adapter.getItem(info.position);
                db.db.delete("Event", "time = ?", new String[]{Long.toString(ev.time.getTime())});
                adapter.remove(ev);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteAll) {
            db.db.execSQL("DELETE FROM Event");
            adapter.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init() {
        db = new DogDatabaseHelper(this);

        Cursor res = db.db.rawQuery("Select * from Event order by time desc", null);
        res.moveToFirst();
        while(!res.isAfterLast()) {
            EventInfo ev = new EventInfo(res.getString(0), res.getLong(1));
            adapter.add(ev);
            res.moveToNext();
        }
    }

    public void addEvent(View v) {
        String action;
        switch (v.getId()) {
            case R.id.foodBtn:
                action = "Food";
                break;
            case R.id.waterBtn:
                action = "Water";
                break;
            case R.id.pooBtn:
                action = "Poo";
                break;
            case R.id.peeBtn:
                action = "Pee";
                break;
            default:
                return;
        }
        EventInfo ev = db.addEvent(action);
        if(ev != null)
            adapter.insert(ev, 0);
    }
}



