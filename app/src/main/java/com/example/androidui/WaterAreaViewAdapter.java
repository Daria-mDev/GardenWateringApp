package com.example.androidui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WaterAreaViewAdapter extends RecyclerView.Adapter<WaterAreaViewAdapter.WaterAreaViewHolder>{
    ArrayList<WaterArea> locationsList;
    Context context;

    public WaterAreaViewAdapter(Context context, ArrayList<WaterArea> locationsList){
        this.context = context;
        this.locationsList = locationsList;
    }
    @NonNull
    @Override
    public WaterAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_water_location, parent, false);
        return new WaterAreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterAreaViewHolder holder, int position) {
        holder.waterArea.setText(locationsList.get(position).getLocation());
        holder.planWater.setChecked(locationsList.get(position).getPlanWater());
        holder.isWatering.setChecked(locationsList.get(position).getIsWatering());

        if(holder.isWatering.isChecked()) {
            holder.waterArea.setTextColor(holder.waterArea.getHighlightColor());
        }
        holder.planWater.setContentDescription("Tomorrow watering" + holder.planWater.getText());
        holder.isWatering.setContentDescription("Today watering" + holder.isWatering.getText());
    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }

    public static class WaterAreaViewHolder extends RecyclerView.ViewHolder {
        private CheckBox planWater;
        private TextView waterArea;
        private CheckBox isWatering;

        public WaterAreaViewHolder(View view){
            super(view);
            planWater = view.findViewById(R.id.plan_water);
            waterArea = view.findViewById(R.id.location);
            isWatering = view.findViewById(R.id.is_watering);
        }
    }
}


