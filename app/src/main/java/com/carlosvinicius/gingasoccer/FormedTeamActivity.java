package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.carlosvinicius.gingasoccer.adapters.FormedTeamRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormedTeamActivity extends AppCompatActivity {

    @BindView(R.id.formed_teams_rv)
    RecyclerView mFormedTeamsRecyclerView;
    private FormedTeamRecyclerViewAdapter mFormedTeamsRecyclerViewAdapter;

    Integer mNumberOfPlayers;

    List<String> mTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formed_team);

        ButterKnife.bind(this);

        mTeams = new ArrayList<>();

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getResources().getString(R.string.number_of_players))) {
                mNumberOfPlayers = intent.getIntExtra(getResources().getString(R.string.number_of_players), 0);
            }

            if (intent.hasExtra(getResources().getString(R.string.players))) {
                List<User> players = (List<User>) intent.getSerializableExtra(getResources().getString(R.string.players));

                List<User> attendedPlayers = new ArrayList<>();

                for (User player: players) {
                    if (player.isAttend()) {
                        attendedPlayers.add(player);
                    }
                }

                int numberOfItens = attendedPlayers.size();
                int randomMax = attendedPlayers.size();

                Random random = new Random();

                String team = "";

                for (int controle = 0; controle < numberOfItens; controle++) {
                    int randomNum = random.nextInt(randomMax);

                    if (controle > 0 && (controle % mNumberOfPlayers) == 0) {
                        mTeams.add(team);
                        team = "";
                    }

                    if (!"".equals(team)) {
                        team = team + "\n";
                    }

                    team = team + players.get(randomNum).getNickname();

                    players.remove(randomNum);
                    randomMax--;
                }

                mTeams.add(team);
            }
        }

        mFormedTeamsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mFormedTeamsRecyclerView.setHasFixedSize(true);
        mFormedTeamsRecyclerViewAdapter = new FormedTeamRecyclerViewAdapter(mTeams);
        mFormedTeamsRecyclerView.setAdapter(mFormedTeamsRecyclerViewAdapter);
    }
}
