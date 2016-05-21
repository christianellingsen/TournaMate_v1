package com.dtu.tournamate_v1.createNewTournament;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Tournament;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chris on 26-04-2016.
 */
public class SetNameAndType_frag extends Fragment implements View.OnClickListener {

    EditText t_name_et;
    Button create_b;
    CircleImageView groupPlay_img, knockOut_img;
    Switch setNumberOfTeams, setTeamSize, open;
    TextView numberOfTeams_tv, teamSize_tv;

    CardView group_cv, knockOut_cv, setNumber_cv, number_cv, setSize_cv, size_cv, open_cv;

    View root;
    private boolean cancel = false;
    private boolean typeSelected = false;
    private View focusView = null;

    int numberOfTeams = 2;
    int teamSize = 1;

    Tournament activeTournament;

    Firebase ref = new Firebase(MyApplication.firebase_URL);

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        root = i.inflate(R.layout.set_name_and_type, container, false);

        t_name_et = (EditText) root.findViewById(R.id.set_tournament_name_et);
        create_b = (Button) root.findViewById(R.id.createTournamentButton);
        groupPlay_img = (CircleImageView) root.findViewById(R.id.groupPlayImage);
        knockOut_img = (CircleImageView) root.findViewById(R.id.knock_out_image);
        setNumberOfTeams = (Switch) root.findViewById(R.id.set_team_number_switch);
        numberOfTeams_tv = (TextView) root.findViewById(R.id.number_of_teams_value);
        setTeamSize = (Switch) root.findViewById(R.id.team_size_switch);
        teamSize_tv = (TextView) root.findViewById(R.id.team_size_value);
        open = (Switch) root.findViewById(R.id.open_to_join_switch);

        group_cv = (CardView) root.findViewById(R.id.group_play_cv);
        knockOut_cv = (CardView) root.findViewById(R.id.knock_out_cv);
        setNumber_cv = (CardView) root.findViewById(R.id.set_number_cv);
        number_cv = (CardView) root.findViewById(R.id.number_cv);
        setSize_cv = (CardView) root.findViewById(R.id.set_size_cv);
        size_cv = (CardView) root.findViewById(R.id.size_cv);
        open_cv = (CardView) root.findViewById(R.id.open_cv);

        activeTournament = MyApplication.getActiveTournament();

        SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        setTeamSize.setChecked(prefs.getBoolean("setTeamSize",false));
        setNumberOfTeams.setChecked(prefs.getBoolean("setNumberOfTeams",false));
        teamSize = prefs.getInt("teamSize",1);
        numberOfTeams = prefs.getInt("numberOfTeams",2);

        teamSize_tv.setText(""+teamSize);
        numberOfTeams_tv.setText(""+numberOfTeams);

        if (!setTeamSize.isChecked()) {
            size_cv.setAlpha((float) 0.4);
        }
        if (!setNumberOfTeams.isChecked()) {
            number_cv.setAlpha((float) 0.4);
        }
        number_cv.setClickable(setTeamSize.isChecked());
        size_cv.setClickable(setTeamSize.isChecked());

        group_cv.setOnClickListener(this);
        knockOut_cv.setOnClickListener(this);
        setNumber_cv.setOnClickListener(this);
        number_cv.setOnClickListener(this);
        setSize_cv.setOnClickListener(this);
        size_cv.setOnClickListener(this);
        open_cv.setOnClickListener(this);

        create_b.setOnClickListener(this);

        MyApplication.teams.clear();

