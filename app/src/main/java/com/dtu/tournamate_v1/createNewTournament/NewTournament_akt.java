package com.dtu.tournamate_v1.createNewTournament;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dtu.tournamate_v1.DBAdapter;
import com.dtu.tournamate_v1.Match;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.Team;
import com.dtu.tournamate_v1.activeTournament.ActiveMatchScore_frag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Christian on 06-04-2015.
 */
public class NewTournament_akt extends Activity{

    DBAdapter db_adapter = new DBAdapter(this);
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
        //updateOnDevice();
    }

    public void updateOnDevice(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date  = dateFormat.format(new Date());
        openDB();
        db_adapter.updateRowTournament(MyApplication.tournamentID_sql,MyApplication.tournamentName,MyApplication.type,MyApplication.activeMatch,MyApplication.matchesPlayed, String.valueOf(MyApplication.isDone).toString() ,String.valueOf(MyApplication.isOnline),MyApplication.tournamentID_parse,date);
        Log.d("DB", "Tournament updated. Name: " + MyApplication.tournamentName +" type: "+ MyApplication.type +" activematch: "+ MyApplication.activeMatch +" played: "+ MyApplication.matchesPlayed +" done: "+ String.valueOf(MyApplication.isDone).toString() +" online: "+ String.valueOf(MyApplication.isOnline) +" date: "+ date);
        for(Match m : MyApplication.matchList){
            Team t1 = m.getT1();
            Team t2 = m.getT2();
            db_adapter.updateRowTeam(t1.getTeamID_sql(), t1.getTeamName(), t1.getMatchesWon(), t1.getMatchesLost(), t1.getMatchesDraw(), t1.getOverAllScore(), t1.getMatechesPlayed());
            db_adapter.updateRowTeam(t2.getTeamID_sql(), t2.getTeamName(), t2.getMatchesWon(), t2.getMatchesLost(), t2.getMatchesDraw(), t2.getOverAllScore(), t2.getMatechesPlayed());
            //db_adapter.updateRowMatch(m.getMatchID_sql(),String.valueOf(m.isPlayed()),(int)MyApplication.tournamentID_sql,(int)t1.getTeamID_sql(),(int)t2.getTeamID_sql(), m.getScoreT1(),m.getScoreT2(),m.getMatchNumber());
        }
        closeDB();
    }

    public void openDB(){
        db_adapter = new DBAdapter(this);
        db_adapter.open();
    }

    public void closeDB(){
        db_adapter.close();
    }

}
