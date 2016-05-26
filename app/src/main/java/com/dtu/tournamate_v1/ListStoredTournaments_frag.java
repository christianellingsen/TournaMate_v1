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
import android.support.v7.widget.DefaultItemAnimator;
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

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class ListStoredTournaments_frag extends Fragment {

    private String TAG = this.getTag();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    ArrayList<Tournament> storedTournaments = new ArrayList();
    ArrayList<String> pendingDelete = new ArrayList();
    ArrayList<Match> fetchedMatchesFirebase = new ArrayList();

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);

    SwipeRefreshLayout refreshLayout;

    //FirebaseRecyclerAdapter<Tournament, viewHolder> fireBaseAdapter;
    TournamentRecyclerAdapter adapter;

    View root;
    View view;

    private Paint p = new Paint();

    private int storedTournamnetsSize = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.list_stored_matched_fragment, container, false);

        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.stored_matches_swipeRefresh);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.stored_matches_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(200);
        //animator.setRemoveDuration(0);
        mRecyclerView.setItemAnimator(animator);
       // mRecyclerView.setItemAnimator(new SlideInRightAnimator());

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        refreshList();

        adapter = new TournamentRecyclerAdapter(storedTournaments,this,mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setClickable(true);

        if (MyApplication.getUser().getStoredTournamentsID().size()>0){
            findUsersTournaments();
        }

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //fireBaseAdapter.cleanup();
    }


    public void findUsersTournaments() {
        //Log.d(TAG, "findUsersTournaments called");
        Firebase tournamentRef = myFirebaseRef.child(MyApplication.tournamentsString);
        Query storedTournamentsQuery = tournamentRef.orderByChild("createdBy_uID").equalTo(MyApplication.getUser().getU_ID()).getRef();

        storedTournamentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storedTournaments.clear();
                //Log.d(TAG, "findUsersTournaments onDataChange called. Size of storedTournaments: "+MyApplication.getUser().getStoredTournamentsID().size());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tournament t = child.getValue(Tournament.class);
                    for (String s : MyApplication.getUser().getStoredTournamentsID()) {
                        if (s.equals(t.getT_ID())) {
                            //Log.d(TAG, "stored tournamnets: "+s);
                            storedTournaments.add(t);
                            //adapter.notifyItemInserted(storedTournaments.size()-1);
                        }
                    }
                }

                //doneFetchingTournaments = true;
                refreshLayout.setRefreshing(false);
                //Log.d(TAG,"Update called");
                updateList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    private void updateList(){
        //Log.d(TAG, "size of T array: "+storedTournaments.size());
        adapter.notifyDataSetChanged();
    }

    void refreshList() {
        // Load items
        findUsersTournaments();
        //updateList();
    }


    @Override
    public void onResume() {
        super.onResume();
        findUsersTournaments();
    }
}