        return root;
    }

    @Override
    public void onClick(View v) {

        cancel = false;
        focusView = null;

        if (v==group_cv){
            typeSelected =true;
            groupPlay_img.setImageResource(R.drawable.save);
            knockOut_img.setImageResource(R.drawable.knoc_out);
            create_b.setAlpha(1);
            create_b.setClickable(true);
            //MyApplication.type="Round Robin";
            activeTournament.setType("Round Robin");
        }

        else if (v==knockOut_cv){
            typeSelected =true;
            knockOut_img.setImageResource(R.drawable.save);
            groupPlay_img.setImageResource(R.drawable.group_play);
            create_b.setAlpha(1);
            create_b.setClickable(true);
            //MyApplication.type="Single Elimination";
            activeTournament.setType("Single Elimination");
        }

        else if (v==setNumber_cv){
            setNumberOfTeams.setChecked(!setNumberOfTeams.isChecked());
            number_cv.setClickable(setNumberOfTeams.isChecked());
            if (setNumberOfTeams.isChecked()){
                number_cv.setAlpha(1);
            }
            else {
                number_cv.setAlpha((float)0.4);
            }
        }

        else if (v==setSize_cv){
            setTeamSize.setChecked(!setTeamSize.isChecked());
            size_cv.setClickable(setTeamSize.isChecked());
            if (setTeamSize.isChecked()){
                size_cv.setAlpha(1);
            }
            else {
                size_cv.setAlpha((float)0.4);
            }
        }

        else if (v==number_cv && setNumberOfTeams.isChecked()){

            final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getContext())
                    .minValue(1)
                    .maxValue(99)
                    .defaultValue(numberOfTeams)
                    .backgroundColor(Color.WHITE)
                    .separatorColor(getResources().getColor(R.color.colorPrimary))
                    .textColor(Color.BLACK)
                    .textSize(20)
                    .enableFocusability(false)
                    .wrapSelectorWheel(true)
                    .build();

            new AlertDialog.Builder(getActivity())
                    .setTitle("Set fixed number of teams")
                    .setView(numberPicker)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            numberOfTeams = numberPicker.getValue();
                            //Snackbar.make(getView(), "You picked : " + numberPicker.getValue(), Snackbar.LENGTH_LONG).show();
                            numberOfTeams_tv.setText(""+numberOfTeams);
                        }
                    })
                    .show();

        }

        else if (v==size_cv && setTeamSize.isChecked()) {
            final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getContext())
                    .minValue(1)
                    .maxValue(99)
                    .defaultValue(teamSize)
                    .backgroundColor(Color.WHITE)
                    .separatorColor(getResources().getColor(R.color.colorPrimary))
                    .textColor(Color.BLACK)
                    .textSize(20)
                    .enableFocusability(false)
                    .wrapSelectorWheel(true)
                    .build();

            new AlertDialog.Builder(getActivity())
                    .setTitle("Set fixed number of teams")
                    .setView(numberPicker)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            teamSize = numberPicker.getValue();
                            //Snackbar.make(getView(), "You picked : " + numberPicker.getValue(), Snackbar.LENGTH_LONG).show();
                            teamSize_tv.setText(""+teamSize);
                        }
                    })
                    .show();
        } else if (v==open_cv){
            open.setChecked(!open.isChecked());
            //MyApplication.isOpenToJoin = open.isChecked();
            activeTournament.setIsOpenToJoin(open.isChecked());
        }

        else if (v == create_b) {

            String t_name = t_name_et.getText().toString();
            t_name_et.setError(null);

            if (t_name.length() < 2) {
                t_name_et.setError("Name to short");
                focusView = t_name_et;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                //MyApplication.tournamentName = t_name;
                //MyApplication.numberOfTeams = numberOfTeams;
                //MyApplication.teamSize = teamSize;
                //MyApplication.isOpenToJoin = open.isChecked();

                Firebase tournamentRef = ref.child(MyApplication.tournamentsString);
                Firebase newTournamentRef = tournamentRef.push();
                Log.d("Firebase", "Created new tournament");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String date = dateFormat.format(new Date());

                activeTournament.setName(t_name);
                activeTournament.setCreatedBy_uID(MyApplication.getUser().getU_ID());
                activeTournament.setCreatedBy(MyApplication.getUser().getFullName());
                activeTournament.setIsDone(false);
                activeTournament.setWinner("No winner yet");
                activeTournament.setCreatedAt(date);
                activeTournament.setIsOpenToJoin(open.isChecked());
                activeTournament.setIsStarted(false);
                activeTournament.setNumberOfTeams(numberOfTeams);
                activeTournament.setTeamSize(teamSize);
                activeTournament.setNumberOfMatches(0);
                activeTournament.setT_ID(newTournamentRef.getKey());

                //MyApplication.tournamentRef_firebase = activeTournament.getT_ID();
                //Log.d("FirebaseRef","ref: "+ MyApplication.tournamentRef_firebase);
                //activeTournament.setT_ID(newTournamentRef.getKey());

                newTournamentRef.setValue(activeTournament);

                // Create dummy match
                Firebase matchesRef = ref.child(MyApplication.matchesString);
                Firebase newMatchesRef = matchesRef.push();
                Match m = new Match();
                m.setMatchTitle("Dummy");
                m.setMatchID(newMatchesRef.getKey());
                m.setTournamentID(MyApplication.getActiveTournament().getT_ID());
                m.setPlayed(false);
                newMatchesRef.setValue(m);

                MyApplication.matchList.add(m);
                MyApplication.getActiveTournament().setNumberOfMatches(1);
                ref.child(MyApplication.tournamentsString).child(MyApplication.getActiveTournament().getT_ID()).child("numberOfMatches").setValue(1);

                saveTournamnetToUser();

                for (Player p : MyApplication.players){
                    p.setSelected(false);
                }

                AddPlayer_frag fragment = new AddPlayer_frag();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            }
        }
    }

    public void saveTournamnetToUser(){

        int index = MyApplication.getUser().getStoredTournamentsID().size();
        MyApplication.getUser().getStoredTournamentsID().add(index,MyApplication.getActiveTournament().getT_ID());

        SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        prefs.edit().putStringSet("tournaments", new HashSet<String>(MyApplication.getUser().getStoredTournamentsID())).apply();
        prefs.edit().putBoolean("setTeamSize",setTeamSize.isChecked()).apply();
        prefs.edit().putBoolean("setNumberOfTeams",setNumberOfTeams.isChecked()).apply();
        prefs.edit().putInt("teamSize",teamSize).apply();
        prefs.edit().putInt("numberOfTeams",numberOfTeams).apply();
        prefs.edit().commit();

        Firebase userRef = ref.child(MyApplication.usersString);
        userRef.child(MyApplication.getUser().getU_ID()).setValue(MyApplication.getUser());

    }
}
