package com.dtu.tournamate_v1;

import android.util.Log;
import android.util.SparseArray;

//import com.parse.ParseClassName;
//import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Christian on 16-04-2015.
 */

//@ParseClassName("Team")
public class Team { // extends ParseObject{

    private String teamName, teamID, tournamentID;
    private int matchesWon, matchesLost, matchesDraw, matechesPlayed, overAllScore;
    private ArrayList<Player> teamMembers = new ArrayList<>();
    //private long teamID_sql;

    public Team() {
        this.teamName = "";
        this.matchesWon = 0;
        this.matchesLost = 0;
        this.matchesDraw = 0;
        this.matechesPlayed = 0;
        this.overAllScore = 0;
        this.tournamentID = "";
        this.teamID = "";
    }


    public void addTeamMember(Player newTeamMember){
        teamMembers.add(newTeamMember);
    }
    public String getTeamName(){return teamName;}

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(String tournamentID) {
        this.tournamentID = tournamentID;
    }

    public ArrayList<Player> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<Player> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void matchResult(String result){
        if ("won".equals(result)){
            matchesWon++;
            matechesPlayed++;
            Log.d("Debug", this.getTeamName() + " won a game!");
        }
        else if ("lost".equals(result)){
            matchesLost++;
            matechesPlayed++;
            Log.d("Debug", this.getTeamName() + " lost a game!");
        }
        else {
            matchesDraw++;
            matechesPlayed++;
            Log.d("Debug", this.getTeamName() + " played draw!");
        }
    }

    public void addToOverAllScore(int points){
        this.overAllScore += points;
    }

    public int getMatchesWon() {
        return matchesWon;
    }
    public void setMatchesWon(int matchesWon){
        this.matchesWon = matchesWon;
    }

    public int getOverAllScore(){
        return overAllScore;
    }
    public void setOverAllScore(int overAllScore){
        this.overAllScore= overAllScore;
    }

    public int getMatchesLost() {
        return matchesLost;
    }
    public void setMatchesLost(int matchesLost) {
        this.matchesLost = matchesLost;
    }

    public int getMatchesDraw() {
        return matchesDraw;
    }
    public void setMatchesDraw(int matchesDraw) {
        this.matchesDraw = matchesDraw;
    }

    public int getMatechesPlayed() {
        return matechesPlayed;
    }
    public void setMatechesPlayed(int matechesPlayed) {
        this.matechesPlayed = matechesPlayed;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void makeTeamName(){
        teamName = "Dummy";
        if(teamMembers.size()>0){
            teamName = "";
            for(int i=0; i<teamMembers.size();i++) {
                String name = teamMembers.get(i).getName();
                teamName += name;
                teamName += " & ";
            }
            teamName = teamName.substring(0,teamName.length()-3);
        }
    }

}
