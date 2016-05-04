package com.dtu.tournamate_v1;

/**
 * Created by ce on 25-04-2016.
 */
public class Player {

    //Basic info
    private String p_ID, u_ID, te_ID, t_ID,name;
    private int score;
    // Image?

    public Player() {
        this.p_ID = "";
        this.u_ID = "";
        this.te_ID = "";
        this.t_ID = "";
        this.name = "";
        this.score = 0;
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
