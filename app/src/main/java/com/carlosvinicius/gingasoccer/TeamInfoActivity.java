package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carlosvinicius.gingasoccer.adapters.TeamPlayersRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.models.Team;
import com.carlosvinicius.gingasoccer.models.User;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamInfoActivity extends AppCompatActivity implements TeamPlayersRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = TeamInfoActivity.class.getSimpleName();

    private Team mTeam;
    private ArrayList<User> mPlayers;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @BindView(R.id.players_team_info_rv)
    RecyclerView mTeamPlayersRecyclerView;
    private TeamPlayersRecyclerViewAdapter mTeamPlayersRecyclerViewAdapter;

    @BindView(R.id.day_time_match)
    TextView mDayTimeMatchTextView;

    @BindView(R.id.address_team_info_tv)
    TextView mAddressTeamInfoTextView;

    @BindView(R.id.search_edt)
    EditText mSearchPlayerEditText;

    @BindView(R.id.nickname_searched_tv)
    TextView mNicknameSearchedTextView;

    @BindView(R.id.fullname_searched_tv)
    TextView mFullnameSearchedTextView;

    @BindView(R.id.add_to_team_btn)
    Button mAddPlayerToteam;

    private User mSearchedPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

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

                        Query query = mDatabaseReference.child("users").child(key);
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
                mPlayers, mTeam.getTeamKey(), this, mDatabaseReference);
        mTeamPlayersRecyclerView.setAdapter(mTeamPlayersRecyclerViewAdapter);
    }

    @OnClick(R.id.begin_match_btn)
    public void beginMatch(View view) {
        Intent intent = new Intent(this, PlayersDrawActivity.class);
        intent.putExtra(getResources().getString(R.string.players), mPlayers);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.search_player_btn)
    public void searchPlayer(View view) {
        String email = mSearchPlayerEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mSearchPlayerEditText.setError("Required.");
            return;
        } else {
            mSearchPlayerEditText.setError(null);
        }

        mNicknameSearchedTextView.setVisibility(View.INVISIBLE);
        mFullnameSearchedTextView.setVisibility(View.VISIBLE);
        mAddPlayerToteam.setVisibility(View.INVISIBLE);
        mFullnameSearchedTextView.setText(getResources().getString(R.string.no_player_found));

        Query query = mDatabaseReference.child("users").orderByChild("email").equalTo(email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mSearchedPlayer = dataSnapshot.getValue(User.class);
                mSearchedPlayer.setActive(true);
                mSearchedPlayer.setPlayerKey(dataSnapshot.getKey());

                mNicknameSearchedTextView.setText(mSearchedPlayer.getNickname());
                mNicknameSearchedTextView.setVisibility(View.VISIBLE);

                mFullnameSearchedTextView.setText(mSearchedPlayer.getFullname());
                mFullnameSearchedTextView.setVisibility(View.VISIBLE);

                mAddPlayerToteam.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

//    new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            mSearchedPlayer = dataSnapshot.getValue(User.class);
//            mSearchedPlayer.setActive(true);
//            mSearchedPlayer.setPlayerKey(dataSnapshot.getKey());
//
//            mNicknameSearchedTextView.setText(mSearchedPlayer.getNickname());
//            mNicknameSearchedTextView.setVisibility(View.VISIBLE);
//
//            mFullnameSearchedTextView.setText(mSearchedPlayer.getFullname());
//            mFullnameSearchedTextView.setVisibility(View.VISIBLE);
//
//            mAddPlayerToteam.setVisibility(View.VISIBLE);
//
//                for (User user: mPlayers) {
//                    if (user.getEmail().equals(player.getEmail())) {
//                        return;
//                    }
//                }
//
//                mPlayers.add(player);
//
//                mTeamPlayersRecyclerViewAdapter.setPlayers(mPlayers);
//
//                Map<String, Object> addUsers = new HashMap<>();
//
//                String teamPath = "team/" + mTeams.getTeamKey() + "/usersTeam/" + player.getPlayerKey();
//                addUsers.put(teamPath, true);
//                mDatabaseReference.updateChildren(addUsers);
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//            System.out.println("teste 2");
//        }
//    }

    @OnClick(R.id.add_to_team_btn)
    public void addPlayerToTeam(View view) {
        Boolean existente = false;

        for (User user: mPlayers) {
            if (user.getEmail().equals(mSearchedPlayer.getEmail())) {
                user.setActive(true);
                existente = true;
            }
        }

        if (!existente) {
            mPlayers.add(mSearchedPlayer);
        }

        Map<String, Object> addUsers = new HashMap<>();

        String teamPath = "team/" + mTeam.getTeamKey() + "/usersTeam/" + mSearchedPlayer.getPlayerKey();
        addUsers.put(teamPath, true);
        mDatabaseReference.updateChildren(addUsers);

        mTeamPlayersRecyclerViewAdapter.setPlayers(mPlayers);
    }

    @Override
    public void onListItemClick(int clickedItemId, Boolean actualState) {
        mPlayers.get(clickedItemId).setActive(actualState);
    }
}
