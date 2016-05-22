package com.dtu.tournamate_v1.createNewTournament;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dtu.tournamate_v1.Logic.RoundRobin_logic;
import com.dtu.tournamate_v1.Logic.SingleElimination_logic;
import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.dtu.tournamate_v1.activeTournament.ActiveMatchScore_frag;
import com.firebase.client.Firebase;
//import com.parse.ParseException;
//import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Christian on 05-04-2015.
 */
public class  TournamentReady_frag extends Fragment implements View.OnClickListener {

    // Define graphical elements
    TextView tournamanetName_tv, selectedType_tv, numberOfPlayers_tv, numberOfTeams_tv;
    Button start_b,editTeams_b;
    ListView lv;
    View rod;
    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.tournament_ready, container, false);

        lv = (ListView) rod.findViewById(R.id.listViewTeams);
        tournamanetName_tv = (TextView) rod.findViewById(R.id.tournamnet_ready_name);
        selectedType_tv = (TextView) rod.findViewById(R.id.textViewSelectedType);
        numberOfPlayers_tv = (TextView) rod.findViewById(R.id.tournament_ready_numberOfPlayers_tv);
        numberOfTeams_tv = (TextView) rod.findViewById(R.id.tournament_ready_numberOfTeams_tv);
        start_b = (Button) rod.findViewById(R.id.buttonStart);
        editTeams_b = (Button) rod.findViewById(R.id.tournament_ready_edit_teams_b);

        tournamanetName_tv.setText(MyApplication.getActiveTournament().getName());
        selectedType_tv.setText(MyApplication.getActiveTournament().getType());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Start tournament");

        ArrayList<String> teamNames = new ArrayList<>();
        int playerCounter = 0;

        for (Team t : MyApplication.teams) {
            String teamName = t.getTeamName();
            playerCounter = playerCounter + t.getTeamMembers().size();
            teamNames.add(teamName);
        }

        numberOfPlayers_tv.setText(""+playerCounter);
        numberOfTeams_tv.setText(""+MyApplication.teams.size());

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.team_tv, R.id.textViewTeamName, teamNames) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);

                TextView teamNumber = (TextView) view.findViewById(R.id.textViewTeamNumber);
                teamNumber.setText("" + (position + 1));

                return view;
            }
        };
        lv.setAdapter(adapter);

        start_b.setOnClickListener(this);
        editTeams_b.setOnClickListener(this);

        return rod;
    }

    public void onClick(View v) {
        if (v == start_b) {

            if (MyApplication.teams.size() < 2) {
                Snackbar snackbar = Snackbar
                        .make(getView(), "Need at least 2 teams", Snackbar.LENGTH_LONG)
                        .setAction("ADD TEAMS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MakeTeams2_frag fragment = new MakeTeams2_frag();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.main_frame, fragment)
                                        .commit();
                            }
                        });

                snackbar.show();
            } else {
                final ProgressDialog progress;
                progress = ProgressDialog.show(getActivity(), getString(R.string.tournamentResume_dialogHeader), getString(R.string.tournamentResume_dialogText), true);
                progress.setCancelable(false);
                progress.show();

                // Delete dummy match in Firebase
                Firebase matchesRef = myFirebaseRef.child("Matches");
                matchesRef.child(MyApplication.matchList.get(0).getMatchID()).setValue(null);
                Log.d("TournamentReady", "Deleting: " + MyApplication.matchList.get(0).getMatchID());

                //Start tournament
                MyApplication.matchList.clear();
                MyApplication.matchesPlayed = 0;

                // Save tournament to Firebase
                Firebase tournamentRef = myFirebaseRef.child(MyApplication.tournamentsString);
                Firebase tRef = tournamentRef.child(MyApplication.getActiveTournament().getT_ID());
                Log.d("TournamantReady", "T ref: " + MyApplication.getActiveTournament().getT_ID());

                tRef.child("isOpenToJoin").setValue(false);
                tRef.child("isStarted").setValue(true);


                //Delete dummy match
                if (MyApplication.matchList.size() > 0) {
                    Firebase ref = new Firebase(MyApplication.firebase_URL);
                    for (Match m : MyApplication.matchList) {
                        Firebase matchRef = ref.child(MyApplication.matchesString).child(m.getMatchID());
                        matchRef.setValue(null);
                    }
                }
                MyApplication.matchList.clear();

                // Make matches
                if (MyApplication.getActiveTournament().getType().equals("Round Robin")) {
                    RoundRobin_logic rr;
                    rr = new RoundRobin_logic();
                    rr.createMatches();
                } else if (MyApplication.getActiveTournament().getType().equals("Single Elimination")) {
                    SingleElimination_logic se;
                    se = new SingleElimination_logic();
                    try {
                        se.createMatches();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                // Save matches to Firebase

                for (Match m : MyApplication.matchList) {
                    Firebase newMatchesRef = matchesRef.push();
                    m.setMatchID(newMatchesRef.getKey());
                    m.setTournamentID(MyApplication.getActiveTournament().getT_ID());
                    m.setT1ID(m.getT1().getTeamID());
                    m.setT2ID(m.getT2().getTeamID());

                    Log.d("Firebase", "match id: " + m.getMatchID() + " t1 id lenght: " + m.getT1ID().length() + " t2 id lenght: " + m.getT2ID().length());
                    if (m.getT1ID().length() > 1) {
                        m.setTeamsAdded(1);
                    }
                    if (m.getT2ID().length() > 1) {
                        m.setTeamsAdded(2);
                    }
                    newMatchesRef.setValue(m);
                }
                tRef.child("numberOfMatches").setValue(MyApplication.matchList.size());
                //tournament.setNumberOfMatches(MyApplication.matchList.size());
                //newTournamentRef.setValue(tournament);
                //Log.d("Firebase", "Tournament id: " + tournament.getT_ID());
                progress.dismiss();

                ActiveMatchScore_frag fragment = new ActiveMatchScore_frag();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();

            }
        }
        else {


            MakeTeams2_frag fragment = new MakeTeams2_frag();
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();

        }
    }


}
