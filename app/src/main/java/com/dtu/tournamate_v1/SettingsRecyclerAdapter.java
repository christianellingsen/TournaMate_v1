package com.dtu.tournamate_v1;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ce on 18-04-2016.
 */
public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> {

    LinkedHashMap<String,String> settingsMap = new LinkedHashMap<String,String>();
    private ArrayList<String> settingsOptions,settingsValues;
    private int[] settingTypes;
    Fragment settingsFrag;

    public static final int VALUE = 0;
    //public static final int SWITCH = 1;

    Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);
    Firebase userRef = myFirebaseRef.child("users");
    Firebase thisUserRef = userRef.child(MyApplication.getUser().getU_ID());

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ValueViewHolder extends ViewHolder {
        TextView settingsValue, settingsOption;

        public ValueViewHolder(View v) {
            super(v);
            this.settingsOption = (TextView) v.findViewById(R.id.settings_option);
            this.settingsValue = (TextView) v.findViewById(R.id.settings_value);
        }
    }

    public class SwitchViewHolder extends ViewHolder {
        Switch settingsSwitch;

        public SwitchViewHolder(View v) {
            super(v);
            this.settingsSwitch = (Switch) v.findViewById(R.id.settings_switch);
        }
    }

    public SettingsRecyclerAdapter(LinkedHashMap<String, String> settingsMap, int[] settingTypes, Fragment settingsFrag) {
        this.settingsMap = settingsMap;
        this.settingTypes = settingTypes;
        this.settingsFrag = settingsFrag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == VALUE) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.settings_value, viewGroup, false);

            return new ValueViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.settings_toggle, viewGroup, false);
            return new SwitchViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == VALUE) {
            final ValueViewHolder holder = (ValueViewHolder) viewHolder;
            holder.settingsOption.setText((new ArrayList<String>(settingsMap.keySet())).get(position));
            holder.settingsValue.setText((new ArrayList<String>(settingsMap.values())).get(position));

            holder.settingsValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "Tryk på value", Toast.LENGTH_SHORT).show();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(settingsFrag.getContext());
                    LayoutInflater inflater = settingsFrag.getActivity().getLayoutInflater();
                    final View v = inflater.inflate(R.layout.dialog_add_player, null);
                    builder.setView(v);
                    builder.setTitle("Edit value");
                    builder.create();

                    final EditText newValue = (EditText) v.findViewById(R.id.dialog_add_player_name);
                    newValue.setText(holder.settingsValue.getText());
                    final AlertDialog dialog = builder.show();

                    Button done = (Button) v.findViewById(R.id.make_team_add_player_add);
                    done.setText("Save");
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v2) {

                            holder.settingsValue.setText(newValue.getText());

                            if ((new ArrayList<String>(settingsMap.keySet())).get(position).equals("First name")){
                                thisUserRef.child("firstName").setValue(newValue.getText().toString());
                                MyApplication.getUser().setFirstName(newValue.getText().toString());
                            }
                            else if((new ArrayList<String>(settingsMap.keySet())).get(position).equals("Last name")){
                                thisUserRef.child("lastName").setValue(newValue.getText().toString());
                                MyApplication.getUser().setLastName(newValue.getText().toString());
                            }




                            dialog.dismiss();

                            // Lav firebase kald
                        }
                    });

                    Button cancel = (Button) v.findViewById(R.id.make_team_add_player_cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v2) {
                            dialog.dismiss();
                        }
                    });

                    Spinner teamSpinner = (Spinner) v.findViewById(R.id.dialog_add_player_team);
                    teamSpinner.setVisibility(View.GONE);
                    

                }
            });
        }
        else {
            SwitchViewHolder holder = (SwitchViewHolder) viewHolder;
            holder.settingsSwitch.setText((new ArrayList<String>(settingsMap.keySet())).get(position));
            if ((new ArrayList<String>(settingsMap.values())).get(position).equals("1")){
                holder.settingsSwitch.setChecked(true);
            }
        }



    }

    @Override
    public int getItemCount() {
        return settingTypes.length;
    }

    @Override
    public int getItemViewType(int position) {
        return settingTypes[position];
    }



}
