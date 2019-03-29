package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.carlosvinicius.gingasoccer.adapters.TeamRecyclerViewAdapter;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends AppCompatActivity implements TeamRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = UserInfoActivity.class.getSimpleName();

    private User user;
    private String userKey;
    private List<Team> mTeamList;

    @BindView(R.id.teams_rv)
    RecyclerView mTeamsRecyclerView;
    private TeamRecyclerViewAdapter mTeamRecyclerViewAdapter;

    @BindView(R.id.new_team_fab)
    FloatingActionButton newTeamFloatingActionButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getResources().getString(R.string.user))) {
                user = (User) intent.getSerializableExtra(getResources().getString(R.string.user));

                Log.d(TAG, "User: " + user.getFullname());
            }

            if (intent.hasExtra(getResources().getString(R.string.user_key))) {
                userKey = intent.getStringExtra(getResources().getString(R.string.user_key));
            }
        }

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateRecyclerView(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateRecyclerView(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateRecyclerView(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "childEventListener, childEventListener()");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "childEventListener, onCancelled()");
            }
        };

        mTeamList = new ArrayList<>();

        Query query = databaseReference.orderByChild("userKey").equalTo(userKey);
        query.addChildEventListener(childEventListener);

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
    }

    private void updateRecyclerView(DataSnapshot dataSnapshot) {
        mTeamList = new ArrayList<>();

        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

        while (iterator.hasNext()) {
            UsersTeam team = iterator.next().getValue(UsersTeam.class);

            System.out.println("Teste");

//            databaseReference.child("team").child(team.getTeamKey()).
//
//            mTeamList.add(team);
        }

        mTeamRecyclerViewAdapter.setTeamList(mTeamList);
    }

    @Override
    public void onListItemClick(int clickedItemId) {
        final Team team = mTeamList.get(clickedItemId);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(getResources().getString(R.string.team), team);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
