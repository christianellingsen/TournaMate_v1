package com.dtu.tournamate_v1.activeTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;

import java.util.ArrayList;

/**
 * Created by Christian on 19-04-2015.
 */
public class RankList_frag extends Fragment {

    private static final String TAG = "Ranklist";
    ImageView info;
    FrameLayout infoCardFrame, shadowFrame;
    ListView lv;
    ArrayList<Team> teams;
    ArrayList<String> teamsStringList;
    View rod;

    private boolean showInfo = false;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.rank_list, container, false);
        infoCardFrame = (FrameLayout) rod.findViewById(R.id.info_container);
        shadowFrame = (FrameLayout) rod.findViewById(R.id.shadowFrame);
        info = (ImageView) rod.findViewById(R.id.tableInfoView);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Rank list");
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);



        shadowFrame.setVisibility(View.GONE);
        infoCardFrame.setVisibility(View.GONE);

        teams = MyApplication.teams;
        teamsStringList = new ArrayList<>();

        final Animation zoomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        final Animation zoomOut = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_out);
        final Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        final Animation fadeOut = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_out);

        getActivity().getSupportFragmentManager().beginTransaction()
        .replace(R.id.info_container, new infoCard_frag()).commit();

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showInfo) {
                    shadowFrame.setVisibility(View.VISIBLE);
                    infoCardFrame.setVisibility(View.VISIBLE);
                    shadowFrame.startAnimation(fadeIn);
                    infoCardFrame.startAnimation(zoomIn);
                }
                showInfo = !showInfo;
            }
        });

        infoCardFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showInfo) {
                    shadowFrame.startAnimation(fadeOut);
                    //
                    infoCardFrame.startAnimation(zoomOut);
                    infoCardFrame.setVisibility(View.GONE);
                    shadowFrame.setVisibility(View.GONE);
                }
                showInfo = !showInfo;
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Rank");

        MyApplication.rankTeams();

        lv = (ListView) rod.findViewById(R.id.listViewRankTeams);

        for(Team t : teams){
            if(t.getTeamName().equals("Dummy")) {

            }
            else {
                teamsStringList.add(t.getTeamName());
            }
        }



        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.rank_tv,R.id.textViewRankTV_name,teamsStringList) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);

                TextView teamRank = (TextView) view.findViewById(R.id.textViewRankTV_Rank);
                teamRank.setText("" + (position+1));

                TextView played = (TextView) view.findViewById(R.id.textViewRankTV_Played);
                played.setText("" + teams.get(position).getMatechesPlayed());

                TextView won = (TextView) view.findViewById(R.id.textViewRankTV_Won);
                won.setText(""+teams.get(position).getMatchesWon());

                TextView lost = (TextView) view.findViewById(R.id.textViewRankTV_Lost);
                lost.setText(""+teams.get(position).getMatchesLost());

                TextView draw = (TextView) view.findViewById(R.id.textViewRankTV_Draw);
                draw.setText(""+teams.get(position).getMatchesDraw());

                TextView points = (TextView) view.findViewById(R.id.textViewRankTV_Points);
                points.setText("" + teams.get(position).getOverAllScore());

                return view;
            }
        };
        lv.setAdapter(adapter);

        return rod;
    }


    @Override
    public void onStop() {
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsSelected");
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.d(TAG,"R.id.home pressed");
                getActivity().onBackPressed();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
