package com.carlosvinicius.gingasoccer.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.carlosvinicius.gingasoccer.R;

/**
 * Implementation of App Widget functionality.
 */
public class GingaSoccerWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ginga_soccer_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
//
//        String text = recipe == null ? "Selecione sua receita" : (recipe.getName() + " (Click para alterar)");
//
//        views.setTextViewText(R.id.widget_recipe_name_tv, text);
//
//        // Click event handler for the title, launches the app when the user clicks on title
//        Intent titleIntent = new Intent(context, MainActivity.class);
//        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
//        views.setOnClickPendingIntent(R.id.widget_recipe_name_tv, titlePendingIntent);
//
//        Intent intent = new Intent(context, BakingWidgetRemoteViewsService.class);
//        views.setRemoteAdapter(R.id.widget_ingredient_lv, intent);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
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

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        final String action = intent.getAction();
//
//        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
//            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//            ComponentName cn = new ComponentName(context, BakingWidgetProvider.class);
//            int[] appWidgetIds = mgr.getAppWidgetIds(cn);
//
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
//            String text = recipe == null ? "Selecione sua receita" : (recipe.getName() + " (Click para alterar)");
//            views.setTextViewText(R.id.widget_recipe_name_tv, text);
//            mgr.updateAppWidget(appWidgetIds, views);
//
//            mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredient_lv);
//        }
//
//        super.onReceive(context, intent);
//    }
//
//    public static void sendRefreshBroadcast(Context context) {
//        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        intent.setComponent(new ComponentName(context, BakingWidgetProvider.class));
//        context.sendBroadcast(intent);
//    }
}

