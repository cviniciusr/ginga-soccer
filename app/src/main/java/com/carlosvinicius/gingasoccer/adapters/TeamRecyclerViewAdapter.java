package com.carlosvinicius.gingasoccer.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlosvinicius.gingasoccer.R;
import com.carlosvinicius.gingasoccer.models.Team;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamRecyclerViewAdapter extends RecyclerView.Adapter<TeamRecyclerViewAdapter.ViewHolder> {

    private List<Team> mTeamList;
    private final ListItemClickListener mOnClickListener;

    public TeamRecyclerViewAdapter(List<Team> items, ListItemClickListener mOnClickListener) {
        this.mTeamList = items;
        this.mOnClickListener = mOnClickListener;
    }

    public interface  ListItemClickListener {
        void onListItemClick(int clickedItemId);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Team team = mTeamList.get(position);

        holder.teamNameTextView.setText(team.getName());

        String time = team.getStartTime() + " - " + team.getEndTime();

        holder.timeTextView.setText(time);
        holder.addressTestView.setText(team.getAddress());
    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }

    public void setTeamList(List<Team> teamList) {
        this.mTeamList = teamList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;

        @BindView(R.id.team_name_tv)
        TextView teamNameTextView;

        @BindView(R.id.time_tv)
        TextView timeTextView;

        @BindView(R.id.address_tv)
        TextView addressTestView;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);
//            cardView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Log.i("Clicked", "onClick: ");

            int adapterPosition = getAdapterPosition();

            mOnClickListener.onListItemClick(adapterPosition);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + teamNameTextView.getText() + "'";
        }
    }
}
