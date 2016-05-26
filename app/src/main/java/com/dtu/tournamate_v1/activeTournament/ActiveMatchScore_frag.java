package com.dtu.tournamate_v1.activeTournament;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.firebase.client.Firebase;
//import com.parse.FindCallback;
//import com.parse.GetCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Christian on 19-04-2015.
 */
public class ActiveMatchScore_frag extends Fragment implements View.OnClickListener {

    ArrayList<Match> matches;
    View rod;
    private TextView matchNumber_tv, teamX_tv, teamY_tv, teamXScore_tv, teamYScore_tv, matchTitle_tv;
    private Button teamXPlus_b, teamXMinus_b, teamYMinus_b, teamYPlus_b, rank_b, matchlist_b, next_b;
    private int activeMatchNumber;
    private Match m, nextMatch;
    private Team t1, t2;
    private boolean readyToPlay = false;
    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase matchesRef = myFirebaseRef.child(MyApplication.matchesString);
    Firebase teamsRef = myFirebaseRef.child(MyApplication.teamsString);
    Firebase activeMatchRef, nextMatchRef;
    Firebase t1Ref, t2Ref;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rod = i.inflate(R.layout.active_match_score, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(MyApplication.getActiveTournament().getName());

        matchNumber_tv = (TextView) rod.findViewById(R.id.textViewMatchNumber);
        matchTitle_tv = (TextView) rod.findViewById(R.id.textView_matchtitle);
        teamX_tv = (TextView) rod.findViewById(R.id.textViewTeamX);
        teamY_tv = (TextView) rod.findViewById(R.id.textViewTeamY);
        teamXScore_tv = (TextView) rod.findViewById(R.id.textViewTeamXScore);
        teamYScore_tv = (TextView) rod.findViewById(R.id.textViewTeamYScore);
        teamXPlus_b = (Button) rod.findViewById(R.id.buttonTeamXPlus);
        teamYPlus_b = (Button) rod.findViewById(R.id.buttonTeamYPlus);
        teamXMinus_b = (Button) rod.findViewById(R.id.buttonTeamXMinus);
        teamYMinus_b = (Button) rod.findViewById(R.id.buttonTeamYMinus);
        rank_b = (Button) rod.findViewById(R.id.buttonRank);
        matchlist_b = (Button) rod.findViewById(R.id.buttonViewAllMatches);
        next_b = (Button) rod.findViewById(R.id.buttonNextMatch);

        teamXPlus_b.setOnClickListener(this);
        teamXMinus_b.setOnClickListener(this);
        teamYPlus_b.setOnClickListener(this);
        teamYMinus_b.setOnClickListener(this);
        rank_b.setOnClickListener(this);
        matchlist_b.setOnClickListener(this);
        next_b.setOnClickListener(this);
        teamX_tv.setOnClickListener(this);
        teamY_tv.setOnClickListener(this);

        matches = MyApplication.matchList;
        t1 = new Team();
        t2 = new Team();
        nextMatch = new Match();


        if (MyApplication.getActiveTournament().getType().equals("Round Robin")) {
            matchTitle_tv.setVisibility(View.GONE);
            readyToPlay = true;
        } else {
            matchTitle_tv.setVisibility(View.VISIBLE);
        }

        //Log.d("Debug", "Active match: " + MyApplication.activeMatch);
        //Log.d("Debug", "Matches played: " + MyApplication.matchesPlayed);

        if (MyApplication.matchesPlayed + 1 != MyApplication.activeMatch) {
            activeMatchNumber = MyApplication.activeMatch;
        } else {
            activeMatchNumber = (MyApplication.matchesPlayed + 1);
            if (MyApplication.activeMatch < MyApplication.matchList.size()) {
                MyApplication.activeMatch++;
            }
        }


        if (activeMatchNumber <= matches.size()) {
            m = matches.get(activeMatchNumber - 1);

            Log.d("Active match","size of matchlist: "+matches.size());
            for (Team t : MyApplication.teams){
                Log.d("Active match","Set teams: t name: "+t.getTeamName()+" tID: "+t.getTeamID()+" m t1 ID: "+m.getT1ID()+" or "+ m.getT2ID());

                if (m.getT1ID().trim().equals(t.getTeamID().trim())){
                    t1 = t;
                    Log.d("Active match","team1 found "+ t1.getTeamName());
                }
                else if (m.getT2ID().trim().equals(t.getTeamID().trim())){
                    t2=t;
                    Log.d("Active match","team2 found "+t2.getTeamName());
                }
            }


            matchNumber_tv.setText(getString(R.string.activeTournament_Match) + m.getMatchNumber());
            matchTitle_tv.setText(m.getMatchTitle());
            teamX_tv.setText(t1.getTeamName());
            teamY_tv.setText(t2.getTeamName());
        }
        if (MyApplication.matchesPlayed == matches.size() - 2) {
            //next_b.setText(getString(R.string.activeTournament_playNext));
        } else if (MyApplication.matchesPlayed == matches.size() - 1) {
            //next_b.setText(getString(R.string.activeTournament_done));
        }

        if (MyApplication.getActiveTournament().getIsDone()) {
            //next_b.setText(getString(R.string.activeTournament_seeWinner));
        }

        if (t1.getTeamName().length()>0 && t2.getTeamName().length()>0){
            readyToPlay = true;
            Log.d("RR debug","match ready to play");
        }
        else {
            //next_b.setText("Not ready yet");
            next_b.setBackground(getResources().getDrawable(R.drawable.icon_next_match_red));
            next_b.setClickable(false);
        }


        teamXScore_tv.setText("" + m.getScoreT1());
        teamYScore_tv.setText("" + m.getScoreT2());


        if (MyApplication.getActiveTournament().getType().equals(MyApplication.singleEliminationString)) {
            nextMatch = matches.get(m.getNextMatchNumber() - 1);
        }

        activeMatchRef = matchesRef.child(m.getMatchID());
        nextMatchRef = matchesRef.child(nextMatch.getMatchID());

        t1Ref = teamsRef.child(t1.getTeamID());
        t2Ref = teamsRef.child(t2.getTeamID());

        return rod;
    }

