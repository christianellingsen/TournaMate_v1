package com.dtu.tournamate_v1;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.Team;
import com.firebase.client.Firebase;
//import com.facebook.FacebookSdk;
//import com.firebase.client.Firebase;
//import com.parse.Parse;
//import com.parse.ParseObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Christian on 12-03-2015.
 */

public class MyApplication extends android.app.Application {

    // User data

    private static boolean firstTime = true;
    private static String u_ID, firstName, lastName, userName, imageURL, eMail;

    // Tournaments data
    public static List<String> tournamnetTypes = new ArrayList<>();
    public static Set<String> playerSet = new HashSet<>();
    public static Set<String> selectedPlayerSet = new HashSet<>();
    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<Match> matchList = new ArrayList<>();
    public static int matchesPlayed = 0;
    public static int activeMatch = 1;
    public static String type, tournamentName, tournamentID_parse;
    public static boolean isDone = false;
    public static boolean isOnline = false;
    public static boolean resumingTournament = false;
    public static int numberOfMatches;

    // Firebase
    public static final String firebase_URL = "https://brilliant-torch-7862.firebaseio.com/TournaMate_v1";


    @Override
    public void onCreate() {
        super.onCreate();

        tournamnetTypes.add("Round Robin");
        tournamnetTypes.add("Single Elimination");

        //SharedPreferences playerList = getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        //SharedPreferences teamList = getSharedPreferences("TeamList", Context.MODE_PRIVATE);
        //SharedPreferences.Editor pl_editor = playerList.edit();
        //SharedPreferences.Editor tl_editor = teamList.edit();


        /**
        FacebookSdk.sdkInitialize(getApplicationContext());
        **/

        // Firebase
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }


    public static void rankTeams() {

        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                return t2.getMatchesWon() - t1.getMatchesWon();
            }
        });

    }

    public static void sortMatches() {
        Collections.sort(matchList, new Comparator<Match>() {
            @Override
            public int compare(Match m1, Match m2) {
                return m1.getMatchNumber() - m2.getMatchNumber();
            }
        });
    }

}