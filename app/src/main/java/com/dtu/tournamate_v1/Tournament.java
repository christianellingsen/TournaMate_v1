package com.dtu.tournamate_v1;

import android.util.Log;

//import com.parse.ParseClassName;
//import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Christian on 26-04-2015.
 */

public class Tournament {

    private String name;
    private Boolean isDone;
    private String winner;
    private String createdAt;
    private String objectID;
    private long objectID_sql;
    private String type;
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

    public String getObjectID(){return objectID;}
    public void setObjectID(String objectID){this.objectID=objectID;}

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getNumberOfMatches() {return numberOfMatches;}
    public void setNumberOfMatches(int numberOfMatches) {this.numberOfMatches = numberOfMatches;}


}
