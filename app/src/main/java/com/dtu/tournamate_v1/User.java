package com.dtu.tournamate_v1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chris on 27-04-2016.
 */
public class User {

    private String u_ID;
    private String firstName;
    private String lastName;
    private String email;
    private String provider;
    private ArrayList<String> storedTournamentsID;

    public User() {
        this.u_ID = "";
        this.firstName = "";
        this.lastName = "";
        this.email ="";
        this.provider="";
        this.storedTournamentsID = new ArrayList<>();
    }

    public ArrayList<String> getStoredTournamentsID() {
        return storedTournamentsID;
    }

    public void setStoredTournamentsID(ArrayList<String> storedTournamentsID) {
        this.storedTournamentsID = storedTournamentsID;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getU_ID() {
        return u_ID;
    }

    public void setU_ID(String u_ID) {
        this.u_ID = u_ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
