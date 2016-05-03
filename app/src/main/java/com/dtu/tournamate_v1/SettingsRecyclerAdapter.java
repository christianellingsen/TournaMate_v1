package com.dtu.tournamate_v1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by ce on 18-04-2016.
 */
public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> {

    LinkedHashMap<String,String> settingsMap = new LinkedHashMap<String,String>();
    private ArrayList<String> settingsOptions,settingsValues;
    private int[] settingTypes;

    public static final int VALUE = 0;
    //public static final int SWITCH = 1;



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

    public SettingsRecyclerAdapter(LinkedHashMap<String, String> settingsMap, int[] settingTypes) {
        this.settingsMap = settingsMap;
        this.settingTypes = settingTypes;
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
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == VALUE) {
            ValueViewHolder holder = (ValueViewHolder) viewHolder;
            holder.settingsOption.setText((new ArrayList<String>(settingsMap.keySet())).get(position));
            holder.settingsValue.setText((new ArrayList<String>(settingsMap.values())).get(position));
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
