package com.dtu.tournamate_v1.createNewTournament;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
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

/**
 * Created by Christian on 06-04-2015.
 */
public class NewTournament_akt extends Activity{

    String selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tournament);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new_tournament);
        setSupportActionBar(toolbar);

        if (!MyApplication.resumingTournament) {

            final AlertDialog setName_dialog = new AlertDialog.Builder(NewTournament_akt.this).create();

            setName_dialog.setTitle(getString(R.string.newTournament_dialogHeader));
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.set_tournemnt_name_dialog,  (ViewGroup) findViewById(R.id.dialog_root_id));
            setName_dialog.setView(dialogView);

            final EditText name = (EditText) dialogView.findViewById(R.id.dialog_t_name_et);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, MyApplication.tournamnetTypes);

            Spinner type = (Spinner) dialogView.findViewById(R.id.dialog_t_type_spinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedType = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            type.setAdapter(adapter);
            type.setSelection(0);



            Button ok_b = (Button) dialogView.findViewById(R.id.dialog_t_ok);

            ok_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = name.getText().toString();
                    MyApplication.tournamentName = value;
                    MyApplication.type = selectedType;
                    setName_dialog.dismiss();
                }
            });

            setName_dialog.show();



            if (savedInstanceState == null) {
                AddPlayer_frag fragment = new AddPlayer_frag();
                getFragmentManager().beginTransaction()
                        .add(R.id.fragmentContent, fragment)
                        .commit();
            }
        }
        else {
            ActiveMatchScore_frag fragment = new ActiveMatchScore_frag();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContent, fragment)
                    .commit();
        }
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
