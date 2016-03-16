package com.apps.macfar.dogdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by William on 14-Nov-15.
 */
public class myArrayAdapter extends ArrayAdapter<EventInfo> {

    public myArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public myArrayAdapter(Context context, List<EventInfo> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        EventInfo p = getItem(position);

        if (p != null) {
            TextView action = (TextView) v.findViewById(R.id.actionTV);
            TextView time = (TextView) v.findViewById(R.id.timeTV);

            if (action != null) {
                action.setText(p.action);
            }

            if (time != null) {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                time.setText(df.format(p.time));
            }
        }

        return v;
    }
}
