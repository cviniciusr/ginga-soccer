package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.carlosvinicius.gingasoccer.adapters.TeamRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.models.Team;
import com.carlosvinicius.gingasoccer.models.User;

import java.util.List;

import butterknife.BindView;

public class UserInfoActivity extends AppCompatActivity implements TeamRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = UserInfoActivity.class.getSimpleName();

    private User user;
    private List<Team> mTeamList;

    @BindView(R.id.teams_rv)
    RecyclerView mTeamsRecyclerView;
    private TeamRecyclerViewAdapter mTeamRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getResources().getString(R.string.user))) {
                user = (User) intent.getSerializableExtra(getResources().getString(R.string.user));

                Log.d(TAG, "User: " + user.getFullname());
            }
        }

        mTeamsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mTeamsRecyclerView.setHasFixedSize(true);
        mTeamRecyclerViewAdapter = new TeamRecyclerViewAdapter(mTeamList, this);
        mTeamsRecyclerView.setAdapter(mTeamRecyclerViewAdapter);
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
