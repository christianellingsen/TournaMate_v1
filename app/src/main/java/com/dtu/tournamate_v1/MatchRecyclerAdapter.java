package com.dtu.tournamate_v1;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtu.tournamate_v1.activeTournament.ActiveMatchScore_frag;

import java.util.ArrayList;

/**
 * Created by ce on 18-04-2016.
 */
public class MatchRecyclerAdapter extends RecyclerView.Adapter<MatchRecyclerAdapter.ViewHolder> {

    Fragment fragment;
    ArrayList<Match> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mView;
        TextView t1Name;
        TextView t2Name;
        TextView t1Score;
        TextView t2Score;
        ImageView t1Badge;
        ImageView t2Badge;

        public ViewHolder(View v) {
            super(v);
            t1Name = (TextView) v.findViewById(R.id.match_t1_name);
            t2Name = (TextView) v.findViewById(R.id.match_t2_name);
            t1Score = (TextView) v.findViewById(R.id.match_t1_score);
            t2Score = (TextView) v.findViewById(R.id.match_t2_score);
            t1Badge = (ImageView) v.findViewById(R.id.t1_badge);
            t2Badge = (ImageView) v.findViewById(R.id.t2_badge);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MatchRecyclerAdapter(ArrayList<Match> myDataset, Fragment fragment) {
        this.mDataset = myDataset;
        this.fragment = fragment;
        Log.d("Match list","Size of match list:"+mDataset.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MatchRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_list_with_score2, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Match m = mDataset.get(position);
        holder.t1Name.setText(m.getT1().getTeamName());
        holder.t2Name.setText(m.getT2().getTeamName());
        holder.t1Score.setText(""+m.getScoreT1());
        holder.t2Score.setText("" + m.getScoreT2());

        if (m.isPlayed()){
            if (m.getScoreT1()>m.getScoreT2()){
                holder.t1Badge.setImageResource(R.drawable.win_badge);
                holder.t2Badge.setImageResource(R.drawable.lost_bagde);
            }
            else if (m.getScoreT2() >m.getScoreT1()){
                holder.t1Badge.setImageResource(R.drawable.lost_bagde);
                holder.t2Badge.setImageResource(R.drawable.win_badge);
            }
            else {
                holder.t1Badge.setImageResource(R.drawable.draw_badge);
                holder.t2Badge.setImageResource(R.drawable.draw_badge);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("RecyclerView", "You clicked on " + position);
                //mRecycleViewAdapter.getRef(position).removeValue();
                MyApplication.activeMatch = position + 1;
                fragment.getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new ActiveMatchScore_frag())
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


