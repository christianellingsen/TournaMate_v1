package com.dtu.tournamate_v1.activeTournament;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dtu.tournamate_v1.MainMenu_akt;
import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.firebase.client.Firebase;
/**import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;**/

import java.util.ArrayList;

/**
 * Created by Christian on 20-04-2015.
 */
public class TournamentWinner_frag extends Fragment implements View.OnClickListener {

    private TextView winnerName_tv;
    private Button rank_b, matchList_b, end_b;
    private Team winner;

    private ArrayList<Team> teams;
    private ArrayList<String> teamsStringList;
    private ArrayList<Match> matches;
    private ArrayList<String> matchesStringList;
    private View rod;

    private FrameLayout frame;

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rod = i.inflate(R.layout.tournament_winner, container, false);

        matchesStringList = new ArrayList<>();
        teams = MyApplication.teams;
        matches = MyApplication.matchList;
        teamsStringList = new ArrayList<>();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Result");

        winnerName_tv = (TextView) rod.findViewById(R.id.textViewWinner_teamName);
        rank_b = (Button) rod.findViewById(R.id.buttonWinner_rank);
        matchList_b = (Button) rod.findViewById(R.id.buttonWinner_matchList);
        end_b = (Button) rod.findViewById(R.id.buttonWinner_end);
        frame = (FrameLayout) rod.findViewById(R.id.winner_frame);

        getFragmentManager().beginTransaction()
                .replace(R.id.winner_frame, new RankList_frag())
                .addToBackStack(null)
                .commit();


        rank_b.setOnClickListener(this);
        matchList_b.setOnClickListener(this);
        end_b.setOnClickListener(this);

        MyApplication.rankTeams();

        winner = MyApplication.teams.get(0);

        winnerName_tv.setText(winner.getTeamName());

        Firebase tournamentRef = myFirebaseRef.child("Tournaments");
        Firebase updateTournamentRef = tournamentRef.child(MyApplication.getActiveTournament().getT_ID());
        updateTournamentRef.child("isDone").setValue(true);
        updateTournamentRef.child("winner").setValue(winner.getTeamName());

        return rod;

    }

    @Override
    public void onClick(View v){
        if (v==rank_b){
            getFragmentManager().beginTransaction()
                    .replace(R.id.winner_frame, new RankList_frag())
                    .addToBackStack(null)
                    .commit();

        }
        else if (v==matchList_b){
            getFragmentManager().beginTransaction()
                    .replace(R.id.winner_frame, new MatchList_frag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (v==end_b){

            Intent i = new Intent(getActivity(), MainMenu_akt.class);
            startActivity(i);
        }
    }

}
