package com.dtu.tournamate_v1.spectateTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MatchRecyclerAdapter;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.firebase.client.ChildEventListener;
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
public class SpectateTournament extends Fragment {

    private String TAG = "SpectateTournamnet";

    private TextView tournamentName;
    View root;
    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase matchesRef = myFirebaseRef.child(MyApplication.matchesString);
    FirebaseRecyclerAdapter fireBaseAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MatchRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        root = i.inflate(R.layout.spectate_tournament, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Spectate tournament");

        tournamentName = (TextView) root.findViewById(R.id.spectate_tournament_tName);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.spectate_tournament_recyklerview);
        mRecyclerView.setHasFixedSize(true);

        tournamentName.setText(MyApplication.spectateT_name);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //adapter = new MatchRecyclerAdapter(matches,this);
        //mRecyclerView.setAdapter(adapter);
        mRecyclerView.setClickable(true);

        Query query = matchesRef.orderByChild("tournamentID").equalTo(MyApplication.spectateT_ID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fireBaseAdapter =
                new FirebaseRecyclerAdapter<Match, viewHolder>(
                        Match.class,
                        R.layout.match_list_with_score2,
                        viewHolder.class,
                        query
                ) {
                    @Override
                    protected void populateViewHolder(viewHolder mViewHolder, Match m, final int i) {

                        mViewHolder.t1Name.setText(m.getT1().getTeamName());
                        mViewHolder.t2Name.setText(m.getT2().getTeamName());
                        mViewHolder.t1Score.setText(""+m.getScoreT1());
                        mViewHolder.t2Score.setText("" + m.getScoreT2());
                        mViewHolder.matchNumber.setText("Match # "+m.getMatchNumber());
                        mViewHolder.title.setText(m.getMatchTitle());

                        if (m.isPlayed()){
                            mViewHolder.isPlayedCheck.setImageResource(R.drawable.ic_check_black_36dp);
                            if (m.getScoreT1()>m.getScoreT2()){
                                mViewHolder.t1Badge.setImageResource(R.drawable.win_badge);
                                mViewHolder.t2Badge.setImageResource(R.drawable.lost_bagde);
                            }
                            else if (m.getScoreT2() >m.getScoreT1()){
                                mViewHolder.t1Badge.setImageResource(R.drawable.lost_bagde);
                                mViewHolder.t2Badge.setImageResource(R.drawable.win_badge);
                            }
                            else {
                                mViewHolder.t1Badge.setImageResource(R.drawable.draw_badge);
                                mViewHolder.t2Badge.setImageResource(R.drawable.draw_badge);
                            }
                        }
                    }

                };

        mRecyclerView.setAdapter(fireBaseAdapter);

        return root;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView t1Name;
        TextView t2Name;
        TextView t1Score;
        TextView t2Score;
        ImageView t1Badge;
        ImageView t2Badge;
        TextView matchNumber;
        TextView title;
        ImageView isPlayedCheck;

        public viewHolder(View v) {
            super(v);
            t1Name = (TextView) v.findViewById(R.id.match_t1_name);
            t2Name = (TextView) v.findViewById(R.id.match_t2_name);
            t1Score = (TextView) v.findViewById(R.id.match_t1_score);
            t2Score = (TextView) v.findViewById(R.id.match_t2_score);
            t1Badge = (ImageView) v.findViewById(R.id.t1_badge);
            t2Badge = (ImageView) v.findViewById(R.id.t2_badge);
            matchNumber = (TextView) v.findViewById(R.id.newMatchlist_matchNumber);
            title = (TextView) v.findViewById(R.id.newMatchlist_matchTitle);
            isPlayedCheck = (ImageView) v.findViewById(R.id.isPlayedCheckIV);
            mView = v;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fireBaseAdapter.cleanup();
    }
}
