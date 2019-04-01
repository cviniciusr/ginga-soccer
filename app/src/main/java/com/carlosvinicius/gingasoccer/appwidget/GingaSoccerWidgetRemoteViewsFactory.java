package com.carlosvinicius.gingasoccer.appwidget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.carlosvinicius.gingasoccer.R;
import com.carlosvinicius.gingasoccer.models.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GingaSoccerWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = GingaSoccerWidgetRemoteViewsFactory.class.getSimpleName();

    private Context mContext;
    private String mUserKey = "";
    private List<Team> mTeams;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener listener;

    public GingaSoccerWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;

        if (intent.hasExtra(context.getResources().getString(R.string.user_key))) {
            mUserKey = intent.getStringExtra(context.getResources().getString(R.string.user_key));
        }
    }

    @Override
    public void onCreate() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, Object>> map =
                        (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();

                mTeams = new ArrayList<>();

                if (map == null) {
                    return;
                }

//                for (HashMap<String, Object> value : map.values()) {
                for(Map.Entry<String, HashMap<String, Object>> entry: map.entrySet()) {
                    String key = entry.getKey();
                    HashMap<String, Object> value = entry.getValue();
                    String name = (String) value.get("name");
                    String weekday = (String) value.get("weekday");
                    String startTime = (String) value.get("startTime");
                    String endTime = (String) value.get("endTime");
                    String address = (String) value.get("address");
                    Map<String, Object> players = (HashMap<String, Object>) value.get("usersTeam");

                    Team team = new Team(name, weekday, startTime, endTime, address);
                    team.setTeamKey(key);
                    team.setPlayers(players);

                    mTeams.add(team);
                }

                GingaSoccerWidgetProvider.sendRefreshBroadcast(mContext);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("teste 2");
            }
        };

        if (mUserKey != null && !"".equals(mUserKey)) {
            Query query = mDatabaseReference.child("team").orderByChild("usersTeam/" + mUserKey).equalTo(true);
            query.addValueEventListener(listener);
        }
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "Data Changed");
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        Log.d(TAG, "Get count");
        return mTeams == null ? 0 : mTeams.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "Get view at");

        if (position == AdapterView.INVALID_POSITION || mTeams == null || mTeams.isEmpty()) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ginga_soccer_widget_list_item);

        Team team = mTeams.get(position);

        String text = team.getWeekday() + " - " + team.getStartTime() + " " + team.getEndTime();

        rv.setTextViewText(R.id.widget_team_info, text);
        rv.setTextViewText(R.id.widget_team_address, team.getAddress());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.d(TAG, "Loading View");
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
