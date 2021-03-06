package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.carlosvinicius.gingasoccer.adapters.TeamRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.appwidget.GingaSoccerWidgetProvider;
import com.carlosvinicius.gingasoccer.models.Team;
import com.carlosvinicius.gingasoccer.models.User;
import com.carlosvinicius.gingasoccer.models.UsersTeam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends AppCompatActivity implements TeamRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = UserInfoActivity.class.getSimpleName();

    private User user;
    private String userKey;
    private List<Team> mTeamList;

    @BindView(R.id.nickname_info_tv)
    TextView mNicknameInfoTextView;

    @BindView(R.id.fullname_info_tv)
    TextView mFullnameInfoTextView;

    @BindView(R.id.teams_rv)
    RecyclerView mTeamsRecyclerView;
    private TeamRecyclerViewAdapter mTeamRecyclerViewAdapter;

    @BindView(R.id.new_team_fab)
    FloatingActionButton newTeamFloatingActionButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ValueEventListener listener;
//    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getResources().getString(R.string.user))) {
                user = (User) intent.getSerializableExtra(getResources().getString(R.string.user));

                mNicknameInfoTextView.setText(user.getNickname());
                mFullnameInfoTextView.setText(user.getFullname());

                Log.d(TAG, "User: " + user.getFullname());
            }

            if (intent.hasExtra(getResources().getString(R.string.user_key))) {
                userKey = intent.getStringExtra(getResources().getString(R.string.user_key));
            }
        }

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

//        childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                updateRecyclerView(dataSnapshot);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                updateRecyclerView(dataSnapshot);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                updateRecyclerView(dataSnapshot);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                Log.i(TAG, "childEventListener, childEventListener()");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.i(TAG, "childEventListener, onCancelled()");
//            }
//        };

        mTeamList = new ArrayList<>();

        mTeamsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mTeamsRecyclerView.setHasFixedSize(true);
        mTeamRecyclerViewAdapter = new TeamRecyclerViewAdapter(mTeamList, this);
        mTeamsRecyclerView.setAdapter(mTeamRecyclerViewAdapter);

        newTeamFloatingActionButton.setOnClickListener(view -> {
            Intent newTeamIntent = new Intent(UserInfoActivity.this, CreateTeamActivity.class);
            newTeamIntent.putExtra(getResources().getString(R.string.user), user);
            newTeamIntent.putExtra(getResources().getString(R.string.user_key), userKey);

            if (newTeamIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(newTeamIntent);
            }
        });

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, Object>> map =
                        (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();

                mTeamList = new ArrayList<>();

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

                    mTeamList.add(team);
                }

                mTeamRecyclerViewAdapter.setTeamList(mTeamList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(UserInfoActivity.this
//                        , "Error finding teams: ", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Query query = databaseReference.orderByChild("team").equalTo(userKey);
        Query query = databaseReference.child("team").orderByChild("usersTeam/" + userKey).equalTo(true);

        query.addValueEventListener(listener);

//        query.addChildEventListener(childEventListener);
    }

//    private void updateRecyclerView(DataSnapshot dataSnapshot) {
//        String name = (String) dataSnapshot.child("name").getValue();
//        String weekday = (String) dataSnapshot.child("weekday").getValue();
//        String startTime = (String) dataSnapshot.child("startTime").getValue();
//        String endTime = (String) dataSnapshot.child("endTime").getValue();
//        String address = (String) dataSnapshot.child("address").getValue();
//
//        Team team = new Team(name, weekday, startTime, endTime, address);
//
//        mTeamList.add(team);
//
//        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
//        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
//
//        while (iterator.hasNext()) {
//            Team team = iterator.next().getValue(Team.class);
//
//            System.out.println("Teste");
//
//            databaseReference.child("team").child(team.getTeamKey()).
//
//            mTeamList.add(team);
//        }
//
//        mTeamRecyclerViewAdapter.setTeamList(mTeamList);
//    }

    @Override
    public void onListItemClick(int clickedItemId) {
        final Team team = mTeamList.get(clickedItemId);

        Intent intent = new Intent(this, TeamInfoActivity.class);
        intent.putExtra(getResources().getString(R.string.team), team);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:

                mAuth.signOut();

                SharedPreferences sharedPref = getSharedPreferences(
                        getResources().getString(R.string.ginga_soccer_preferences), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getResources().getString(R.string.user_key), "");
                editor.commit();

                GingaSoccerWidgetProvider.sendRefreshBroadcast(this);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
