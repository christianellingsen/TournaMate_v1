package com.dtu.tournamate_v1;

/**
 * Created by ce on 25-04-2016.
 */
public class Player {

    //Basic info
    private String p_ID, u_ID, te_ID, t_ID,name;
    private int score;
    // Image?

    //Firebase
    private String teamRef;
    private String userRef;

    public Player(String p_ID, String u_ID, String te_ID, String t_ID, String name, int score, String teamRef, String userRef) {
        this.p_ID = p_ID;
        this.u_ID = u_ID;
        this.te_ID = te_ID;
        this.t_ID = t_ID;
        this.name = name;
        this.score = score;
        this.teamRef = teamRef;
        this.userRef = userRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public String getP_ID() {
        return p_ID;
    }

    public void setP_ID(String p_ID) {
        this.p_ID = p_ID;
    }

    public String getU_ID() {
        return u_ID;
    }

    public void setU_ID(String u_ID) {
        this.u_ID = u_ID;
    }

    public String getTe_ID() {
        return te_ID;
    }

    public void setTe_ID(String te_ID) {
        this.te_ID = te_ID;
    }

    public String getT_ID() {
        return t_ID;
    }

    public void setT_ID(String t_ID) {
        this.t_ID = t_ID;
    }
}
