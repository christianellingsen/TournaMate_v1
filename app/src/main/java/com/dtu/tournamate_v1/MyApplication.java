package com.dtu.tournamate_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.dtu.tournamate_v1.createNewTournament.NewTournament_frag;
import com.dtu.tournamate_v1.createNewTournament.TournamentReady_frag;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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

    public static Set<String> playerSet = new HashSet<>();
    public static Set<String> selectedPlayerSet = new HashSet<>();

    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<Match> matchList = new ArrayList<>();
    public static ArrayList<Player> players = new ArrayList<>();
    public static int matchesPlayed = 0;
    public static int activeMatch = 1;

    public static boolean resumingTournament = false;

    private boolean doneFetchingUser, doneFetchingPlayers = false;
    private Handler handler;

    // TEST ACTIVE TOURNAMENT

    private static Tournament activeTournament = new Tournament();

    // Firebase strings
    public static final String firebase_URL = "https://brilliant-torch-7862.firebaseio.com/TournaMate_v1";
    public static String usersString = "users";
    public static String tournamentsString = "Tournaments";
    public static String teamsString = "Teams";
    public static String matchesString = "Matches";
    public static String playersString = "Players";

    // Tournament type strings
    public static String roundRobinString = "Round Robin";
    public static String singleEliminationString ="Single Elimination";

    // Speactate tournament
    public static String spectateT_ID ="";
    public static String spectateT_name ="";

    // Make teams
    public static Player playerToMove;
    public static boolean playerToMoveSelected = false;
    public static int playerMoveFromTeam = 0;
    public static int playerMovePlayerIndex = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();

        SharedPreferences prefs = getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        user.setU_ID(prefs.getString("uID", ""));
        user.setFirstName(prefs.getString("firstName", ""));
        user.setLastName(prefs.getString("lastName", ""));
        user.setEmail(prefs.getString("email", ""));
        user.setStoredTournamentsID(new ArrayList<String>(prefs.getStringSet("tournaments",new HashSet<String>())));


        // Firebase
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);



        Firebase ref = new Firebase(MyApplication.firebase_URL);
        AuthData authData = ref.getAuth();
        if (authData != null) {
            fetchUserFromFirebase();
            new Thread(fetchFirebaseRunnable).start();
        }


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


    public void fetchUserFromFirebase(){
        Firebase ref = new Firebase(firebase_URL);
        Firebase usersRef = ref.child(MyApplication.usersString);
        Firebase playersRef = ref.child(MyApplication.playersString);
        final String uid = MyApplication.getUser().getU_ID();

        if(!doneFetchingUser) {
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    setUser(user);
                            //MyApplication.getUser().setStoredTournamentsID(user.getStoredTournamentsID());
                            //MyApplication.getUser().setStoredPlayers(user.);
                    doneFetchingUser = true;
                   // Log.d("MyApplication","User fetched: " +getUser().getFullName());
                    //Log.d("MyApplication","User playerList: "+ getUser().getStoredPlayers().size());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        if (!doneFetchingPlayers && doneFetchingUser) {
            players.clear();
            playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String playerID = child.getKey();
                        //Log.d("Fetch tournamnets","MY U_ID: "+MyApplication.getUser().getU_ID());
                        //Log.d("MyApplication: ", "Found player: "+ playerID);
                        for (String s : getUser().getStoredPlayers()) {
                            //Log.d("MyApplication","Compare "+s+" with " +playerID);
                            if (s!=null && s.equals(playerID)) {
                                Player p = child.getValue(Player.class);
                                players.add(p);
                                //Log.d("MyApplication", "Player added");
                            }
                        }
                    }
                    doneFetchingPlayers=true;
                    //Log.d("MyApplication", "Datasnapshot loop done");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        else {
            //Log.d("MyApplication","Fetching players not ready yet");
        }
    }

    public final Runnable fetchFirebaseRunnable = new Runnable() {

        @Override
        public void run() {
            //Log.d("Firebase", "Runnable call");
            if (doneFetchingUser && doneFetchingPlayers) {
                //Log.d("MyApplication","Done. Players: "+players.size());
            }
            else {
                handler.postDelayed(fetchFirebaseRunnable, 1000);
                //Log.d("MyApplication","Still fetching. DoneUser: "+ doneFetchingUser+ " donePlayers: "+ doneFetchingPlayers);
                fetchUserFromFirebase();
            }
        }
    };

}