    public void updateScore(String team, int newScore) {
        if ("t1".equals(team)) {
            m.setScoreT1(newScore);
            teamXScore_tv.setText("" + newScore);
        } else {
            m.setScoreT2(newScore);
            teamYScore_tv.setText("" + newScore);
        }
    }


    @Override
    public void onClick(View v) {

        if (v == rank_b) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new RankList_frag())
                    .addToBackStack(null)
                    .commit();
        } else if (v == matchlist_b) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new MatchList_frag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (v==teamX_tv){
            if (teamX_tv.getText().equals(m.getT1().getTeamName())){
                teamX_tv.setText(m.getT1().getTeamMembersAsString());
            }
            else {
                teamX_tv.setText(m.getT1().getTeamName());
            }
        }
        else if (v==teamY_tv){
            if (teamY_tv.getText().equals(m.getT2().getTeamName())){
                teamY_tv.setText(m.getT2().getTeamMembersAsString());
            }
            else {
                teamY_tv.setText(m.getT2().getTeamName());
            }
        }

        else if (v == next_b) {
            if (MyApplication.getActiveTournament().getIsDone()){
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new TournamentWinner_frag())
                        .addToBackStack(null)
                        .commit();
            }
            else {


                if (m.getScoreT1() == m.getScoreT2() && MyApplication.getActiveTournament().getType().equals("Single Elimination")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false)
                            .setTitle("Someone must be ELIMINATED")
                            .setMessage("This is a elimination tournament. Make sure that " +
                                    "the match is not draw")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //All of the fun happens inside the CustomListener now.
                                    //I had to move it to enable data validation.
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    if (!m.isPlayed()) {
                        MyApplication.matchesPlayed++;
                    }

                    if (m.getScoreT1() > m.getScoreT2()) {
                        m.setWinner(t1.getTeamName());
                        t1.matchResult("won");
                        t2.matchResult("lost");
                        t1.addToOverAllScore(3);
                        t2.addToOverAllScore(0);
                        if (m.getMatchNumber() < MyApplication.matchList.size() && MyApplication.getActiveTournament().getType().equals("Single Elimination")) {
                            if (!m.isPlayed()) {
                                Log.d("Debug", "Match not played before");
                                if (nextMatch.getTeamsAdded() == 0) {
                                    nextMatch.setT1(m.getT1());
                                    nextMatch.setT1ID(m.getT1ID());
                                    nextMatch.setTeamsAdded(1);
                                    Log.d("Debug", "case 1");
                                } else {
                                    nextMatch.setT2(m.getT1());
                                    nextMatch.setT2ID(m.getT1ID());
                                    nextMatch.setTeamsAdded(2);
                                    Log.d("Debug", "case 2");
                                }
                            } else {
                                Log.d("Debug", "Match played before");
                                if (nextMatch.getT1().getTeamName().equals(m.getT2().getTeamName())) {
                                    nextMatch.setT1(m.getT1());
                                    nextMatch.setT1ID(m.getT1ID());
                                    Log.d("Debug", "T1 overwrite t1");
                                } else {
                                    nextMatch.setT2(m.getT1());
                                    nextMatch.setT2ID(m.getT1ID());
                                    Log.d("Debug", "T1 overwrite t2");
                                }
                            }
                        }
                        Log.d("Debug", m.getT1().getTeamName() + " won");

                    } else if (m.getScoreT1() < m.getScoreT2()) {
                        m.setWinner(t2.getTeamName());
                        t1.matchResult("lost");
                        t2.matchResult("won");
                        t1.addToOverAllScore(0);
                        t2.addToOverAllScore(3);

                        if (m.getMatchNumber() < MyApplication.matchList.size()) {
                            if (!m.isPlayed()) {
                                Log.d("Debug", "Match not played before");
                                if (nextMatch.getTeamsAdded() == 0) {
                                    nextMatch.setT1(m.getT2());
                                    nextMatch.setT1ID(m.getT2ID());
                                    nextMatch.setTeamsAdded(1);
                                    Log.d("Debug", "case 3");
                                } else {
                                    nextMatch.setT2(m.getT2());
                                    nextMatch.setT2ID(m.getT2ID());
                                    nextMatch.setTeamsAdded(2);
                                    Log.d("Debug", "case 4");
                                }
                            } else {
                                Log.d("Debug", "Match played before");
                                if (nextMatch.getT1().getTeamName().equals(m.getT1().getTeamName())) {
                                    nextMatch.setT1(m.getT2());
                                    nextMatch.setT1ID(m.getT2ID());
                                    Log.d("Debug", "T2 overwrite T1");
                                } else {
                                    nextMatch.setT2(m.getT2());
                                    nextMatch.setT2ID(m.getT2ID());
                                    Log.d("Debug", "T2 overwrite T2");
                                }
                            }
                            Log.d("Debug", m.getT2().getTeamName() + " won");
                        }
                    } else {
                        t1.matchResult("draw");
                        t2.matchResult("draw");
                        t1.addToOverAllScore(1);
                        t2.addToOverAllScore(1);
                        Log.d("Debug", "Is was a draw");
                    }

                    // m.getT1().addToOverAllScore(Integer.parseInt(teamXScore_tv.getText().toString()));
                    //m.getT2().addToOverAllScore(Integer.parseInt(teamYScore_tv.getText().toString()));

                    m.setPlayed(true);


                    for (Match mm : matches) {
                        if (!mm.isPlayed()) {
                            MyApplication.activeMatch = matches.indexOf(mm) + 1;
                            Log.d("Debug", "Next match: " + (matches.indexOf(mm) + 1));
                            break;
                        }
                    }

                    Log.d("Match", "Match " + activeMatchNumber + " done!");
                    Log.d("Match", "Score " + m.getScoreT1() + " - " + m.getScoreT2());
                    if (m.getWinner() != null) {
                        Log.d("Match", "Winner is " + m.getWinner());
                    } else Log.d("Match", "Match draw!");

                    if (MyApplication.matchesPlayed == matches.size()) {
                        MyApplication.getActiveTournament().setIsDone(true);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new TournamentWinner_frag())
                                .addToBackStack(null)
                                .commit();
                    } else {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new ActiveMatchScore_frag())
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }

        } else if (v == teamXPlus_b) {
            int score = Integer.parseInt(teamXScore_tv.getText().toString());
            score++;
            updateScore("t1", score);

        } else if (v == teamXMinus_b) {
            int score = Integer.parseInt(teamXScore_tv.getText().toString());
            if (score != 0) {
                score--;
            }
            updateScore("t1", score);

        } else if (v == teamYPlus_b) {
            int score = Integer.parseInt(teamYScore_tv.getText().toString());
            score++;
            updateScore("t2", score);

        } else if (v == teamYMinus_b) {
            int score = Integer.parseInt(teamYScore_tv.getText().toString());
            if (score != 0) {
                score--;
            }
            updateScore("t2", score);
        }
        saveToFireBase();
    }

    public void saveToFireBase(){

        //Firebase updateMatchRef = matchesRef.child(m.getMatchID());
        if (readyToPlay) {
            activeMatchRef.setValue(m);
            t1Ref.setValue(t1);
            t2Ref.setValue(t2);

            if (MyApplication.getActiveTournament().getType().equals(MyApplication.singleEliminationString)) {
                nextMatchRef.setValue(nextMatch);
            }

        }
    }

}
