package com.carlosvinicius.gingasoccer.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlosvinicius.gingasoccer.R;
import com.carlosvinicius.gingasoccer.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormedTeamRecyclerViewAdapter extends RecyclerView.Adapter<FormedTeamRecyclerViewAdapter.ViewHolder> {

    private List<String> mTeams;

    public FormedTeamRecyclerViewAdapter(List<String> items) {
        this.mTeams = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.formed_team_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String team = mTeams.get(position);

        holder.formedTeamTextView.setText(team);
    }

    @Override
    public int getItemCount() {
        return mTeams.size();
    }

    public void setPlayers(List<String> teams) {
        this.mTeams = teams;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.formed_team_tv)
        TextView formedTeamTextView;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }

        public void onClick(View v) {
            Log.i("Clicked", "onClick: ");

            int adapterPosition = getAdapterPosition();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + formedTeamTextView.getText() + "'";
        }
    }
}
