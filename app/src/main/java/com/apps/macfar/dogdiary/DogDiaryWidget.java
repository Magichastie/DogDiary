package com.apps.macfar.dogdiary;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class DogDiaryWidget extends AppWidgetProvider {

    private static String FOOD_ACTION = "FOOD_ACTION";
    private static String WATER_ACTION = "WATER_ACTION";
    private static String POO_ACTION = "POO_ACTION";
    private static String PEE_ACTION = "PEE_ACTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.event_buttons);

            remoteViews.setOnClickPendingIntent(R.id.widgetFoodBtn, getPendingSelfIntent(context, FOOD_ACTION));
            remoteViews.setOnClickPendingIntent(R.id.widgetWaterBtn, getPendingSelfIntent(context, WATER_ACTION));
            remoteViews.setOnClickPendingIntent(R.id.widgetPooBtn, getPendingSelfIntent(context, POO_ACTION));
            remoteViews.setOnClickPendingIntent(R.id.widgetPeeBtn, getPendingSelfIntent(context, PEE_ACTION));

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dog_diary_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String intentAction = intent.getAction();
        if (intentAction.equals(FOOD_ACTION)) {
            DogDatabaseHelper db = new DogDatabaseHelper(context);
            db.addEvent("Food");
        }
        else if(intentAction.equals(WATER_ACTION)) {
            DogDatabaseHelper db = new DogDatabaseHelper(context);
            db.addEvent("Water");
        }
        else if(intentAction.equals(POO_ACTION)) {
            DogDatabaseHelper db = new DogDatabaseHelper(context);
            db.addEvent("Poo");
        }
        else if(intentAction.equals(PEE_ACTION)) {
            DogDatabaseHelper db = new DogDatabaseHelper(context);
            db.addEvent("Pee");
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

