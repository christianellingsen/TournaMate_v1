package com.dtu.tournamate_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dtu.tournamate_v1.Settings.Settings;
import com.dtu.tournamate_v1.createNewTournament.NewTournament_frag;
import com.dtu.tournamate_v1.login.WelcomeScreen_akt;
import com.dtu.tournamate_v1.spectateTournament.SearchTournaments;
import com.firebase.client.Firebase;

public class MainMenu_akt extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView userName;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_akt);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My tournaments");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                MyApplication.resumingTournament = false;
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_frame, new NewTournament_frag())
                        .commit();

                fabOnOff(0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        userName = (TextView) header.findViewById(R.id.drawerTop_userName);
        userName.setText(MyApplication.getUser().getFullName());

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Go to profile
                Log.d("Drawer", "Drawer header click");
            }
        });


        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_frame, new ListStoredMatched_frag())
                .commit();

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
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("My tournaments");
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

        if(id == R.id.action_home) {
            getSupportActionBar().setTitle("My tournaments");
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_frame, new ListStoredMatched_frag())
                    .commit();
            fabOnOff(1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_my_tournaments) {
            getSupportActionBar().setTitle("My tournaments");
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_frame, new ListStoredMatched_frag())
                    .commit();
            fabOnOff(1);
        }

        else if (id==R.id.nav_search){
            getSupportActionBar().setTitle("Search");
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.main_frame, new SearchTournaments())
                    .commit();
            fabOnOff(0);
        }

        else if (id==R.id.nav_my_profile){
            Snackbar snackbar = Snackbar
                    .make(getCurrentFocus(), "Not implemented yet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        else if (id== R.id.nav_settings){
            getSupportActionBar().setTitle("Settings");
            Snackbar snackbar = Snackbar
                    .make(getCurrentFocus(), "Not implemented yet", Snackbar.LENGTH_LONG);
            snackbar.show();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new Settings())
                    .commit();
            fabOnOff(0);
        }

        else if (id == R.id.nav_log_out) {
            Firebase ref = new Firebase(MyApplication.firebase_URL);
            ref.unauth();
            startActivity(new Intent(this, WelcomeScreen_akt.class));
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void fabOnOff(int state){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (state==0){
            fab.setVisibility(View.GONE);
        }
        else {
            fab.setVisibility(View.VISIBLE);
        }
    }

}
