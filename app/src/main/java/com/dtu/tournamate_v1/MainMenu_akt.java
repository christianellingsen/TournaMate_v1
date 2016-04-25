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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class MainMenu_akt extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Tournament> storedTournaments = new ArrayList();
    ArrayList<String> storedTournamentsNames = new ArrayList();
    private DBAdapter dbAdapter = new DBAdapter(this);

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase tournamentRef = myFirebaseRef.child("Tournaments");

    FirebaseRecyclerAdapter<Tournament, viewHolder> fireBaseAdapter;

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

        fireBaseAdapter =
                new FirebaseRecyclerAdapter<Tournament, viewHolder>(
                        Tournament.class,
                        R.layout.tournament_listelement,
                        viewHolder.class,
                        tournamentRef
                ) {
                    @Override
                    protected void populateViewHolder(viewHolder mViewHolder, Tournament t, final int i) {
                        mViewHolder.tName.setText(t.getName());
                        mViewHolder.tDate.setText(t.getCreatedAt());
                        mViewHolder.mView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Log.w("RecyclerView", "You clicked on " + i);
                                //mRecycleViewAdapter.getRef(position).removeValue();
                                String tID = fireBaseAdapter.getRef(i).getKey();
                                fetchTournament(tID);

                                ProgressDialog progressDialog = ProgressDialog.show(MainMenu_akt.this, "",
                                        "Loading. Please wait...", true);
                                try {
                                    Thread.sleep(2000);                 //1000 milliseconds is one second.
                                } catch(InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }
                                Log.d("Firebase", "Resuming tournament");
                                Log.d("Firebase","Tournament name " +MyApplication.tournamentName);
                                Log.d("Firebase","Number of matches "+ MyApplication.matchList.size());
                                Log.d("Firebase", "Number of teams " + MyApplication.teams.size());
                                Intent i = new Intent(getBaseContext(), NewTournament_akt.class);
                                startActivity(i);

                                progressDialog.dismiss();

                            }
                        });
                    }

                };
        mRecyclerView.setAdapter(fireBaseAdapter);
        mRecyclerView.setClickable(true);


    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView tName;
        TextView tDate;
        public viewHolder(View v){
            super(v);
            tName = (TextView) v.findViewById(R.id.tournament_list_name);
            tDate = (TextView) v.findViewById(R.id.tournament_list_date);
            mView = v;
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

    public interface RecyclerViewClickListener
    {

        public void recyclerViewListClicked(View v, int position);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fireBaseAdapter.cleanup();
    }

    public void fetchTournament(final String objectID){

        MyApplication.resumingTournament = true;
        MyApplication.matchList.clear();
        MyApplication.teams.clear();

        Firebase tournamentRef = myFirebaseRef.child("Tournaments");
        Firebase matchRef = myFirebaseRef.child("Matches");
        Firebase fetchTournamentRef = tournamentRef.child(objectID);

        tournamentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String t_objectID = (String) child.getKey();
                    if(t_objectID == objectID){
                        MyApplication.tournamentID_parse = t_objectID;
                        MyApplication.isDone =(boolean) child.child("isDone").getValue();
                        MyApplication.tournamentName = (String) child.child("name").getValue();
                        MyApplication.type = (String) child.child("type").getValue();
                        Log.d("Firebase", "Tournament found: " + MyApplication.tournamentName);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        matchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String t_objectID = (String) child.getKey();
                    if (t_objectID == objectID) {
                        Match m = child.getValue(Match.class);
                        Log.d("Firebase", "Match found: " + m.getMatchTitle());
                        //MyApplication.isDone =(boolean) child.child("isDone").getValue();
                        //MyApplication.tournamentName = (String) child.child("name").getValue();
                        //MyApplication.type = (String) child.child("type").getValue();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

}
