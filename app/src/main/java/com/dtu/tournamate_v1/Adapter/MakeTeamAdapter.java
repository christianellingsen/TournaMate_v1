package com.dtu.tournamate_v1.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.dtu.tournamate_v1.createNewTournament.MakeTeams2_frag;
import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by ce on 04-05-2016.
 */
public class MakeTeamAdapter extends RecyclerView.Adapter<MakeTeamAdapter.ViewHolder> {

    ArrayList<Team> teamsList = new ArrayList<>();
    int teamSize;
    Fragment fragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mView;
        TextView teamName;
        ListView playerList;


        public ViewHolder(View v) {
            super(v);
            teamName = (TextView) v.findViewById(R.id.make_team_teamName);
            playerList = (ListView) v.findViewById(R.id.make_team_playerList);
            mView = v;
        }
    }

    public MakeTeamAdapter(ArrayList<Team> dataSet, int teamSize, Fragment fragment) {
        this.teamsList = dataSet;
        this.teamSize = teamSize;
        this.fragment = fragment;

    }

    @Override
    public MakeTeamAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.make_team_element, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.teamName.setText(teamsList.get(position).getTeamName());
        final ArrayList<Player> teamMembers= teamsList.get(position).getTeamMembers();
        //Log.d("TeamsAdapter","Teams # " + (position+1) + " size: " +teamsList.get(position).getTeamMembers().size());

        viewHolder.teamName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                // Get the layout inflater
                LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_add_player, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(view);
                builder.setTitle("Delete team?");
                builder.setMessage("Players joins the last team");
                builder.create();

                final AlertDialog dialog = builder.show();

                Button done = (Button) view.findViewById(R.id.make_team_add_player_add);
                done.setText("Delete");
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int numberOfPlayers = teamsList.get(position).getTeamMembers().size();

                        for (int p = 0; p<numberOfPlayers;p++){
                            teamsList.get(teamsList.size()-1).addTeamMember(teamsList.get(position).getTeamMembers().get(p));
                        }
                        teamsList.remove(position);
                        updateTeamlist();
                        dialog.dismiss();
                    }
                });

                Button cancel = (Button) view.findViewById(R.id.make_team_add_player_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Spinner teamSpinner = (Spinner) view.findViewById(R.id.dialog_add_player_team);
                teamSpinner.setVisibility(View.GONE);

                EditText et = (EditText) view.findViewById(R.id.dialog_add_player_name);
                et.setVisibility(View.GONE);


                return false;
            }
        });


        viewHolder.teamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.playerToMoveSelected){
                    teamsList.get(position).addTeamMember(MyApplication.playerToMove);
                    teamsList.get(MyApplication.playerMoveFromTeam).getTeamMembers().remove(MyApplication.playerMovePlayerIndex);
                    MyApplication.playerToMoveSelected =false;
                    MyApplication.playerToMove =null;
                    updateTeamlist();
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                    LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
                    final View view = inflater.inflate(R.layout.dialog_add_player, null);
                    builder.setView(view);
                    builder.setTitle("Edit team name");
                    builder.create();

                    EditText teamName = (EditText) view.findViewById(R.id.dialog_add_player_name);
                    if (teamsList.get(position).getTeamMembers().size()>0) {
                        teamName.setText("Team "+teamsList.get(position).getTeamMembers().get(0).getName());
                    }
                    else {
                        teamName.setText(teamsList.get(position).getTeamName());
                    }

                    final AlertDialog dialog = builder.show();

                    Button done = (Button) view.findViewById(R.id.make_team_add_player_add);
                    done.setText("Done");
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText et = (EditText) view.findViewById(R.id.dialog_add_player_name);
                            String name = et.getText().toString();
                            teamsList.get(position).setTeamName(name);

                            updateTeamlist();
                            dialog.dismiss();
                        }
                    });

                    Button cancel = (Button) view.findViewById(R.id.make_team_add_player_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    Spinner teamSpinner = (Spinner) view.findViewById(R.id.dialog_add_player_team);
                    teamSpinner.setVisibility(View.GONE);
                }

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(fragment.getActivity(),R.layout.make_team_player_element,R.id.make_team_playerName,teamMembers) {
            @Override
            public View getView(final int i, View cachedView, ViewGroup parent) {
                View view = super.getView(i, cachedView, parent);

                final TextView name = (TextView) view.findViewById(R.id.make_team_playerName);
                final CardView card = (CardView) view.findViewById(R.id.make_team_player_card);
                name.setText(teamMembers.get(i).getName());

                if (MyApplication.playerToMoveSelected && MyApplication.playerMoveFromTeam==position &&
                        MyApplication.playerMovePlayerIndex==i){
                    //Log.d("TeamList","PlayerSelected. Invert color");
                    //name.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    card.setCardBackgroundColor(Color.parseColor("#00ffcc"));

                }
                //Log.d("Player","Team: "+(position+1)+" player name: "+ name.getText().toString() );

                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContext(),"Clicked on: "+name.getText().toString() + " int: "+ i,Toast.LENGTH_SHORT).show();

                        if (!MyApplication.playerToMoveSelected) {
                            MyApplication.playerToMove = teamsList.get(position).getTeamMembers().get(i);
                            MyApplication.playerMoveFromTeam = position;
                            MyApplication.playerMovePlayerIndex = i;
                            MyApplication.playerToMoveSelected = true;
                        }
                        else {
                            if (MyApplication.playerMoveFromTeam==position && MyApplication.playerMovePlayerIndex==i){
                                Toast.makeText(getContext(),"Same player selected",Toast.LENGTH_SHORT).show();
                                MyApplication.playerToMoveSelected = false;
                                MyApplication.playerToMove =null;
                            }
                            else {
                                teamsList.get(position).addTeamMember(MyApplication.playerToMove);
                                teamsList.get(MyApplication.playerMoveFromTeam).getTeamMembers().remove(MyApplication.playerMovePlayerIndex);
                                MyApplication.playerToMoveSelected =false;
                                MyApplication.playerToMove =null;
                            }
                        }

                        updateTeamlist();

                    }
                });

                return view;
            }
        };

        ViewGroup.LayoutParams params = viewHolder.playerList.getLayoutParams();
        //Log.d("Team list","viewHolder.playerList.getDividerHeight() = " + viewHolder.playerList.getDividerHeight());
        //params.height = (viewHolder.playerList.getDividerHeight() * (teamSize));
        params.height = 120*teamsList.get(position).getTeamMembers().size();
        //Log.d("Team list"," params.height: "+ params.height);
        viewHolder.playerList.setLayoutParams(params);

        viewHolder.playerList.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    public void updateTeamlist(){
        //Log.d("TeamsAdapter","Update list");
        this.notifyDataSetChanged();

        Firebase ref = new Firebase(MyApplication.firebase_URL);
        // Firebase update teams
        for (Team t : teamsList){
            Firebase teamRef = ref.child(MyApplication.teamsString).child(t.getTeamID());
            teamRef.setValue(t);
        }
        Firebase tournamentRef = ref.child(MyApplication.tournamentsString).child(MyApplication.getActiveTournament().getT_ID());
        tournamentRef.child("numberOfTeams").setValue(MyApplication.getActiveTournament().getNumberOfTeams());


    }

}