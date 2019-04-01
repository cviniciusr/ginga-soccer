package com.carlosvinicius.gingasoccer.appwidget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.carlosvinicius.gingasoccer.R;

public class GingaSoccerWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    public GingaSoccerWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
//        return BakingWidgetProvider.recipe == null ? 0 : BakingWidgetProvider.recipe.getIngredients().size();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
//        if (position == AdapterView.INVALID_POSITION || BakingWidgetProvider.recipe == null) {
//            return null;
//        }
//
//        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_list_item);
//
//        Ingredient ingredient = BakingWidgetProvider.recipe.getIngredients().get(position);
//
//        String text = ingredient.getIngredient() + " - " + ingredient.getQuantity() + " (" + ingredient.getMeasure() + ")";
//
//        rv.setTextViewText(R.id.widget_ingredient_item, text);
//
//        return rv;
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
