package com.dtu.tournamate_v1.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;

import java.util.ArrayList;

/**
 * Created by ce on 04-05-2016.
 */
public class MakeTeamAdapter extends RecyclerView.Adapter<MakeTeamAdapter.ViewHolder> {

    ArrayList<Player> dataSet = new ArrayList<>();
    ArrayList<Team> tempTeamList = new ArrayList<>();
    int teamSize;
    Fragment fragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mView;
        TextView teamName;
        ListView playerList;


        public ViewHolder(View v) {
            super(v);
            teamName = (TextView) v.findViewById(R.id.make_team_teamName);
            playerList = (ListView) v.findViewById(R.id.make_team_playerList);
            mView = v;
        }
    }

    public MakeTeamAdapter(ArrayList<Player> dataSet, int teamSize, Fragment fragment) {
        this.dataSet = dataSet;
        this.teamSize = teamSize;
        this.fragment = fragment;

        for (int i = 0; i<dataSet.size();i++){
            Team t = new Team();
            t.addTeamMember(dataSet.get(i).getName());
            tempTeamList.add(t);
        }

    }

    @Override
    public MakeTeamAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.make_team_element, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.teamName.setText("Team # " + (position+1));

        ArrayAdapter adapter = new ArrayAdapter(fragment.getActivity(),R.layout.make_team_player_element,R.id.make_team_playerName,dataSet) {
            @Override
            public View getView(int i, View cachedView, ViewGroup parent) {
                View view = super.getView(i, cachedView, parent);

                TextView name = (TextView) view.findViewById(R.id.make_team_playerName);
                name.setText(dataSet.get(position).getName());
                Log.d("Player","Team: "+(position+1)+" player name: "+ name.getText().toString() );
                return view;
            }
        };

        /**ViewGroup.LayoutParams params = viewHolder.playerList.getLayoutParams();
        params.height = (viewHolder.playerList.getDividerHeight() * (teamSize));
        viewHolder.playerList.setLayoutParams(params);
         **/
        viewHolder.playerList.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return tempTeamList.size();
    }

}