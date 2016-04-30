package com.dtu.tournamate_v1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.tournamate_v1.createNewTournament.NewTournament_frag;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ce on 18-04-2016.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private Context context;
    private Handler handler = new Handler();
    Fragment fragment;
    private ArrayList<Tournament> mDataset;
    private boolean doneFetchingFirebase = false;
    private String tID;
    ProgressDialog progressDialog;
    ArrayList<Match> fetchedMatchesFirebase = new ArrayList();

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);

    private Paint p = new Paint();
    RecyclerView recyclerView;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mView;
        TextView tName;
        TextView tDate;

        public ViewHolder(View v) {
            super(v);
            tName = (TextView) v.findViewById(R.id.tournament_list_name);
            tDate = (TextView) v.findViewById(R.id.tournament_list_date);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerAdapter(ArrayList<Tournament> myDataset, Fragment fragment, RecyclerView recyclerView) {
        mDataset = myDataset;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tournament_listelement, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tName.setText(mDataset.get(position).getName());
        holder.tDate.setText(mDataset.get(position).getCreatedAt());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("RecyclerView", "You clicked on " + position);
                //mRecycleViewAdapter.getRef(position).removeValue();
                ArrayList<String> tArray = new ArrayList<String>(MyApplication.getUser().getStoredTournamentsID());
                tID = tArray.get(position);
                fetchTournament();
                fetchFromFireBase();
                //fetchMatches(tID);

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void fetchTournament() {

        MyApplication.resumingTournament = true;
        MyApplication.matchList.clear();
        MyApplication.teams.clear();
        fetchedMatchesFirebase.clear();

        Firebase matchRef = myFirebaseRef.child(MyApplication.matchesString);
        Firebase tournamentRef = myFirebaseRef.child(MyApplication.tournamentsString);
        Firebase teamsRef = myFirebaseRef.child(MyApplication.teamsString);

        tournamentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "Tournament listener called: ");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String t_objectID = (String) child.getKey();
                    if (t_objectID.equals(tID)) {
                        MyApplication.tournamentID_parse = t_objectID;
                        MyApplication.isDone = (boolean) child.child("isDone").getValue();
                        MyApplication.tournamentName = (String) child.child("name").getValue();
                        MyApplication.type = (String) child.child("type").getValue();
                        MyApplication.numberOfMatches = Integer.parseInt("" + child.child("numberOfMatches").getValue());
                        Log.d("Firebase", "Tournament found: " + MyApplication.tournamentID_parse);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String t_objectID = (String) child.child("tournamentID").getValue();
                    Log.d("Firebase", "Team found: " + child.child("teamName").getValue());
                    if (t_objectID.equals(tID)) {
                        Team t = child.getValue(Team.class);
                        MyApplication.teams.add(t);
                    }
                }
                Log.d("Firebase", "Done fetching teams");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Firebase", "Team fetch error: " + firebaseError.getMessage());
            }
        });

        matchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "Match listener called: ");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String m_objectID = (String) child.getKey();
                    Log.d("Firebase", "Match found: " + (String) child.getKey());
                    String t_objectID = (String) child.child("tournamentID").getValue();
                    if (t_objectID.equals(tID)) {
                        Match m = child.getValue(Match.class);
                        Log.d("Firebase", "Match title: " + m.getMatchTitle());
                        fetchedMatchesFirebase.add(m);
                    }
                }
                MyApplication.matchList = fetchedMatchesFirebase;
                doneFetchingFirebase = true;
                Log.d("Firebase", "Done fetching, and added to Myapplication");

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Firebase", "Match fetch error: " + firebaseError.getMessage());
            }
        });


    }

    public void fetchFromFireBase() {
        progressDialog = ProgressDialog.show(fragment.getActivity(), "",
                "Loading. Please wait...", true);
        progressDialog.setCancelable(true);
        new Thread(resumeWhenReady).start();
    }

    public final Runnable resumeWhenReady = new Runnable() {

        @Override
        public void run() {
            Log.d("Firebase", "Runnable call");
            if (doneFetchingFirebase) {
                Log.d("Firebase", "doneFetchingFirebase true");
                Log.d("Firebase", "Size of matchlist:" + MyApplication.matchList.size() + " and number of matches: " + MyApplication.numberOfMatches);
                if (MyApplication.matchList.size() == MyApplication.numberOfMatches && MyApplication.matchList.size() > 0) {
                    progressDialog.dismiss();
                    Log.d("Firebase", "Done fetching = true");
                    Log.d("Firebase", "Number of matches " + MyApplication.matchList.size());

                    fragment.getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new NewTournament_frag())
                            .commit();
                    ((MainMenu_akt)fragment.getActivity()).fabOnOff(0);
                }
            } else {
                Log.d("Firebase", "doneFetchingFirebase true");
                handler.postDelayed(resumeWhenReady, 1000);
            }
        }
    };



}


