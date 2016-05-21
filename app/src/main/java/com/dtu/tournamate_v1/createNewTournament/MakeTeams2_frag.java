package com.dtu.tournamate_v1.createNewTournament;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.tournamate_v1.Adapter.AutofitRecyclerView;
import com.dtu.tournamate_v1.Adapter.MakeTeamAdapter;
import com.dtu.tournamate_v1.Adapter.MarginDecorator;
import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ce on 04-05-2016.
 */
public class MakeTeams2_frag extends Fragment implements View.OnClickListener {

    ArrayList<Player> players;
    ArrayList<String> selectedPlayers;
    ArrayList<Team> teams;

    CircleImageView addPlayer_img, addTeam_img;

    int teamSize, numberOfTeams;
    int addToTeamIndex = 0;
    AutofitRecyclerView teamsRecyclerView;
    MakeTeamAdapter adapter;

    Button done;

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.make_teams2, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Make teams");

        teamsRecyclerView = (AutofitRecyclerView) root.findViewById(R.id.make_teams_teamsRecyclerview);

        addPlayer_img = (CircleImageView) root.findViewById(R.id.make_team_add_player);
        addTeam_img = (CircleImageView) root.findViewById(R.id.make_team_add_team);
        done = (Button) root.findViewById(R.id.make_teams_done_b);



        teamSize = MyApplication.getActiveTournament().getTeamSize();
        numberOfTeams = MyApplication.getActiveTournament().getNumberOfTeams();

        players = new ArrayList<>();
        selectedPlayers = new ArrayList<>(MyApplication.selectedPlayerSet);
        for (String name : selectedPlayers){
            Player p = new Player();
            p.setName(name);
            players.add(p);
        }

        if (MyApplication.teams.size()<1){
            makeRandomTeams();
        }

        teams = MyApplication.teams;
        adapter = new MakeTeamAdapter(teams,teamSize,this);
        teamsRecyclerView.addItemDecoration(new MarginDecorator(getActivity()));
        teamsRecyclerView.setAdapter(adapter);

        addPlayer_img.setOnClickListener(this);
        addTeam_img.setOnClickListener(this);
        done.setOnClickListener(this);



        return root;
    }


    public void makeRandomTeams() {
        Log.d("MakeTeams","############ START MAKETEAMS ###########");
        int remainder = players.size() % teamSize;
        if (remainder != 0) {
            numberOfTeams += 1;
        }
        ArrayList<Player> shuffelPlayers = new ArrayList<>(players);
        Collections.shuffle(shuffelPlayers);
        MyApplication.teams.clear();

        int numberOfMembersAdded = 0;
        while (MyApplication.teams.size()<numberOfTeams){
            //Log.d("MakeTeams","Making a team");

            Team t = new Team();
            if (shuffelPlayers.size()-numberOfMembersAdded < teamSize){
                for(int i = numberOfMembersAdded; i<shuffelPlayers.size();i++){
                    t.addTeamMember(shuffelPlayers.get(numberOfMembersAdded));
                    numberOfMembersAdded++;
                    //Log.d("MakeTeams","Making uneven team");
                }
            }
            else {
                for (int i = 0; i < teamSize; i++) {
                    t.addTeamMember(shuffelPlayers.get(numberOfMembersAdded));
                    numberOfMembersAdded++;
                    //Log.d("MakeTeams","Making a normal size team");
                }
            }
            t.setTeamName("Team # "+  (MyApplication.teams.size()+1));

            // Firebase push

            Firebase ref = new Firebase(MyApplication.firebase_URL);
            Firebase teamRef = ref.child(MyApplication.teamsString);
            Firebase newTeamRef = teamRef.push();
            t.setTeamID(newTeamRef.getKey());
            t.setTournamentID(MyApplication.getActiveTournament().getT_ID());
            MyApplication.teams.add(t);
            newTeamRef.setValue(t);

            Log.d("MakeTeams","Team: "+t.getTeamName());
            //Log.d("MakeTeams","team counter: "+(teamNumberCounter-1));
            //Log.d("MakeTeams","team members added : "+(numberOfMembersAdded));

        }
        Log.d("MakeTeams","########## DONE MAKING TEAMS ###############");
    }

    @Override
    public void onClick(final View v) {
        if (v==addPlayer_img){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_add_player, null);
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view);
            builder.setTitle("Add player");
            builder.create();

            final AlertDialog dialog = builder.show();

            Button add = (Button) view.findViewById(R.id.make_team_add_player_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et = (EditText) view.findViewById(R.id.dialog_add_player_name);
                    String name = et.getText().toString();
                    Player p = new Player();
                    p.setName(name);
                    teams.get(addToTeamIndex).addTeamMember(p);

                    SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
                    SharedPreferences.Editor pl_editor = playerList.edit();
                    name.replaceAll("\\s+$", "");
                    MyApplication.playerSet.add(name);
                    pl_editor.putStringSet("Saved players",MyApplication.playerSet).commit();

                    adapter.updateTeamlist();
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


            ArrayList<String> teamNames = new ArrayList<>();
            for (Team t : teams){
                teamNames.add(t.getTeamName());
            }

            Spinner teamSpinner = (Spinner) view.findViewById(R.id.dialog_add_player_team);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.dialog_spinner_text, teamNames);
            dataAdapter.setDropDownViewResource(R.layout.dialog_spinner_text);

            // attaching data adapter to spinner
            teamSpinner.setAdapter(dataAdapter);
            teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(), "Team: " + teams.get(position).getTeamName().toString(), Toast.LENGTH_SHORT).show();
                    addToTeamIndex = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        else if (v==addTeam_img){
            Team t = new Team();
            t.setTeamName("Team # "+(teams.size()+1));
            t.setTournamentID(MyApplication.getActiveTournament().getT_ID());
            Firebase ref = new Firebase(MyApplication.firebase_URL);
            Firebase matchesRef = ref.child(MyApplication.teamsString).push();
            t.setTeamID(matchesRef.getKey());
            MyApplication.teams.add(t);
            MyApplication.getActiveTournament().setNumberOfTeams(MyApplication.getActiveTournament().getNumberOfTeams()+1);
            adapter.updateTeamlist();
        }

        else if (v==done){

            getFragmentManager().beginTransaction()
                    //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.main_frame, new TournamentReady_frag())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
