package com.apps.macfar.dogdiary;

import java.util.Date;

/**
 * Created by William on 24-Feb-16.
 */
public class EventInfo {
    public String action;
    public Date time;

    EventInfo() {
        action = "";
        time = new Date();
    }

    EventInfo(String n, Date a) {
        action = n;
        time = a;
    }

    EventInfo(String n, Long a) {
        action = n;
        time = new Date(a);
    }
}
