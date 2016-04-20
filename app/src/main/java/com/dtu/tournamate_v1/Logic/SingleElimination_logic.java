package com.dtu.tournamate_v1.Logic;

import android.util.Log;

import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by ce on 20-04-2016.
 */
public class SingleElimination_logic {

    private int numberOfMatches, numberOfRounds, matchNumber, roundID, numberOfSkewMatches;
    private ArrayList<Team> teams;
    private ArrayList<Match> matches = new ArrayList<>();
    private ArrayList<Match> nextRoundMatches = new ArrayList<>();
    private boolean skewNumberOfTeams = false;

    public SingleElimination_logic(){
        this.teams = MyApplication.teams;
        this.numberOfMatches = 0;
        this.numberOfRounds = 0;
        this.matchNumber = 0;
        this.roundID = 0;
    }

    public void createMatches() throws IOException {

        //In single elimination the number of matches is the number of teams -1
        numberOfMatches = (teams.size() - 1);
        findNumberOfRounds();
        Collections.shuffle(teams);
        roundID = numberOfRounds;
        Log.d("Debug", "Round ID: " + roundID);
        for (int counter = numberOfMatches;counter>0;counter--){
            matchNumber = counter;
            Log.d("Knockout", "For loop counter: "+ counter);

            Match m = new Match();
            m.setMatchNumber(matchNumber);
            m.setRoundID(roundID);

            if (matchNumber==numberOfMatches){
                m.setMatchTitle("Final");
            }
            else if (matchNumber==numberOfMatches-1){
                    m.setMatchTitle("Semifinal 1");
            }
            else if (matchNumber==numberOfMatches-2){
                m.setMatchTitle("Semifinal 2");
            }
            else if (matchNumber==numberOfMatches-3){
                m.setMatchTitle("Quarterfinal 1");
            }
            else if(matchNumber==numberOfMatches-4){
                m.setMatchTitle("Quarterfinal 2");
            }
            else if(matchNumber==numberOfMatches-5){
                m.setMatchTitle("Quarterfinal 3");
            }
            else if(matchNumber==numberOfMatches-6){
                m.setMatchTitle("Quarterfinal 4");
            }

            matches.add(m);
            Log.d("Debug", "Adding match");

            if(matchNumber<((int) Math.pow(2,roundID))){
                roundID--;
            }
            Log.d("Debug", "Round ID: " + roundID);
        }
        MyApplication.matchList = matches;
        MyApplication.sortMatches();
        matches = MyApplication.matchList;
        int i = 0;
        while(i<teams.size()){
            Match m = matches.get(i);
            Team t1, t2;
            t1 = teams.get(i);
            t2 = null;
            if(teams.size()<=i+1){
                t2 = teams.get(i+1);
            }
            m.setT1(t1);
            if(t2==null){
                m.setT2(t2);
            }
            i = i+2;
        }
        MyApplication.matchList = matches;

        Log.d("Debug", "############ DONE ##############");
        printMatches();
    }


    public void findNumberOfRounds(){
        int i = 1;
        int powerOf2 = 2;
        while (powerOf2<teams.size()){
            numberOfRounds++;
            i++;
            powerOf2 = (int)Math.pow(2, i);
            Log.d("Knockout", "i: "+ i+" power of 2: "+powerOf2+" no. rounds: " + numberOfRounds);
        }
        numberOfRounds++;
        numberOfSkewMatches = powerOf2-teams.size();
        Log.d("Knockout", "Number of rounds: "+ numberOfRounds+ " and skew matches: "+numberOfSkewMatches);
    }

    public void printMatches(){
        MyApplication.sortMatches();
        for (Match m : MyApplication.matchList){
            Log.d("Knockout","Match: "+m.getMatchNumber()+ " has title: "+m.getMatchTitle());
        }
    }
}
