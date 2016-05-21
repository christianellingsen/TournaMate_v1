package com.dtu.tournamate_v1.Adapter;

import java.util.List;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.dtu.tournamate_v1.Player;
import com.dtu.tournamate_v1.R;
import com.dtu.tournamate_v1.createNewTournament.AddPlayer_frag;

/**
 * Created by Christian on 21-05-2016.
 */
public class AddPlayerAdaper extends RecyclerView.Adapter<AddPlayerAdaper.ViewHolder> {

    private List<Player> pList;
    private AddPlayer_frag fragment;

    public AddPlayerAdaper(List<Player> players, AddPlayer_frag frag) {
        this.pList = players;
        this.fragment=frag;

    }


    @Override
    public AddPlayerAdaper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_checkbox, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.tvName.setText(pList.get(position).getName());
        viewHolder.chkSelected.setChecked(pList.get(position).isSelected());
        viewHolder.chkSelected.setTag(pList.get(position));

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = viewHolder.chkSelected;
                cb.setChecked(!cb.isChecked());
                Player player = pList.get(pos);

                player.setSelected(cb.isChecked());
                pList.get(pos).setSelected(cb.isChecked());



            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               // Log.d("AddPlayerAdapter","onLongClick");
                fragment.showControls();
                return false;
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return pList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView tvName;
        public CheckBox chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.add_player_tvName);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.add_player_chkSelected);
            cardView = (CardView) itemLayoutView.findViewById(R.id.add_player_cardview);

        }

    }

    // method to access in activity after updating selection
    public List<Player> getPlayerList() {
        return pList;
    }

}

