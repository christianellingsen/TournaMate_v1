package com.dtu.tournamate_v1.spectateTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.tournamate_v1.Adapter.FoundTournamentsAdapter;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Tournament;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;


/**
 * Created by Christian on 21-05-2016.
 */
public class SearchTournaments extends Fragment{

    private String TAG = "SearchTournamnet";

    private View root;

    private SearchView searchView;
    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private ArrayList<Tournament> foundTournamnets = new ArrayList<>();

    Firebase ref = new Firebase(MyApplication.firebase_URL);
    Firebase tournamentRef = ref.child(MyApplication.tournamentsString);
    Query tournamnetNameQuery;
    private String tournamnetName ="";

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState){

        root = i.inflate(R.layout.search_tournamnets, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search");

        searchView = (SearchView) root.findViewById(R.id.search_tournament_field);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.search_tournaments_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FoundTournamentsAdapter(foundTournamnets,SearchTournaments.this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setClickable(true);

        searchView.setIconified(false);
        searchView.setQueryHint("Tournament name");

        //***setOnQueryTextListener***
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                tournamnetName = query;
                Log.d(TAG,"OnSearchClick. Query: "+ tournamnetName);
                //tournamnetNameQuery = tournamentRef.orderByChild("name").startAt(tournamnetName);
                makeQuery();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return root;
    }

    public void makeQuery(){

        foundTournamnets.clear();

        tournamnetNameQuery = tournamentRef.orderByChild("name");
        tournamnetNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tournament t = child.getValue(Tournament.class);
                    if (t.getName().toLowerCase().contains(tournamnetName.toLowerCase()))
                    foundTournamnets.add(t);
                }
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
