package com.dtu.tournamate_v1.activeTournament;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.firebase.client.Firebase;
//import com.parse.FindCallback;
//import com.parse.GetCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 19-04-2015.
 */
public class ActiveMatchScore_frag extends Fragment implements View.OnClickListener {

    ArrayList<Match> matches;
    View rod;
    private TextView matchNumber_tv, teamX_tv, teamY_tv, teamXScore_tv, teamYScore_tv, matchTitle_tv;
    private Button teamXPlus_b, teamXMinus_b, teamYMinus_b, teamYPlus_b, rank_b, matchlist_b, next_b;
    private int activeMatchNumber;
    private Match m;
    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase matchesRef = myFirebaseRef.child("Matches");
    Firebase activeMatchRef;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.active_match_score, container, false);

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

        matches = MyApplication.matchList;

        if (MyApplication.type.equals("Round Robin")) {
            matchTitle_tv.setVisibility(View.GONE);
        } else {
            matchTitle_tv.setVisibility(View.VISIBLE);
        }

        Log.d("Debug", "Active match: " + MyApplication.activeMatch);
        Log.d("Debug", "Matches played: " + MyApplication.matchesPlayed);

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

            matchNumber_tv.setText(getString(R.string.activeTournament_Match) + m.getMatchNumber());
            matchTitle_tv.setText(m.getMatchTitle());
            teamX_tv.setText(m.getT1().getTeamName());
            teamY_tv.setText(m.getT2().getTeamName());
        }
        if (MyApplication.matchesPlayed == matches.size() - 2) {
            next_b.setText(getString(R.string.activeTournament_playNext));
        } else if (MyApplication.matchesPlayed == matches.size() - 1) {
            next_b.setText(getString(R.string.activeTournament_done));
        }

        if (MyApplication.isDone) {
            next_b.setText(getString(R.string.activeTournament_seeWinner));
        }

        teamXScore_tv.setText("" + m.getScoreT1());
        teamYScore_tv.setText("" + m.getScoreT2());

        if (m.isPlayed()) {
            matchNumber_tv.setBackgroundColor(Color.parseColor("#0FAE02"));
        }

        if (MyApplication.isOnline) {
            //fetchMatchID();
        }

        activeMatchRef = matchesRef.child(m.getMatchID());

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
                    .replace(R.id.main_frame, new RoundRobinMatchList_frag())
                    .addToBackStack(null)
                    .commit();
        } else if (v == next_b) {
            if(m.getScoreT1()==m.getScoreT2() && MyApplication.type.equals("Single Elimination")){

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
            }
            else {
                if (!m.isPlayed()) {
                    MyApplication.matchesPlayed++;
                }
                if (m.getScoreT1() > m.getScoreT2()) {
                    m.setWinner(m.getT1());
                    m.getT1().matchResult("won");
                    m.getT2().matchResult("lost");
                    m.getT1().addToOverAllScore(3);
                    m.getT2().addToOverAllScore(0);
                    m.setWinner(m.getT1());
                    if (m.getMatchNumber() < MyApplication.matchList.size() && MyApplication.type.equals("Single Elimination")) {


                        if (matches.get(m.getNextMatchNumber() - 1).getTeamsAdded() == 0) {
                            matches.get(m.getNextMatchNumber() - 1).setT1(m.getT1());
                            matches.get(m.getNextMatchNumber() - 1).setTeamsAdded(1);
                            Log.d("Debug", "case 1");
                        } else {
                            matches.get(m.getNextMatchNumber() - 1).setT2(m.getT1());
                            Log.d("Debug", "case 2");
                        }
                    }
                    Log.d("Debug", m.getT1().getTeamName() + " won");
                } else if (m.getScoreT1() < m.getScoreT2()) {
                    m.setWinner(m.getT2());
                    m.getT1().matchResult("lost");
                    m.getT2().matchResult("won");
                    m.getT1().addToOverAllScore(0);
                    m.getT2().addToOverAllScore(3);
                    m.setWinner(m.getT2());
                    if (m.getMatchNumber() < MyApplication.matchList.size()) {
                        if (matches.get(m.getNextMatchNumber() - 1).getTeamsAdded() == 0) {
                            matches.get(m.getNextMatchNumber() - 1).setT1(m.getT2());
                            matches.get(m.getNextMatchNumber() - 1).setTeamsAdded(1);
                            Log.d("Debug", "case 3");
                        } else {
                            matches.get(m.getNextMatchNumber() - 1).setT2(m.getT2());
                            Log.d("Debug", "case 4");
                        }
                        Log.d("Debug", m.getT2().getTeamName() + " won");
                    }
                } else {
                    m.getT1().matchResult("draw");
                    m.getT2().matchResult("draw");
                    m.getT1().addToOverAllScore(1);
                    m.getT2().addToOverAllScore(1);
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
                    Log.d("Match", "Winner is " + m.getWinner().getTeamName());
                } else Log.d("Match", "Match draw!");

                if (MyApplication.matchesPlayed == matches.size()) {
                    MyApplication.isDone = true;
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
                if (MyApplication.isOnline) {
                    //saveToParse();
                    saveToFireBase();
                }
            }
        } else if (v == teamXPlus_b) {
            int score = Integer.parseInt(teamXScore_tv.getText().toString());
            score++;
            updateScore("t1", score);
            //if(MyApplication.isOnline){saveToParse();}
            if(MyApplication.isOnline){saveToFireBase();}

        } else if (v == teamXMinus_b) {
            int score = Integer.parseInt(teamXScore_tv.getText().toString());
            if (score != 0) {
                score--;
            }
            updateScore("t1", score);
            //if(MyApplication.isOnline){saveToParse();}
            if(MyApplication.isOnline){saveToFireBase();}

        } else if (v == teamYPlus_b) {
            int score = Integer.parseInt(teamYScore_tv.getText().toString());
            score++;
            updateScore("t2", score);
            //if(MyApplication.isOnline){saveToParse();}
            if(MyApplication.isOnline){saveToFireBase();}

        } else if (v == teamYMinus_b) {
            int score = Integer.parseInt(teamYScore_tv.getText().toString());
            if (score != 0) {
                score--;
            }
            updateScore("t2", score);
            //if(MyApplication.isOnline){saveToParse()();}
            if(MyApplication.isOnline){saveToFireBase();}
        }
    }

    public void saveToFireBase(){

        //Firebase updateMatchRef = matchesRef.child(m.getMatchID());
        activeMatchRef.setValue(m);
    }

}
