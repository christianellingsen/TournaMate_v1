package com.dtu.tournamate_v1.createNewTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;

/**
 * Created by chris on 26-04-2016.
 */
public class SetNameAndType_frag extends Fragment implements View.OnClickListener {

    EditText t_name_et;
    Button groupPlay_b, elimination_b;
    View root;
    private boolean cancel = false;
    private View focusView = null;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        root = i.inflate(R.layout.set_name_and_type, container, false);

        t_name_et = (EditText) root.findViewById(R.id.set_tournament_name_et);
        groupPlay_b = (Button) root.findViewById(R.id.set_group_play_b);
        elimination_b = (Button) root.findViewById(R.id.set_elimination_b);


        groupPlay_b.setOnClickListener(this);
        elimination_b.setOnClickListener(this);

        t_name_et.setFocusable(true);
        t_name_et.hasFocus();

        return root;
    }

    @Override
    public void onClick(View v) {
        cancel = false;
        focusView = null;

        String t_name =  t_name_et.getText().toString();
        t_name_et.setError(null);

        if(t_name.length()<2){
            t_name_et.setError("Name to short");
            focusView = t_name_et;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            MyApplication.tournamentName = t_name;
            if (v == groupPlay_b) {
                MyApplication.type = "Round Robin";
            } else if (v == elimination_b) {
                MyApplication.type = "Single Elimination";
            }

            AddPlayer_frag fragment = new AddPlayer_frag();
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
        }
    }
}
