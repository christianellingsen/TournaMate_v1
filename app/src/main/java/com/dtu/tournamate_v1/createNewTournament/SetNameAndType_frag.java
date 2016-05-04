package com.dtu.tournamate_v1.createNewTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chris on 26-04-2016.
 */
public class SetNameAndType_frag extends Fragment implements View.OnClickListener {

    EditText t_name_et;
    Button create_b;
    CircleImageView groupPlay_img, knockOut_img;
    Switch setNumberOfTeams, setTeamSize, open;
    TextView numberOfTeams, teamSize;

    CardView group_cv, knockOut_cv, setNumber_cv, number_cv, setSize_cv, size_cv, open_cv;

    View root;
    private boolean cancel = false;
    private boolean typeSelected = false;
    private View focusView = null;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        root = i.inflate(R.layout.set_name_and_type, container, false);

        t_name_et = (EditText) root.findViewById(R.id.set_tournament_name_et);
        create_b = (Button) root.findViewById(R.id.createTournamentButton);
        groupPlay_img = (CircleImageView) root.findViewById(R.id.groupPlayImage);
        knockOut_img = (CircleImageView) root.findViewById(R.id.knock_out_image);
        setNumberOfTeams = (Switch) root.findViewById(R.id.set_team_number_switch);
        numberOfTeams = (TextView) root.findViewById(R.id.number_of_teams_value);
        setTeamSize = (Switch) root.findViewById(R.id.team_size_switch);
        teamSize = (TextView) root.findViewById(R.id.team_size_value);
        open = (Switch) root.findViewById(R.id.open_to_join_switch);

        group_cv = (CardView) root.findViewById(R.id.group_play_cv);
        knockOut_cv = (CardView) root.findViewById(R.id.knock_out_cv);
        setNumber_cv = (CardView) root.findViewById(R.id.set_number_cv);
        number_cv = (CardView) root.findViewById(R.id.number_cv);
        setSize_cv = (CardView) root.findViewById(R.id.set_size_cv);
        size_cv = (CardView) root.findViewById(R.id.size_cv);
        open_cv = (CardView) root.findViewById(R.id.open_cv);



        group_cv.setOnClickListener(this);
        knockOut_cv.setOnClickListener(this);
        setNumber_cv.setOnClickListener(this);
        number_cv.setOnClickListener(this);
        setSize_cv.setOnClickListener(this);
        size_cv.setOnClickListener(this);
        open_cv.setOnClickListener(this);

        create_b.setOnClickListener(this);

        t_name_et.setFocusable(true);
        t_name_et.hasFocus();

        return root;
    }

    @Override
    public void onClick(View v) {

        cancel = false;
        focusView = null;

        if (v==group_cv){
            typeSelected =true;
            groupPlay_img.setImageResource(R.drawable.ic_check_black_36dp);
            knockOut_img.setImageResource(R.drawable.user_pic);
            create_b.setAlpha(1);
            create_b.setClickable(true);
            MyApplication.type="Round Robin";
        }

        else if (v==knockOut_cv){
            typeSelected =true;
            knockOut_img.setImageResource(R.drawable.ic_check_black_36dp);
            groupPlay_img.setImageResource(R.drawable.user_pic);
            create_b.setAlpha(1);
            create_b.setClickable(true);
            MyApplication.type="Single Elimination";
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
            //TODO
        }

        else if (v==size_cv && setTeamSize.isChecked()){
            //TODO
        }

        else if (v==open_cv){
            //TODO
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
                MyApplication.tournamentName = t_name;
                AddPlayer_frag fragment = new AddPlayer_frag();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            }
        }
    }
}
