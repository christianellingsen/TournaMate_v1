package com.dtu.tournamate_v1;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;
//import com.facebook.FacebookSdk;
//import com.firebase.client.Firebase;
//import com.parse.Parse;
//import com.parse.ParseObject;

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
    private static User user = new User();
    public static boolean emailExits = false;

    // Tournaments data
    //public static List<String> tournamnetTypes = new ArrayList<>();
    public static Set<String> playerSet = new HashSet<>();
    public static Set<String> selectedPlayerSet = new HashSet<>();

    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<Match> matchList = new ArrayList<>();
    public static ArrayList<Player> players = new ArrayList<>();
    public static int matchesPlayed = 0;
    public static int activeMatch = 1;

    //public static String type, tournamentName, tournamentRef_firebase;
    //public static boolean isDone = false;
    public static boolean resumingTournament = false;
    //public static int numberOfMatches;
    //public static boolean isOpenToJoin = false;
    //public static boolean isStarted = false;


    // TEST ACTIVE TOURNAMENT

    private static Tournament activeTournament = new Tournament();

    // Firebase strings
    public static final String firebase_URL = "https://brilliant-torch-7862.firebaseio.com/TournaMate_v1";
    public static String usersString = "users";
    public static String tournamentsString = "Tournaments";
    public static String teamsString = "Teams";
    public static String matchesString = "Matches";

    // Tournament type strings
    public static String roundRobinString = "Round Robin";
    public static String singleEliminationString ="Single Elimination";


    // Make teams

    public static Player playerToMove;
    public static boolean playerToMoveSelected = false;
    public static int playerMoveFromTeam = 0;
    public static int playerMovePlayerIndex = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        // Firebase
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        Firebase ref = new Firebase(firebase_URL);

        //tournamnetTypes.add("Round Robin");
        //tournamnetTypes.add("Single Elimination");


        SharedPreferences prefs = getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        user.setU_ID(prefs.getString("uID", ""));
        user.setFirstName(prefs.getString("firstName", ""));
        user.setLastName(prefs.getString("lastName", ""));
        user.setEmail(prefs.getString("email", ""));
        user.setStoredTournamentsID(new ArrayList<String>(prefs.getStringSet("tournaments",new HashSet<String>())));
        //user.setStoredTournamentsID(new ArrayList<String>());


        /**
        FacebookSdk.sdkInitialize(getApplicationContext());
        **/



    }


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
    }

    public static Tournament getActiveTournament() {
        return activeTournament;
    }

    public static void setActiveTournament(Tournament activeTournament) {
        MyApplication.activeTournament = activeTournament;
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

    public void saveUserToPrefs(){
        SharedPreferences prefs = getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        prefs.edit().putString("uID",user.getU_ID()).apply();
        prefs.edit().putString("firstName", user.getFirstName()).apply();
        prefs.edit().putString("lastName", user.getLastName()).apply();
        prefs.edit().putString("email", user.getEmail()).apply();
        prefs.edit().putStringSet("tournaments", new HashSet<String>(user.getStoredTournamentsID())).apply();
        prefs.edit().commit();
    }

}