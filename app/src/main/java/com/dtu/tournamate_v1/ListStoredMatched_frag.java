package com.dtu.tournamate_v1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;


public class ListStoredMatched_frag extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean doneFetchingFirebase = false;
    private boolean doneFetchingTournaments = false;
    private Handler handler;

    private String tID;
    ProgressDialog progressDialog;

    ArrayList<Tournament> storedTournaments = new ArrayList();
    ArrayList<String> pendingDelete = new ArrayList();
    ArrayList<Match> fetchedMatchesFirebase = new ArrayList();

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase tournamentRef = myFirebaseRef.child("Tournaments");
    Firebase userRef = myFirebaseRef.child(MyApplication.usersString).child(MyApplication.getUser().getU_ID());

    SwipeRefreshLayout refreshLayout;

    //FirebaseRecyclerAdapter<Tournament, viewHolder> fireBaseAdapter;
    TournamentRecyclerAdapter adapter;

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

        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.stored_matches_swipeRefresh);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.stored_matches_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //adapter.notifyDataSetChanged();
                updateList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });


        Log.d("Debug",MyApplication.getUser().getStoredTournamentsID().toString());
        // Standard adapter
        Query tournamentQueryUID = tournamentRef.orderByChild("createdBy_uID").equalTo(MyApplication.getUser().getU_ID());
        Log.d("Log in","User id: "+MyApplication.getUser().getU_ID());
        //Query tournamentQueryDeleted = tournamentRef.orderByChild("isDeleted").equalTo(false);
        //Query tournamentQueryDeleted = tournamentRef.orderByChild("isDeleted").equalTo(false);
        //Query tournamentQueryUID = tournamentQueryDeleted.getRef().orderByChild("createdBy_uID").equalTo(MyApplication.getUser().getU_ID());

        // Firebase adaper
        /**
        fireBaseAdapter =
                new FirebaseRecyclerAdapter<Tournament, viewHolder>(
                        Tournament.class,
                        R.layout.tournament_listelement,
                        viewHolder.class,
                        tournamentQueryUID
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
         **/
        adapter = new TournamentRecyclerAdapter(storedTournaments,this,mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        //updateList();
        //mRecyclerView.setAdapter(fireBaseAdapter);
        mRecyclerView.setClickable(true);
        findUsersTournaments();
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
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT){

                    Snackbar snackbar = Snackbar
                            .make(getView(), "Tournament is deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    undoDeleteTournament(position);
                                    findUsersTournaments();
                                }
                            });

                    snackbar.show();
                    Log.d("Adapter", "Deleting: " + position);
                    deleteTournament(position);
                    findUsersTournaments();
                    //adapter.notifyItemRemoved(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Added to favourites.. if it was implemented..", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    findUsersTournaments();
                    //mRecyclerView.setAdapter(adapter);
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
        //fireBaseAdapter.cleanup();
    }
    /**
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
    **/

    public void deleteTournament(int i) {

        Log.d("delete","delete called on element i: "+i);
        ArrayList<String> tArray = MyApplication.getUser().getStoredTournamentsID();
        Log.d("delete", "Array: " + tArray.toString());
        String toDelete = tArray.get(i);
        Log.d("delete", "To delete: " + toDelete);
        pendingDelete.clear();
        pendingDelete.add(toDelete);

        MyApplication.getUser().getStoredTournamentsID().remove(toDelete);

        for (Tournament t : storedTournaments){
            if (t.getName().equals(toDelete)){
                storedTournaments.remove(t);
            }
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        prefs.edit().putStringSet("tournaments", new HashSet<String>(MyApplication.getUser().getStoredTournamentsID())).commit();

        Firebase userRef = myFirebaseRef.child(MyApplication.usersString);
        userRef.child(MyApplication.getUser().getU_ID()).setValue(MyApplication.getUser());


    }


    public void undoDeleteTournament(int posistion) {

        //ArrayList<String> tArray = new ArrayList<>(MyApplication.getUser().getStoredTournamentsID());
        Log.d("Delete", "undo delete called on posistion: " + posistion);
        String undoDelete = pendingDelete.get(0);
        Log.d("Delete","pending delete : "+ pendingDelete.get(0));
        Log.d("Delete","t list before: "+ MyApplication.getUser().getStoredTournamentsID().toString());
        MyApplication.getUser().getStoredTournamentsID().add(posistion,undoDelete);
        Log.d("Delete", "t list after: " + MyApplication.getUser().getStoredTournamentsID().toString());

        SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        prefs.edit().putStringSet("tournaments", new HashSet<String>(MyApplication.getUser().getStoredTournamentsID())).commit();

        Firebase userRef = myFirebaseRef.child(MyApplication.usersString);
        userRef.child(MyApplication.getUser().getU_ID()).setValue(MyApplication.getUser());

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

    public void findUsersTournaments() {

        storedTournaments.clear();
        Firebase tournamentRef = myFirebaseRef.child(MyApplication.tournamentsString);
        Firebase usersRef = userRef.child(MyApplication.usersString);
        final String uid = MyApplication.getUser().getU_ID();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String child_uid = (String) child.getKey();
                    if (uid.equals(child_uid)) {
                        User user;
                        user = child.getValue(User.class);
                        MyApplication.getUser().setStoredTournamentsID(user.getStoredTournamentsID());

                        SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
                        prefs.edit().putStringSet("tournaments", new HashSet<String>(user.getStoredTournamentsID())).apply();
                        prefs.edit().commit();

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        tournamentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String createdBy = (String) child.child("createdBy_uID").getValue();

                    if (createdBy.equals(MyApplication.getUser().getU_ID())) {
                        for (String s : MyApplication.getUser().getStoredTournamentsID()) {
                            if (s.equals(child.getKey())) {
                                Tournament t = child.getValue(Tournament.class);
                                storedTournaments.add(t);
                            }
                        }
                    }
                    doneFetchingTournaments = true;
                    refreshLayout.setRefreshing(false);
                    updateList();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    private void updateList(){

        adapter.notifyDataSetChanged();
    }

    void refreshList() {
        // Load items
        findUsersTournaments();

    }


}













