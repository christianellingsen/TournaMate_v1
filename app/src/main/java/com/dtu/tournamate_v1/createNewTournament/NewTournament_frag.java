package com.dtu.tournamate_v1.createNewTournament;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.activeTournament.ActiveMatchScore_frag;
import com.dtu.tournamate_v1.activeTournament.TournamentWinner_frag;

/**
 * Created by Christian on 06-04-2015.
 */
public class NewTournament_frag extends Fragment {

    String selectedType;
    private View root;

    @Override
    public View onCreateView(LayoutInflater ViewInflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = ViewInflater.inflate(R.layout.list_stored_matched_fragment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New tournamnet");


        if (!MyApplication.resumingTournament) {

            if (savedInstanceState == null) {
                SetNameAndType_frag fragment = new SetNameAndType_frag();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            }
        }
        else {
            if (!MyApplication.isDone){
                ActiveMatchScore_frag fragment = new ActiveMatchScore_frag();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            }
            else {
                TournamentWinner_frag fragment = new TournamentWinner_frag();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            }
        }

        return root;
    }

}
