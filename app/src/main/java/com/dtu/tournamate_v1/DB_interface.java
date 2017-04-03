package com.dtu.tournamate_v1;

import android.widget.TextView;

import com.firebase.client.Firebase;

/**
 * Created by ALIDBS on 07-02-2017.
 */

public class DB_interface{

    private String username;

    public DB_interface(){

    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }




}

