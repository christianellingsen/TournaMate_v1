package com.dtu.tournamate_v1.createNewTournament;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.tournamate_v1.Adapter.AddPlayerAdaper;
import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Christian on 05-04-2015.
 */
public class AddPlayer_frag extends Fragment implements View.OnClickListener {

    private String TAG = "AddPlayer_frag";

    private Button done_b, addPlayerToList_b;
    private EditText newName_et;
    private View rod;

    private LinearLayout controlButtons;
    private LinearLayout markAll,deleteMarked,hideControls;
    private TextView markAll_tv;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public boolean allSelected = false;
    public boolean showingControls = false;

    Firebase ref = new Firebase(MyApplication.firebase_URL);
    Firebase playerRef = ref.child(MyApplication.playersString);
    Firebase userRef = ref.child(MyApplication.usersString);

    ArrayList<Player> players = MyApplication.players;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState){

        rod = i.inflate(R.layout.add_players, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add selectedPlayers");

        newName_et = (EditText) rod.findViewById(R.id.editTextName);
        done_b = (Button) rod.findViewById(R.id.buttonDonePlayers);
        addPlayerToList_b = (Button) rod.findViewById(R.id.buttonAddPlayer);

        controlButtons = (LinearLayout) rod.findViewById(R.id.add_play_control_buttons);
        markAll = (LinearLayout) rod.findViewById(R.id.add_player_markAll);
        deleteMarked = (LinearLayout) rod.findViewById(R.id.add_player_DeleteSelected);
        hideControls = (LinearLayout) rod.findViewById(R.id.add_player_Hide);
        markAll_tv = (TextView) rod.findViewById(R.id.add_player_selectAll_tv);

        addPlayerToList_b.setOnClickListener(this);
        newName_et.setOnClickListener(this);
        done_b.setOnClickListener(this);
        markAll.setOnClickListener(this);
        deleteMarked.setOnClickListener(this);
        hideControls.setOnClickListener(this);

        mRecyclerView = (RecyclerView) rod.findViewById(R.id.add_player_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new AddPlayerAdaper(players,AddPlayer_frag.this);
        mRecyclerView.setAdapter(mAdapter);




        /**
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
        **/


        //SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        //MyApplication.playerSet.addAll(playerList.getStringSet("Saved selectedPlayers",new HashSet<String>()));

        //updateList();

        return rod;
    }

    @Override
    public void onClick(View v) {

        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        SharedPreferences.Editor pl_editor = playerList.edit();

        if (v==newName_et){

        }
        else if (v== addPlayerToList_b) {
            String newName = newName_et.getText().toString();
            newName.replaceAll("\\s+$", "");
            Player newPlayer = new Player();
            newPlayer.setName(newName);
            newPlayer.setSelected(true);
            addPlayer(newPlayer);
        }

        else if (v==done_b){
            List<Player> pList = ((AddPlayerAdaper) mAdapter).getPlayerList();
            int numberOfSelectedPlayers = 0;
            for (int i = 0; i < pList.size(); i++) {
                Player player = pList.get(i);
                if (player.isSelected() == true) {
                    numberOfSelectedPlayers++;
                }

            }

            if (numberOfSelectedPlayers!= (int)(MyApplication.getActiveTournament().getNumberOfTeams())/MyApplication.getActiveTournament().getTeamSize()){
                //MyApplication.numberOfTeams = (int)MyApplication.selectedPlayerSet.size()/MyApplication.teamSize;
                MyApplication.getActiveTournament().setNumberOfTeams((int)numberOfSelectedPlayers/MyApplication.getActiveTournament().getTeamSize());
                //Log.d("Team size","Number of teams: " + MyApplication.numberOfTeams + " team size: "+MyApplication.teamSize);
            }

            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new MakeTeams2_frag())
                    .addToBackStack(null)
                    .commit();

        }
        else if (v==markAll){
            for (Player player : players){
                player.setSelected(!allSelected);
            }
            allSelected=!allSelected;
            mAdapter.notifyDataSetChanged();

            //showControls();
            if (allSelected){
                markAll_tv.setText("Mark none");
            }
            else {
                markAll_tv.setText("Mark all");
            }

        }
        else if (v==deleteMarked){
            deleteSelected();
        }
        else if (v==hideControls){
            showControls();
        }
        /**
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
        }**/

    }

    public void updateList(){

        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        ArrayList<String> playerNames = new ArrayList<>(playerList.getStringSet("Saved selectedPlayers",new HashSet<String>()));
        Collections.sort(playerNames);
        Log.d("Debug","Liste fra shared prefs:"+playerNames);
        //ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice,playerNames);
        //lv.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        SharedPreferences.Editor pl_editor = playerList.edit();
        pl_editor.putStringSet("Saved selectedPlayers",MyApplication.playerSet);
        pl_editor.commit();
    }

    public void addPlayer(Player player){
        //SharedPreferences playerList = getActivity().getSharedPreferences("PlayerList", Context.MODE_PRIVATE);
        //SharedPreferences.Editor pl_editor = playerList.edit();

        newName_et.setText("");

        Firebase newPlayerRef = playerRef.push();
        player.setP_ID(newPlayerRef.getKey());
        newPlayerRef.setValue(player);

        MyApplication.players.add(player);
        mAdapter.notifyDataSetChanged();

        MyApplication.getUser().getStoredPlayers().add(player.getP_ID());
        userRef.child(MyApplication.getUser().getU_ID()).setValue(MyApplication.getUser());

        //pl_editor.putStringSet("Saved selectedPlayers",MyApplication.playerSet).commit();
        //Log.d("Debug", "Singleton playerSet: " + MyApplication.playerSet.toString());
    }

    public void showControls(){
        if (showingControls){
            final Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
            controlButtons.setAnimation(slideUp);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    controlButtons.setVisibility(View.GONE);
                }
            }, 100);

        }
        else {
            final Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
            controlButtons.setVisibility(View.VISIBLE);
            controlButtons.setAnimation(slideDown);

        }
        showingControls=!showingControls;
    }

    public void deleteSelected(){

        List<Player> pList = ((AddPlayerAdaper) mAdapter).getPlayerList();

        for (int i = 0; i < pList.size(); i++) {
            Player player = pList.get(i);
            if (player.isSelected() == true) {
                //playerRef.child(player.getP_ID()).setValue(null);
                MyApplication.getUser().getStoredPlayers().remove(player.getP_ID());
                userRef.child(MyApplication.getUser().getU_ID()).setValue(MyApplication.getUser());
                players.remove(player);
            }

        }
        mAdapter.notifyDataSetChanged();
        showControls();
    }
}
