package com.carlosvinicius.gingasoccer.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.carlosvinicius.gingasoccer.R;
import com.carlosvinicius.gingasoccer.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawPlayersRecyclerViewAdapter extends RecyclerView.Adapter<DrawPlayersRecyclerViewAdapter.ViewHolder> {

    private List<User> mPlayers;
    private final ListItemClickListener mOnClickListener;

    public DrawPlayersRecyclerViewAdapter(List<User> items, ListItemClickListener mOnClickListener) {
        this.mPlayers = items;
        this.mOnClickListener = mOnClickListener;
    }

    public interface  ListItemClickListener {
        void onListItemClick(int clickedItemId, Boolean actualState);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.draw_players_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User player = mPlayers.get(position);

        holder.drawPlayerNameTextView.setText(player.getNickname());
        holder.attendSwitch.setChecked(player.isAttend());
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

        @BindView(R.id.draw_player_name_tv)
        TextView drawPlayerNameTextView;

        @BindView(R.id.attend_switch)
        Switch attendSwitch;

        Boolean actualState;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
            view.setOnClickListener(this);

            attendSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                actualState = isChecked;
                view.callOnClick();
            });
        }

        public void onClick(View v) {
            Log.i("Clicked", "onClick: ");

            int adapterPosition = getAdapterPosition();

            mOnClickListener.onListItemClick(adapterPosition, actualState);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + drawPlayerNameTextView.getText() + "'";
        }
    }
}
