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
import androidx.lifecycle.ViewModelProviders;
import java.util.List;




public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckBox water_control_sprinkle;
    TextView controlTemp, controlHumidity;
    List<WeatherCast> weatherCasts = new ArrayList<>();
    ListView weatherCastList;
    ArrayList<WaterArea> locationsList = new ArrayList<>();
    WeatherForecastViewModel weatherForecastViewModel;
    WeatherForecastAdapter stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();

        controlTemp = findViewById(R.id.controlTemp);
        controlHumidity = findViewById(R.id.controlHumidity);

        weatherCastList = findViewById(R.id.weathercastList);

        stateAdapter = new WeatherForecastAdapter(this, R.layout.item_weathercast, weatherCasts);
        weatherCastList.setAdapter(stateAdapter);

        setWeatherForecast();

        recyclerView = findViewById(R.id.waterAreaList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        WaterAreaViewAdapter mAdapter = new WaterAreaViewAdapter(this, locationsList);
        recyclerView.setAdapter(mAdapter);

        water_control_sprinkle = findViewById(R.id.water_control_sprinkle);

    }

    private void setInitialData(){
        weatherCasts.add(new WeatherCast ("February 17, 2021",  R.drawable.cloudy,"19 \u00B0"));
        weatherCasts.add(new WeatherCast ("February 18, 2021",  R.drawable.rain,"21 \u00B0"));
        weatherCasts.add(new WeatherCast ("February 17, 2021",  R.drawable.partly_cloudy,"23 \u00B0"));

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

    private void setWeatherForecast() {
        weatherForecastViewModel = ViewModelProviders.of(this).get(WeatherForecastViewModel.class);
        weatherForecastViewModel.getWeatherCastMutableLiveData().observe(this, castList -> {
            if (castList != null) {
                weatherCasts = castList;
                stateAdapter.setWeatherCasts(weatherCasts);
            }
        });

        weatherForecastViewModel.getCurrentWeatherMutableLiveData().observe(this, currentWeather -> {
            if (currentWeather != null) {
                String tmpTemp = (int)currentWeather.component1() + "\u00B0";
                String tmpHumidity = currentWeather.component2()+ "%";

                controlTemp.setText(tmpTemp);
                controlHumidity.setText(tmpHumidity);
            }
        });
        weatherForecastViewModel.getWeatherForecast();
 }

}

