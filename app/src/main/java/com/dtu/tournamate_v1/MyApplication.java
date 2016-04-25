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

    // DATABASE

    public static long tournamentID_sql = 0;

    public static final String DATABASE_TABLE_TOURNAMENTS = "tournaments";
    public static final String DATABASE_TABLE_MATCHES = "matches";
    public static final String DATABASE_TABLE_TEAMS = "teams";

    public static final int COL_ROWID = 0;

    public static final int COL_TOURNAMENTS_NAME = 1;
    public static final int COL_TOURNAMENTS_TYPE = 2;
    public static final int COL_TOURNAMENTS_DONE = 3;
    public static final int COL_TOURNAMENTS_ACTIVE = 4;
    public static final int COL_TOURNAMENTS_PLAYED = 5;
    public static final int COL_TOURNAMENTS_ONLINE = 6;
    public static final int COL_TOURNAMENTS_PARSE = 7;
    public static final int COL_TOURNAMENTS_DATE = 8;

    public static final int COL_TEAMS_NAME = 1;
    public static final int COL_TEAMS_WON = 2;
    public static final int COL_TEAMS_LOST = 3;
    public static final int COL_TEAMS_DRAW = 4;
    public static final int COL_TEAMS_SCORE = 5;
    public static final int COL_TEAMS_PLAYED = 6;

    public static final int COL_MATCHES_TID = 1;
    public static final int COL_MATCHES_T1ID = 2;
    public static final int COL_MATCHES_T2ID = 3;
    public static final int COL_MATCHES_T1SCORE = 4;
    public static final int COL_MATCHES_T2SCORE = 5;
    public static final int COL_MATCHES_NUMBER = 6;
    public static final int COL_MATCHES_PLAYED = 7;

    // Firebase
    public static final String firebase_URL = "https://brilliant-torch-7862.firebaseio.com/TournaMate_v1";


    // Parse
    public static final String YOUR_APPLICATION_ID = "kSXYu1zyAGX1LjakV2v5X69UnWHg6DWIItTLH3VS";
    public static final String YOUR_CLIENT_KEY = "jTqfP3caYT82lZ3wGufiTjrgfnxHzEhiHfMHDlBz";


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
        // Add your initialization code here
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseObject.registerSubclass(Tournament.class);
        ParseObject.registerSubclass(Match.class);
        ParseObject.registerSubclass(Team.class);

        FacebookSdk.sdkInitialize(getApplicationContext());
        **/

        // Firebase
        Firebase.setAndroidContext(this);

        // ...
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