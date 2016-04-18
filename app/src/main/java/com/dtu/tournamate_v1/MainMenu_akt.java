package com.dtu.tournamate_v1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dtu.tournamate_v1.createNewTournament.NewTournament_akt;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu_akt extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Tournament> storedTournaments = new ArrayList();
    ArrayList<String> storedTournamentsNames = new ArrayList();
    private DBAdapter dbAdapter = new DBAdapter(this);

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase tournamentRef = myFirebaseRef.child("Tournaments");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_akt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent (getApplication().getBaseContext(), NewTournament_akt.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerAdapter<Tournament, MessageViewHolder> fireBaseAdapter =
                new FirebaseRecyclerAdapter<Tournament, MessageViewHolder>(
                        Tournament.class,
                        R.layout.tournament_listelement,
                        MessageViewHolder.class,
                        tournamentRef
                ) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder messageViewHolder, Tournament t, int i) {
                        messageViewHolder.tName.setText(t.getName());
                        messageViewHolder.tDate.setText(t.getCreatedAt());
                    }
                };
        mRecyclerView.setAdapter(fireBaseAdapter);


    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tName;
        TextView tDate;
        public MessageViewHolder(View v){
            super(v);
            tName = (TextView) v.findViewById(R.id.tournament_list_name);
            tDate = (TextView) v.findViewById(R.id.tournament_list_date);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_akt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateList(){

        storedTournamentsNames.clear();
        storedTournaments.clear();

        Log.d("RecyclerView", "Lenght of empty arraylist: " + storedTournamentsNames.size());

        final ProgressDialog progress;
        progress = ProgressDialog.show(this, "Finding stored tournaments", "", true);
        progress.setCancelable(false);
        progress.show();

        dbAdapter.open();
        Cursor c = dbAdapter.getAllRows(MyApplication.DATABASE_TABLE_TOURNAMENTS);
        if (c.moveToFirst()){
            do {
                String temp_name = c.getString(MyApplication.COL_TOURNAMENTS_NAME);
                String date = c.getString(MyApplication.COL_TOURNAMENTS_DATE);
                String isDone = c.getString(MyApplication.COL_TOURNAMENTS_DONE);
                long id = Long.parseLong(c.getString(MyApplication.COL_ROWID));
                String objectID = c.getString(MyApplication.COL_TOURNAMENTS_PARSE);
                Tournament t = new Tournament();
                t.setName(temp_name);
                t.setCreatedAt(date);
                t.setObjectID_sql(id);
                t.setObjectID(objectID);
                if(isDone.equals("true")){
                    t.setIsDone(true);
                }
                else{
                    t.setIsDone(false);
                }
                storedTournaments.add(t);
                storedTournamentsNames.add(temp_name);

            } while (c.moveToNext());
            c.close();
        }
        dbAdapter.close();
        progress.dismiss();
    }


}
