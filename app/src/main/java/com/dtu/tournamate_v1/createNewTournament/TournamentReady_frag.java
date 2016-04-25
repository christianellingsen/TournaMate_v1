package com.dtu.tournamate_v1.createNewTournament;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dtu.tournamate_v1.Logic.RoundRobin_logic;
import com.dtu.tournamate_v1.Logic.SingleElimination_logic;
import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.dtu.tournamate_v1.Tournament;
import com.dtu.tournamate_v1.activeTournament.ActiveMatchScore_frag;
import com.firebase.client.Firebase;
//import com.parse.ParseException;
//import com.parse.SaveCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Christian on 05-04-2015.
 */
public class  TournamentReady_frag extends Fragment implements View.OnClickListener {

    // Define graphical elements
    TextView selectedType_tv;
    Button start_b;
    ToggleButton onOffLine_b;
    ListView lv;
    View rod;
    Boolean tCreated = false;
    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.tournament_ready, container, false);

        lv = (ListView) rod.findViewById(R.id.listViewTeams);
        selectedType_tv = (TextView) rod.findViewById(R.id.textViewSelectedType);
        start_b = (Button) rod.findViewById(R.id.buttonStart);
        onOffLine_b = (ToggleButton) rod.findViewById(R.id.toggleButtonONOFFline);


        selectedType_tv.setText(MyApplication.type);


        MyApplication.activeMatch = 1;
        MyApplication.matchesPlayed = 0;
        MyApplication.isDone = false;
        MyApplication.tournamentID_parse = "";

        ArrayList<String> teamNames = new ArrayList<>();

        for (Team t : MyApplication.teams) {
            String teamName = t.getTeamName();
            teamNames.add(teamName);
        }
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
        onOffLine_b.setOnClickListener(this);

        return rod;
    }

    public void onClick(View v) {
        if (v == start_b) {

            final ProgressDialog progress;
            progress = ProgressDialog.show(getActivity(), getString(R.string.tournamentResume_dialogHeader), getString(R.string.tournamentResume_dialogText), true);
            progress.setCancelable(false);
            progress.show();

            if (onOffLine_b.isChecked()) {

                MyApplication.isOnline = true;

            }
            else {
                MyApplication.isOnline = false;
            }

            //Start tournament

            MyApplication.matchList.clear();
            MyApplication.matchesPlayed = 0;

            if (MyApplication.type.equals("Round Robin")) {
                RoundRobin_logic rr;
                rr = new RoundRobin_logic();
                rr.createMatches();
            }
            else {
                SingleElimination_logic se;
                se = new SingleElimination_logic();
                try {
                    se.createMatches();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Save tournament to Firebase

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date  = dateFormat.format(new Date());

            Tournament tournament = new Tournament();
            tournament.setName(MyApplication.tournamentName);
            tournament.setIsDone(false);
            tournament.setWinner("No winner yet");
            tournament.setCreatedAt(date);
            tournament.setType(MyApplication.type);
            tournament.setNumberOfMatches(MyApplication.matchList.size());

            Firebase tournamentRef = myFirebaseRef.child("Tournaments");
            Firebase newTournamentRef = tournamentRef.push();
            tournament.setObjectID(newTournamentRef.getKey());
            newTournamentRef.setValue(tournament);
            MyApplication.tournamentID_parse = tournament.getObjectID();
            Log.d("Firebase", "Tournament id: " + tournament.getObjectID());

            // Save matches and teams to Firebase

            Firebase matchesRef = myFirebaseRef.child("Matches");
            Firebase teamsRef = myFirebaseRef.child("Teams");

            for (Match m : MyApplication.matchList) {
                Firebase newMatchesRef = matchesRef.push();
                m.setMatchID(newMatchesRef.getKey());
                m.setTournamentID(MyApplication.tournamentID_parse);
                newMatchesRef.setValue(m);

                if(m.getTeamsAdded()>=1){
                    Log.d("Firebase", "Team 1 added: " + m.getT1().getTeamName());
                    Firebase newTeamRef = teamsRef.push();
                    newTeamRef.push();
                    m.getT1().setMatchID(m.getMatchID());
                    newTeamRef.setValue(m.getT1());
                }
                if (m.getTeamsAdded()>1){
                    Log.d("Firebase", "Team 2 added: " + m.getT2().getTeamName());
                    Firebase newTeamRef = teamsRef.push();
                    newTeamRef.push();
                    newTeamRef.push();
                    m.getT2().setMatchID(m.getMatchID());
                    newTeamRef.setValue(m.getT2());
                }

            }

            progress.dismiss();

            ActiveMatchScore_frag fragment = new ActiveMatchScore_frag();
            getFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();

        }
    }


}
