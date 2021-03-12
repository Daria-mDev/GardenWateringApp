package com.example.androidui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WaterAreaViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CheckBox water_control_sprinkle;
    TextView controlTemp, controlHumidity;
    ArrayList<WeatherCast> weathercasts = new ArrayList<>();
    ListView weatherCastList;
    ArrayList<WaterArea> locationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();

        controlTemp = findViewById(R.id.controlTemp);
        controlHumidity = findViewById(R.id.controlHumidity);

        weatherCastList = (ListView) findViewById(R.id.weathercastList);

        WeatherForecastAdapter stateAdapter = new WeatherForecastAdapter(this, R.layout.item_weathercast, weathercasts);
        weatherCastList.setAdapter(stateAdapter);

        recyclerView = findViewById(R.id.waterAreaList);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new WaterAreaViewAdapter(this, locationsList);
        recyclerView.setAdapter(mAdapter);

        water_control_sprinkle = findViewById(R.id.water_control_sprinkle);

    }

    private void setInitialData(){
        weathercasts.add(new WeatherCast ("February 17, 2021",  R.drawable.cloudy,"19 \u00B0"));
        weathercasts.add(new WeatherCast ("February 18, 2021",  R.drawable.rain,"21 \u00B0"));
        weathercasts.add(new WeatherCast ("February 17, 2021",  R.drawable.partly_cloudy,"23 \u00B0"));

        locationsList.add(new WaterArea(false,"BACKYARD",false));
        locationsList.add(new WaterArea(false,"BACK PATIO",true));
        locationsList.add(new WaterArea(false,"FRONT YARD",false));
        locationsList.add(new WaterArea(true,"GARDEN",false));
        locationsList.add(new WaterArea(false,"PORCH",true));
    }

    public void onSprinkleClick(View view) {
        if(!water_control_sprinkle.isChecked()) {
            for(WaterArea waterArea : locationsList) {
                waterArea.setPlanWater(false);
            }
            WaterAreaViewAdapter areaViewAdapter = new WaterAreaViewAdapter(this,locationsList);
            recyclerView.setAdapter(areaViewAdapter);
        }
    }

}