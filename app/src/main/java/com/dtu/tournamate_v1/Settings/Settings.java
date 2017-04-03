package com.dtu.tournamate_v1.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dtu.tournamate_v1.MyApplication;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.SettingsRecyclerAdapter;
import com.dtu.tournamate_v1.User;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ce on 02-05-2016.
 */

public class Settings extends Fragment {

        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;

        private SettingsRecyclerAdapter adapter;

        Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);



        View root;

        public static final int VALUE = 0;
        public static final int SWITCH = 1;

        LinkedHashMap<String,String> settingsMap = new LinkedHashMap<String,String>();
        private ArrayList<String> settingsOptions,settingsValues;
        private int[] settingTypes = {VALUE,VALUE,SWITCH,SWITCH};

        private User user = MyApplication.getUser();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            settingsMap.put("First name",user.getFirstName());
            settingsMap.put("Last name",user.getLastName());
            settingsMap.put("testSwitchOff","0");
            settingsMap.put("testSwitchOn","1");

            // Inflate the layout for this fragment
            root = inflater.inflate(R.layout.settings, container, false);

            mRecyclerView = (RecyclerView) root.findViewById(R.id.settings_recycler_view);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);


            adapter = new SettingsRecyclerAdapter(settingsMap,settingTypes,this);
            mRecyclerView.setAdapter(adapter);
            //updateList();
            //mRecyclerView.setAdapter(fireBaseAdapter);
            mRecyclerView.setClickable(true);

            return root;
        }
}





