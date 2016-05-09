package com.dtu.tournamate_v1.createNewTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dtu.tournamate_v1.Adapter.AutofitRecyclerView;
import com.dtu.tournamate_v1.Adapter.MakeTeamAdapter;
import com.dtu.tournamate_v1.Adapter.MarginDecorator;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;

import java.util.ArrayList;

/**
 * Created by ce on 04-05-2016.
 */
public class MakeTeams2_frag extends Fragment{

    ArrayList<Player> players;
    ArrayList<String> selectedPlayers;
    Button b1, b2, sizePlus, sizeMinus;
    TextView textView_tv;
    int teamSize = 1;
    int numberOfTeams = 2;
    AutofitRecyclerView teamsRecyclerView;
    MakeTeamAdapter adapter;

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.make_teams2, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Make teams");

        teamsRecyclerView = (AutofitRecyclerView) root.findViewById(R.id.make_teams_teamsRecyclerview);
        sizeMinus = (Button) root.findViewById(R.id.teamSizeMinusButton);
        sizePlus = (Button) root.findViewById(R.id.teamSizePlusButton);
        textView_tv = (TextView) root.findViewById(R.id.make_team_teamSize);

        MyApplication.teams.clear();

        players = new ArrayList<>();
        selectedPlayers = new ArrayList<>(MyApplication.selectedPlayerSet);
        for (String name : selectedPlayers){
            Player p = new Player();
            p.setName(name);
            players.add(p);
        }

        numberOfTeams = players.size()/teamSize;
        Log.d("Players","Calc number of teams:" + numberOfTeams);

        adapter = new MakeTeamAdapter(players,teamSize,this);
        teamsRecyclerView.addItemDecoration(new MarginDecorator(getActivity()));
        teamsRecyclerView.setAdapter(adapter);


        sizePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamSize++;
                textView_tv.setText(""+teamSize);
                adapter = new MakeTeamAdapter(players,teamSize,getParentFragment());
                adapter.notifyDataSetChanged();
            }
        });

        sizeMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamSize>1) {
                    teamSize--;
                    textView_tv.setText(""+teamSize);
                    adapter = new MakeTeamAdapter(players,teamSize,getParentFragment());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return root;
    }
}
