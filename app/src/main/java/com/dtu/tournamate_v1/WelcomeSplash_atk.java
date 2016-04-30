package com.dtu.tournamate_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.dtu.tournamate_v1.login.Login;
import com.dtu.tournamate_v1.login.WelcomeScreen_akt;
import com.firebase.client.Firebase;

/**
 * Created by Christian on 17-03-2015.
 */
public class WelcomeSplash_atk extends Activity implements Runnable {

    Handler handler = new Handler();
    static WelcomeSplash_atk aktivitetDerVisesNu = null;
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_splash);

        iv = new ImageView(this);
        iv = (ImageView) findViewById(R.id.imageView_welcomesplash);
        iv.setImageResource(R.drawable.icon);

        if (savedInstanceState == null) {
            handler.postDelayed(this, 2000); // Kør om 3 sekunder
        }
        aktivitetDerVisesNu = this;
    }

    public void run() {
        startActivity(new Intent(this, WelcomeScreen_akt.class));
        aktivitetDerVisesNu.finish();  // Luk velkomst
        aktivitetDerVisesNu = null;
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacks(this);
    }
}