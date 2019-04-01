package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.carlosvinicius.gingasoccer.adapters.FormedTeamRecyclerViewAdapter;
import com.carlosvinicius.gingasoccer.appwidget.GingaSoccerWidgetProvider;
import com.carlosvinicius.gingasoccer.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormedTeamActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

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
