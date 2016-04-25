package com.dtu.tournamate_v1;

import android.util.Log;

//import com.parse.ParseClassName;
//import com.parse.ParseObject;

/**
 * Created by Christian on 05-05-2015.
 */
//@bParseClassName("Matches")
public class Match { //extends ParseObject {

    private int matchNumber;
    private int scoreT1;
    private int scoreT2;
    private int roundID;
    private Team t1, t2, winner;
    private boolean played;
    private String matchID, t1ID, t2ID,tournamentID ;
    //private long matchID_sql,t1ID_sql,t2ID_sql;
    private String matchTitle;
    private int nextMatchNumber;
    private int teamsAdded;

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
        //this.matchID_sql = 0;
        //this.t1ID_sql = 0;
        //this.t2ID_sql = 0;
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

    /**
    // SQL
    public long getMatchID_sql() {
        return matchID_sql;
    }
    public void setMatchID_sql(long matchID_sql) {
        this.matchID_sql = matchID_sql;
    }
    public long getT1ID_sql() {
        return t1ID_sql;
    }
    public void setT1ID_sql(long t1ID_sql) {
        this.t1ID_sql = t1ID_sql;
    }
    public long getT2ID_sql() {
        return t2ID_sql;
    }
    public void setT2ID_sql(long t2ID_sql) {
        this.t2ID_sql = t2ID_sql;
    }


    //PARSE
    public String getT1NameParse() {
        return getString("Team1Name");
    }
    public void putTeam1Name(String value) {
        put("Team1Name", value);
    }
    public String getT2NameParse() {
        return getString("Team2Name");
    }
    public void putTeam2Name(String value) {
        put("Team2Name", value);
    }
    public int getT1ScoreParse() {
        return getInt("Team1Score");
    }
    public void putTeam1Score(int value) {
        put("Team1Score", value);
    }
    public int getT2ScoreParse() {
        return getInt("Team2Score");
    }
    public void putTeam2Score(int value) {
        put("Team2Score", value);
    }
    public boolean getIsPlayedParse(){
        return getBoolean("isPlayed");
    }
    public void putIsPlayed(boolean value) {
        put("isPlayed", value);
    }
    public void putTournamentID(String value) {put("TournamentID",value);}
    public void putMatchNumber(int value){put("MatchNumber",value);}
    public int getMatchNumberParse(){return getInt("MatchNumber");}
    public String getTournamentNameParse() {
        return getString("TournamentName");
    }
    public void putTournamentName(String value) {
        put("TournamentName", value);
    }
     **/
}
