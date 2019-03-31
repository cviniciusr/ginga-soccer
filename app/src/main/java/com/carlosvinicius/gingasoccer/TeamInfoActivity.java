package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.carlosvinicius.gingasoccer.adapters.TeamPlayersRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.models.Team;
import com.carlosvinicius.gingasoccer.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamInfoActivity extends AppCompatActivity implements TeamPlayersRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = TeamInfoActivity.class.getSimpleName();

    private Team mTeam;
    private List<User> mPlayers;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ValueEventListener listener;

    @BindView(R.id.players_team_info_rv)
    RecyclerView mTeamPlayersRecyclerView;
    private TeamPlayersRecyclerViewAdapter mTeamPlayersRecyclerViewAdapter;

    @BindView(R.id.day_time_match)
    TextView mDayTimeMatchTextView;

    @BindView(R.id.address_team_info_tv)
    TextView mAddressTeamInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("teste 1");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("teste 2");
            }
        };

        mPlayers = new ArrayList<>();

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getResources().getString(R.string.team))) {
                mTeam = (Team) intent.getSerializableExtra(getResources().getString(R.string.team));

                Log.d(TAG, "Team: " + mTeam.getName());

                String dayTimeMatch = mTeam.getWeekday() + " - " + mTeam.getStartTime() + " " +
                        mTeam.getEndTime();

                mDayTimeMatchTextView.setText(dayTimeMatch);
                mAddressTeamInfoTextView.setText(mTeam.getAddress());

                if (mTeam.getPlayers() != null) {
                    for (Map.Entry<String, Object> entry : mTeam.getPlayers().entrySet()) {
                        String key = entry.getKey();
                        Boolean active = (Boolean) entry.getValue();

                        Query query = databaseReference.child("users").child(key);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User player = dataSnapshot.getValue(User.class);
                                player.setActive(active);
                                player.setPlayerKey(dataSnapshot.getKey());

                                mPlayers.add(player);

                                mTeamPlayersRecyclerViewAdapter.setPlayers(mPlayers);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println("teste 2");
                            }
                        });
                    }
                }
            }
        }

        mTeamPlayersRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mTeamPlayersRecyclerView.setHasFixedSize(true);
        mTeamPlayersRecyclerViewAdapter = new TeamPlayersRecyclerViewAdapter(
                mPlayers, mTeam.getTeamKey(), this, databaseReference);
        mTeamPlayersRecyclerView.setAdapter(mTeamPlayersRecyclerViewAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemId) {

    }
}
