package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.carlosvinicius.gingasoccer.adapters.TeamRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.models.Team;
import com.carlosvinicius.gingasoccer.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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
