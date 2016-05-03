package com.dtu.tournamate_v1.activeTournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.tournamate_v1.R;

/**
 * Created by ce on 02-05-2016.
 */
public class infoCard_frag extends Fragment {

    TextView header, text;
    View root;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        root = i.inflate(R.layout.info_card, container, false);

        header = (TextView) root.findViewById(R.id.info_header);
        text = (TextView) root.findViewById(R.id.info_text);

        header.setText("Rank table information");
        text.setText("P: Played matches\n" +
                "W: Number of matches won\n" +
                "D: Number of matches draw\n" +
                "L: Number of matches lost\n" +
                "P: Team total points");
        return root;
    }
}
