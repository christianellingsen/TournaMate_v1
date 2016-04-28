package com.dtu.tournamate_v1;

//import com.parse.ParseClassName;
//import com.parse.ParseObject;


/**
 * Created by Christian on 26-04-2015.
 */

public class Tournament {

    private String name,winner,createdAt, t_ID,type,createdBy_uID;
    private Boolean isDone, isDeleted;
    private int numberOfMatches;

    public void Tournament(){
    }

    public String getName(){return this.name;}
    public void setName(String value){this.name = value;}

    public String getWinner(){return this.winner;}
    public void setWinner(String value){this.winner = value;}

    public Boolean getIsDone(){return this.isDone;}
    public void setIsDone(Boolean value){this.isDone = value;}

    public void setCreatedAt(String value){this.createdAt = value;}
    public String getCreatedAt() {return createdAt;}

    public String getT_ID(){return t_ID;}
    public void setT_ID(String t_ID){this.t_ID = t_ID;}

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getNumberOfMatches() {return numberOfMatches;}
    public void setNumberOfMatches(int numberOfMatches) {this.numberOfMatches = numberOfMatches;}

    public String getCreatedBy_uID() {
        return createdBy_uID;
    }

    public void setCreatedBy_uID(String createdBy_uID) {
        this.createdBy_uID = createdBy_uID;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
