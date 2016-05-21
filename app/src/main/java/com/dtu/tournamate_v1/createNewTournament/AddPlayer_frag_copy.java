package com.dtu.tournamate_v1.createNewTournament;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Christian on 05-04-2015.
 */
public class AddPlayer_frag_copy extends Fragment implements View.OnClickListener {

    private Button addPlayerToList_b, remove_b, done_b;
    private ToggleButton selectAll_tb;
    private EditText newName_et;
    private ListView lv;
    private View rod;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState){

        rod = i.inflate(R.layout.add_players, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add selectedPlayers");

        addPlayerToList_b = (Button) rod.findViewById(R.id.buttonAddPlayer);
        remove_b = (Button) rod.findViewById(R.id.buttonRemove);
        selectAll_tb = (ToggleButton) rod.findViewById(R.id.toggleButtonF);
        newName_et = (EditText) rod.findViewById(R.id.editTextName);
        done_b = (Button) rod.findViewById(R.id.buttonDonePlayers);
        lv = (ListView) rod.findViewById(R.id.listViewSelectPlayers);

        addPlayerToList_b.setOnClickListener(this);
        remove_b.setOnClickListener(this);
        selectAll_tb.setOnClickListener(this);
        newName_et.setOnClickListener(this);
        done_b.setOnClickListener(this);

        newName_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.add_player_ime || id == EditorInfo.IME_NULL) {
                    addPlayer(newName_et.getText().toString());
                    updateList();
                    return true;
                }
                return false;
            }
        });


        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);

        MyApplication.playerSet.addAll(playerList.getStringSet("Saved selectedPlayers",new HashSet<String>()));


        updateList();

        return rod;
    }

    @Override
    public void onClick(View v) {

        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        SharedPreferences.Editor pl_editor = playerList.edit();

        if (v==newName_et){

        }
        else if (v == remove_b){
            int cntChoice = lv.getCount();
            SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();

            for(int i = 0; i < cntChoice; i++){

                if(sparseBooleanArray.get(i)) {
                    MyApplication.playerSet.remove(lv.getItemAtPosition(i).toString());
                }
            }
            pl_editor.putStringSet("Saved selectedPlayers",MyApplication.playerSet).commit();
            updateList();
        }

        else if (v== addPlayerToList_b){
            String newName = newName_et.getText().toString();
            addPlayer(newName);
            updateList();
        }
        else if (v==done_b){

            MyApplication.selectedPlayerSet.clear();

            int cntChoice = lv.getCount();
            SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();

            for(int i = 0; i < cntChoice; i++){

                if(sparseBooleanArray.get(i)) {

                    MyApplication.selectedPlayerSet.add(lv.getItemAtPosition(i).toString());
                }
            }

            if(MyApplication.selectedPlayerSet.size()<2){
                Toast.makeText(getActivity(),getString(R.string.addplayer_2players),Toast.LENGTH_SHORT).show();
            }
            else {

                if (MyApplication.selectedPlayerSet.size()!= (int)(MyApplication.getActiveTournament().getNumberOfTeams())/MyApplication.getActiveTournament().getTeamSize()){
                    //MyApplication.numberOfTeams = (int)MyApplication.selectedPlayerSet.size()/MyApplication.teamSize;
                    MyApplication.getActiveTournament().setNumberOfTeams((int)MyApplication.selectedPlayerSet.size()/MyApplication.getActiveTournament().getTeamSize());
                    //Log.d("Team size","Number of teams: " + MyApplication.numberOfTeams + " team size: "+MyApplication.teamSize);
                }

                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new MakeTeams2_frag())
                        .addToBackStack(null)
                        .commit();
            }
        }

        else if(v==selectAll_tb){
            int cntChoice = lv.getCount();
            for (int i = 0; i<cntChoice;i++){
                if (selectAll_tb.isChecked()){
                    lv.setItemChecked(i,true);
                }
                else {lv.setItemChecked(i,false);}
            }
        }
    }

    public void updateList(){

        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        ArrayList<String> playerNames = new ArrayList<>(playerList.getStringSet("Saved selectedPlayers",new HashSet<String>()));
        Collections.sort(playerNames);
        Log.d("Debug","Liste fra shared prefs:"+playerNames);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice,playerNames);
        lv.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        SharedPreferences.Editor pl_editor = playerList.edit();
        pl_editor.putStringSet("Saved selectedPlayers",MyApplication.playerSet);
        pl_editor.commit();
    }

    public void addPlayer(String name){
        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        SharedPreferences.Editor pl_editor = playerList.edit();

        name.replaceAll("\\s+$", "");
        newName_et.setText("");

        MyApplication.playerSet.add(name);
        pl_editor.putStringSet("Saved selectedPlayers",MyApplication.playerSet).commit();
        Log.d("Debug", "Singleton playerSet: " + MyApplication.playerSet.toString());


    }
}
