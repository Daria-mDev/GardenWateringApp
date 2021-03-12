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
    private CheckBox water_control_sprinkle;
    TextView controlTemp, controlHumidity;
    ArrayList<WeatherCast> weatherCasts = new ArrayList<>();
    ListView weatherCastList;
    ArrayList<WaterArea> locationsList = new ArrayList<>();
    MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();

        controlTemp = findViewById(R.id.controlTemp);
        controlHumidity = findViewById(R.id.controlHumidity);

        weatherCastList = findViewById(R.id.weathercastList);

        myThread = new MyThread( this);
        myThread.start();

        recyclerView = findViewById(R.id.waterAreaList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        WaterAreaViewAdapter mAdapter = new WaterAreaViewAdapter(this, locationsList);
        recyclerView.setAdapter(mAdapter);

        water_control_sprinkle = findViewById(R.id.water_control_sprinkle);

    }

    private void setInitialData() {
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