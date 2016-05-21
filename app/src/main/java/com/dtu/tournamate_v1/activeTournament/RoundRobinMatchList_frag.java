package com.dtu.tournamate_v1.activeTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MatchRecyclerAdapter;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by Christian on 19-04-2015.
 */
public class RoundRobinMatchList_frag extends Fragment {

    ListView lv;
    ArrayList<Match> matches;
    ArrayList<String> matchesStringList;
    View root;

    // ******** UPDATE *******

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MatchRecyclerAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    Firebase ref = new Firebase(MyApplication.firebase_URL);
    Firebase matchesRef = ref.child(MyApplication.matchesString);

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        root = i.inflate(R.layout.list_stored_matched_fragment, container, false);
        matches = MyApplication.matchList;
        matchesStringList = new ArrayList<>();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Scheduled matches");

        mRecyclerView = (RecyclerView) root.findViewById(R.id.stored_matches_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.stored_matches_swipeRefresh);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new MatchRecyclerAdapter(matches,this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setClickable(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        return root;
    }

    void refreshList() {
        // Load items
        refreshLayout.setRefreshing(false);
    }
}
