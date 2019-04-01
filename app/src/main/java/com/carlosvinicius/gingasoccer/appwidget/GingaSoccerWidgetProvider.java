package com.carlosvinicius.gingasoccer.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.carlosvinicius.gingasoccer.LoginActivity;
import com.carlosvinicius.gingasoccer.R;

/**
 * Implementation of App Widget functionality.
 */
public class GingaSoccerWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ginga_soccer_widget);

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.ginga_soccer_preferences), Context.MODE_PRIVATE);
        String userKey = sharedPref.getString(context.getResources().getString(
                R.string.user_key), "");

        String text = "".equals(userKey) ? "Login" : "Teams";

        views.setTextViewText(R.id.widget_teams_tv, text);

        // Click event handler for the title, launches the app when the user clicks on title
        Intent loginIntent = new Intent(context, LoginActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, loginIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_teams_tv, titlePendingIntent);

        Intent intent = new Intent(context, GingaSoccerWidgetRemoteViewsService.class);
        intent.putExtra(context.getResources().getString(R.string.user_key), userKey);
        views.setRemoteAdapter(R.id.widget_teams_lv, intent);

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

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, GingaSoccerWidgetProvider.class);
            int[] appWidgetIds = mgr.getAppWidgetIds(cn);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ginga_soccer_widget);

            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getResources().getString(R.string.ginga_soccer_preferences), Context.MODE_PRIVATE);
            String userKey = sharedPref.getString(context.getResources().getString(
                    R.string.ginga_soccer_preferences), "");

            String text = "".equals(userKey) ? "Login" : "Teams";

            views.setTextViewText(R.id.widget_teams_tv, text);

            Intent loginIntent = new Intent(context, LoginActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, loginIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_teams_tv, titlePendingIntent);

            Intent listIntent = new Intent(context, GingaSoccerWidgetRemoteViewsService.class);
            listIntent.putExtra(context.getResources().getString(R.string.user_key), userKey);
            views.setRemoteAdapter(R.id.widget_teams_lv, listIntent);

            mgr.updateAppWidget(appWidgetIds, views);
            mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_teams_lv);
        }

        super.onReceive(context, intent);
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, GingaSoccerWidgetProvider.class));
        context.sendBroadcast(intent);
    }
}

