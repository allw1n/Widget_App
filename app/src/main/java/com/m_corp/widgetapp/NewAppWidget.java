package com.m_corp.widgetapp;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

public class NewAppWidget extends AppWidgetProvider {

    private static final String SHARED_PREFS = "shared_prefs";
    private static final String COUNT = "count";
    private static final int ZERO = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPrefs = context.getSharedPreferences(
                SHARED_PREFS, Context.MODE_PRIVATE);
        int count = sharedPrefs.getInt(COUNT + appWidgetId, ZERO);
        count++;

        String date = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_id, String.valueOf(appWidgetId));
        views.setTextViewText(R.id.appwidget_update,
                context.getString(R.string.date_count_format, count, date));

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(COUNT + appWidgetId, count);
        editor.apply();

        Intent updateIntent = new Intent(context, NewAppWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] idArray = new int[] {appWidgetId};
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(
                context, appWidgetId, updateIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        views.setOnClickPendingIntent(R.id.button_update, updatePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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
}