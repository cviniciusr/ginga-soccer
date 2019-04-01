package com.carlosvinicius.gingasoccer.appwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class GingaSoccerWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GingaSoccerWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
