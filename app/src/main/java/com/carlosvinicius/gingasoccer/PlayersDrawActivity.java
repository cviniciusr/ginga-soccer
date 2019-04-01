package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.carlosvinicius.gingasoccer.adapters.DrawPlayersRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayersDrawActivity extends AppCompatActivity implements DrawPlayersRecyclerViewAdapter.ListItemClickListener {

    @BindView(R.id.players_for_draw_rv)
    RecyclerView mDrawPlayersRecyclerView;
    private DrawPlayersRecyclerViewAdapter mDrawPlayersRecyclerViewAdapter;

    @BindView(R.id.number_of_players_spinner)
    Spinner mNumberOfPlayersSpinner;

    private ArrayList<User> mPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_draw);

        ButterKnife.bind(this);

        mPlayers = new ArrayList<>();

        Intent intent = getIntent();

        if (intent.hasExtra(getResources().getString(R.string.players))) {
            List<User> players = (List<User>) intent.getSerializableExtra(getResources().getString(R.string.players));

            for (User player: players) {
                if (player.isActive()) {
                    player.setAttend(true);
                    mPlayers.add(player);
                }
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_players, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mNumberOfPlayersSpinner.setAdapter(adapter);

        mDrawPlayersRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mDrawPlayersRecyclerView.setHasFixedSize(true);
        mDrawPlayersRecyclerViewAdapter = new DrawPlayersRecyclerViewAdapter(mPlayers, this);
        mDrawPlayersRecyclerView.setAdapter(mDrawPlayersRecyclerViewAdapter);
    }

    @OnClick(R.id.draw_btn)
    public void draw(View view) {
        Integer numberOfPlayers = Integer.valueOf(mNumberOfPlayersSpinner.getSelectedItem().toString());

        Intent intent = new Intent(this, FormedTeamActivity.class);
        intent.putExtra(getResources().getString(R.string.players), mPlayers);
        intent.putExtra(getResources().getString(R.string.number_of_players), numberOfPlayers);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onListItemClick(int clickedItemId, Boolean actualState) {
        mPlayers.get(clickedItemId).setAttend(actualState);
    }
}
