package com.dtu.tournamate_v1.createNewTournament;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.HashSet;

/**
 * Created by Christian on 05-04-2015.
 */
public class  TournamentReady_frag extends Fragment implements View.OnClickListener {

    // Define graphical elements
    TextView selectedType_tv;
    Button start_b;
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

        selectedType_tv.setText(MyApplication.type);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(MyApplication.tournamentName);

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

        return rod;
    }

    public void onClick(View v) {
        if (v == start_b) {

            final ProgressDialog progress;
            progress = ProgressDialog.show(getActivity(), getString(R.string.tournamentResume_dialogHeader), getString(R.string.tournamentResume_dialogText), true);
            progress.setCancelable(false);
            progress.show();

            //Start tournament

            MyApplication.matchList.clear();
            MyApplication.matchesPlayed = 0;

            // Save tournament to Firebase

            Firebase tournamentRef = myFirebaseRef.child("Tournaments");
            Firebase newTournamentRef = tournamentRef.push();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date  = dateFormat.format(new Date());

            Tournament tournament = new Tournament();
            tournament.setName(MyApplication.tournamentName);
            tournament.setCreatedBy_uID(MyApplication.getUser().getU_ID());
            tournament.setCreatedBy(MyApplication.getUser().getFullName());
            tournament.setIsDone(false);
            tournament.setWinner("No winner yet");
            tournament.setCreatedAt(date);
            tournament.setType(MyApplication.type);
            tournament.setIsOpenToJoin(false);
            tournament.setT_ID(newTournamentRef.getKey());

            MyApplication.tournamentID_parse = tournament.getT_ID();
            tournament.setT_ID(newTournamentRef.getKey());


            // Save teams to Firebase

            Firebase teamsRef = myFirebaseRef.child("Teams");

            for (Team t : MyApplication.teams){
                Firebase newTeamRef = teamsRef.push();
                t.setTeamID(newTeamRef.getKey());
                t.setTournamentID(MyApplication.tournamentID_parse);
                newTeamRef.setValue(t);
            }

            // Make matches

            if (MyApplication.type.equals("Round Robin")) {
                RoundRobin_logic rr;
                rr = new RoundRobin_logic();
                rr.createMatches();
            }
            else if (MyApplication.type.equals("Single Elimination")){
                SingleElimination_logic se;
                se = new SingleElimination_logic();
                try {
                    se.createMatches();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Save matches to Firebase

            Firebase matchesRef = myFirebaseRef.child("Matches");

            for (Match m : MyApplication.matchList) {
                Firebase newMatchesRef = matchesRef.push();
                m.setMatchID(newMatchesRef.getKey());
                m.setTournamentID(MyApplication.tournamentID_parse);
                m.setT1ID(m.getT1().getTeamID());
                m.setT2ID(m.getT2().getTeamID());

                Log.d("Firebase", "match id: " + m.getMatchID()+ " t1 id lenght: "+ m.getT1ID().length()+ " t2 id lenght: "+m.getT2ID().length());
                if (m.getT1ID().length()>1){
                    m.setTeamsAdded(1);
                }
                if (m.getT2ID().length()>1){
                    m.setTeamsAdded(2);
                }
                newMatchesRef.setValue(m);
            }

            tournament.setNumberOfMatches(MyApplication.matchList.size());
            newTournamentRef.setValue(tournament);
            Log.d("Firebase", "Tournament id: " + tournament.getT_ID());
            saveTournamnetToUser();
            progress.dismiss();

            ActiveMatchScore_frag fragment = new ActiveMatchScore_frag();
            getFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();

        }
    }

    public void saveTournamnetToUser(){

        int index = MyApplication.getUser().getStoredTournamentsID().size();
        MyApplication.getUser().getStoredTournamentsID().add(index,MyApplication.tournamentID_parse);

        SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        prefs.edit().putStringSet("tournaments", new HashSet<String>(MyApplication.getUser().getStoredTournamentsID())).commit();

        Firebase userRef = myFirebaseRef.child(MyApplication.usersString);
        userRef.child(MyApplication.getUser().getU_ID()).setValue(MyApplication.getUser());

    }

}
