package com.carlosvinicius.gingasoccer.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.carlosvinicius.gingasoccer.R;
import com.carlosvinicius.gingasoccer.models.Team;
import com.carlosvinicius.gingasoccer.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamPlayersRecyclerViewAdapter extends RecyclerView.Adapter<TeamPlayersRecyclerViewAdapter.ViewHolder> {

    private DatabaseReference databaseReference;

    private List<User> mPlayers;
    private String teamKey;
    private final ListItemClickListener mOnClickListener;

    public TeamPlayersRecyclerViewAdapter(List<User> items, String teamKey,
                                          ListItemClickListener mOnClickListener,
                                          DatabaseReference databaseReference) {
        this.mPlayers = items;
        this.teamKey = teamKey;
        this.mOnClickListener = mOnClickListener;
        this.databaseReference = databaseReference;
    }

    public interface  ListItemClickListener {
        void onListItemClick(int clickedItemId);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_players_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User player = mPlayers.get(position);

        holder.playerNameTeamInfoTextView.setText(player.getNickname());
        holder.actualState = player.getActive();
        holder.activeSwitch.setChecked(player.getActive());
        holder.playerKey = player.getPlayerKey();
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public void setPlayers(List<User> players) {
        this.mPlayers = players;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.player_name_team_info_tv)
        TextView playerNameTeamInfoTextView;

        @BindView(R.id.active_switch)
        Switch activeSwitch;

        Boolean actualState;

        String playerKey;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
            view.setOnClickListener(this);

            activeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (actualState != isChecked) {
                    Map<String, Object> updateUser = new HashMap<>();

                    String teamPath = "team/" + teamKey + "/usersTeam/" + playerKey;
                    updateUser.put(teamPath, isChecked);
                    databaseReference.updateChildren(updateUser);

                    actualState = isChecked;
                }
            });
        }

        public void onClick(View v) {
            Log.i("Clicked", "onClick: ");

            int adapterPosition = getAdapterPosition();

            mOnClickListener.onListItemClick(adapterPosition);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + playerNameTeamInfoTextView.getText() + "'";
        }
    }
}
