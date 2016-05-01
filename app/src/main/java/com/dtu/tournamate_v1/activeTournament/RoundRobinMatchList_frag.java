package com.dtu.tournamate_v1.activeTournament;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MatchRecyclerAdapter;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.dtu.tournamate_v1.TournamentRecyclerAdapter;

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

        /**lv = (ListView) root.findViewById(R.id.listView);

        for (Match m : matches){
            Team t1 = m.getT1();
            Team t2 = m.getT2();

            matchesStringList.add(t1.getTeamName() + " VS " + t2.getTeamName());
        }
        Log.d("Debug","Size of matches " +matches.size());
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.match_list_with_score,R.id.textViewMatchList_team,matchesStringList) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);
                Log.d("Debug","Matchlist element: match: "+matchesStringList.get(position)+" number: "+matches.get(position).getMatchNumber());

                TextView matchNumber = (TextView) view.findViewById(R.id.textViewMatchList_number);
                matchNumber.setText(""+matches.get(position).getMatchNumber());

                TextView score = (TextView) view.findViewById(R.id.textViewMatchList_score);
                score.setText(matches.get(position).getScoreT1() + " - " + matches.get(position).getScoreT2());
                score.setTextColor(Color.parseColor("#000000"));
                if (matches.get(position).isPlayed()){
                    score.setTextColor(Color.parseColor("#0FAE02"));
                }

                return view;
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        **/
        return root;
    }

    void refreshList() {
        // Load items
        refreshLayout.setRefreshing(false);

    }
}
