package com.dtu.tournamate_v1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Tournament;
import com.dtu.tournamate_v1.createNewTournament.AddPlayer_frag;
import com.dtu.tournamate_v1.spectateTournament.SearchTournaments;
import com.dtu.tournamate_v1.spectateTournament.SpectateTournament;

import java.util.List;

/**
 * Created by Christian on 21-05-2016.
 */
public class FoundTournamentsAdapter extends RecyclerView.Adapter<FoundTournamentsAdapter.ViewHolder> {

    private List<Tournament> tList;
    private SearchTournaments fragment;

    public FoundTournamentsAdapter(List<Tournament> tournaments, SearchTournaments frag) {
        this.tList = tournaments;
        this.fragment=frag;

    }


    @Override
    public FoundTournamentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.found_tournament_listelement, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.tName.setText(tList.get(pos).getName());
        viewHolder.tDate.setText(tList.get(pos).getCreatedAt());
        viewHolder.tCreatedBy.setText(tList.get(pos).getCreatedBy());
        if (tList.get(pos).getIsOpenToJoin()){
            viewHolder.tOpen.setText("Open to join");
        }
        else {
            viewHolder.tOpen.setText("Closed");
        }

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("SearchTournamnetAdapter","Clicked on "+pos + " and name: "+tList.get(pos).getName());

                if (tList.get(pos).getIsStarted()){
                    MyApplication.spectateT_ID = tList.get(pos).getT_ID();
                    MyApplication.spectateT_name = tList.get(pos).getName();

                    fragment.getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new SpectateTournament())
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    Toast.makeText(fragment.getActivity(),"Tournament not started yet",Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return tList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView tName,tDate,tCreatedBy,tOpen;

        public ViewHolder(View v) {
            super(v);
            tName = (TextView) v.findViewById(R.id.found_tournament_list_name);
            tDate = (TextView) v.findViewById(R.id.found_tournament_list_date);
            tCreatedBy = (TextView) v.findViewById(R.id.found_tournament_list_createdBy);
            tOpen = (TextView) v.findViewById(R.id.found_tournament_list_open);
            mView = v;
        }
    }

    // method to access in activity after updating selection
    public List<Tournament> getTournamentList() {
        return tList;
    }

}

