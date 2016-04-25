package com.dtu.tournamate_v1;

/**
 * Created by ce on 25-04-2016.
 */
public class Player {

    //Basic info
    private String name;
    private int points;
    // Image?

    //Firebase
    private String teamRef;
    private String userRef;

    public Player(String name, int points, String teamRef, String userRef) {
        this.name = name;
        this.points = points;
        this.teamRef = teamRef;
        this.userRef = userRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTeamRef() {
        return teamRef;
    }

    public void setTeamRef(String teamRef) {
        this.teamRef = teamRef;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }
}
