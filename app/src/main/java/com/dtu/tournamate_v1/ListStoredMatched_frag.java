package com.dtu.tournamate_v1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.tournamate_v1.createNewTournament.NewTournament_frag;
import com.firebase.client.DataSnapshot;
import com.firebase.client.EventTarget;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;


public class ListStoredMatched_frag extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean doneFetchingFirebase = false;
    private Handler handler;

    private String tID;
    ProgressDialog progressDialog;

    ArrayList<Tournament> storedTournaments = new ArrayList();
    ArrayList<String> storedTournamentsNames = new ArrayList();
    ArrayList<Match> fetchedMatchesFirebase = new ArrayList();

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase tournamentRef = myFirebaseRef.child("Tournaments");

    FirebaseRecyclerAdapter<Tournament, viewHolder> fireBaseAdapter;
    MyRecyclerAdapter adapter;

    View root;
    View view;

    private int edit_position;
    private boolean add = false;
    private Paint p = new Paint();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.list_stored_matched_fragment, container, false);


        handler = new Handler();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.stored_matches_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // Standard adapter
        Query tournamentQueryUID = tournamentRef.orderByChild("createdBy_uID").equalTo(MyApplication.getUser().getU_ID());
        //Query tournamentQueryDeleted = tournamentRef.orderByChild("isDeleted").equalTo(false);

        Query tournamentQueryDeleted = tournamentQueryUID.getRef().orderByChild("isDeleted").equalTo(false);

        // Firebase adaper
        fireBaseAdapter =
                new FirebaseRecyclerAdapter<Tournament, viewHolder>(
                        Tournament.class,
                        R.layout.tournament_listelement,
                        viewHolder.class,
                        tournamentQueryDeleted
                ) {
                    @Override
                    protected void populateViewHolder(viewHolder mViewHolder, Tournament t, final int i) {
                        mViewHolder.tName.setText(t.getName());
                        mViewHolder.tDate.setText(t.getCreatedAt());
                        mViewHolder.mView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Log.w("RecyclerView", "You clicked on " + i);
                                //mRecycleViewAdapter.getRef(position).removeValue();
                                tID = fireBaseAdapter.getRef(i).getKey();
                                fetchTournament();
                                fetchFromFireBase();
                                //fetchMatches(tID);

                            }
                        });
                    }

                };


        mRecyclerView.setAdapter(fireBaseAdapter);
        mRecyclerView.setClickable(true);
        initSwipe();
        return root;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView tName;
        TextView tDate;

        public viewHolder(View v) {
            super(v);
            tName = (TextView) v.findViewById(R.id.tournament_list_name);
            tDate = (TextView) v.findViewById(R.id.tournament_list_date);
            mView = v;
        }
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT){

                    Snackbar snackbar = Snackbar
                            .make(getView(), "Tournament is deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    undoDeleteTournament(tID);
                                }
                            });

                    snackbar.show();

                    deleteTournament(fireBaseAdapter.getRef(position).getKey());
                    fireBaseAdapter.notifyItemRemoved(position);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Added to favourites.. if it was implemented..", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    mRecyclerView.setAdapter(fireBaseAdapter);
                    //removeView();
                    //edit_position = position;
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(getResources().getColor(R.color.colorPrimary));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_48dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(getResources().getColor(R.color.colorAccent));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_favorite_black_48dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fireBaseAdapter.cleanup();
    }

    public void deleteTournament(final String tournamentID) {

        tID = tournamentID;

        Firebase tournamentRef = myFirebaseRef.child("Tournaments");

        tournamentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String t_objectID = (String) child.getKey();
                    if (t_objectID.equals(tournamentID)) {
                        //child.getRef().removeValue();
                        child.getRef().child("isDeleted").setValue(true);
                        Log.d("Firebase", "Tournament found and deleted");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }

    public void undoDeleteTournament(final String tournamentID) {

        Firebase tournamentRef = myFirebaseRef.child("Tournaments");

        tournamentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String t_objectID = (String) child.getKey();
                    if (t_objectID.equals(tournamentID)) {
                        //child.getRef().removeValue();
                        child.getRef().child("isDeleted").setValue(false);
                        Log.d("Firebase", "Tournament found and deleted");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

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
                    Log.d("Firebase", "Team found: "+ child.child("teamName").getValue());
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

    public void fetchFromFireBase(){
        progressDialog = ProgressDialog.show(getActivity(), "",
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

                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new NewTournament_frag())
                            .commit();
                    ((MainMenu_akt)getActivity()).fabOnOff(0);
                }
            }
            else {
                Log.d("Firebase", "doneFetchingFirebase true");
                handler.postDelayed(resumeWhenReady, 1000);
            }
        }
    };

}













