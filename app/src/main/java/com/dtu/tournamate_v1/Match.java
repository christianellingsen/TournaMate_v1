package com.dtu.tournamate_v1;

import android.util.Log;

//import com.parse.ParseClassName;
//import com.parse.ParseObject;

/**
 * Created by Christian on 05-05-2015.
 */
//@bParseClassName("Matches")
public class Match { //extends ParseObject {

    private int matchNumber, scoreT1, scoreT2, roundID, nextMatchNumber, teamsAdded;
    private Team t1, t2, winner, looser;
    private boolean played;
    private String matchID, matchTitle, t1ID, t2ID, tournamentID ;

    public Match() {
        this.matchNumber =0;
        this.nextMatchNumber = 0 ;
        this.scoreT1 = 0;
        this.scoreT2 = 0;
        this.roundID = 0;
        this.teamsAdded = 0;
        this.t1 = new Team();
        this.t2 = new Team();
        this.winner = new Team();
        this.played = false;
        this.matchID = "";
        this.t1ID = "";
        this.t2ID = "";
        this.tournamentID = "";
        this.matchTitle = "";
    }

    public int getNextMatchNumber() {return nextMatchNumber;}
    public void setNextMatchNumber(int nextMatchNumber) {this.nextMatchNumber = nextMatchNumber;}

    public int getTeamsAdded() {
        return teamsAdded;
    }
    public void setTeamsAdded(int teamsAdded) {this.teamsAdded = teamsAdded;}

    public String getMatchTitle() {
        return matchTitle;
    }
    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public int getMatchNumber() {
        return matchNumber;
    }
    public void setMatchNumber(int value){this.matchNumber=value;}

    public int getRoundID() {
        return roundID;
    }
    public void setRoundID(int roundID) {
        this.roundID = roundID;
    }

    public int getScoreT1() {
        return scoreT1;
    }
    public void setScoreT1(int scoreT1) {
        this.scoreT1 = scoreT1;
    }

    public int getScoreT2() {
        return scoreT2;
    }
    public void setScoreT2(int scoreT2) {
        this.scoreT2 = scoreT2;
    }

    public Team getT1() {
        return t1;
    }
    public void setT1(Team t1) {
        this.t1 = t1;
    }

    public Team getT2() {
        return t2;
    }
    public void setT2(Team t2) {
        this.t2 = t2;
    }

    public Team getWinner() {
        return winner;
    }
    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public boolean isPlayed() {
        return played;
    }
    public void setPlayed(boolean played) {this.played = played; Log.d("Debug","Match " + matchNumber +" is played");}

    public void setMatchID(String vaule){
        this.matchID = vaule;
    }
    public String getMatchID(){
        return this.matchID;
    }

    public String getTournamentID() {
        return tournamentID;
    }
    public void setTournamentID(String tournamentID) {
        this.tournamentID = tournamentID;
    }

    public String getT1ID() {
        return t1ID;
    }
    public void setT1ID(String t1ID) {
        this.t1ID = t1ID;
    }

    public String getT2ID() {
        return t2ID;
    }
    public void setT2ID(String t2ID) {
        this.t2ID = t2ID;
    }

}
