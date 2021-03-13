package com.example.androidui;

import androidx.annotation.NonNull;

import com.example.androidui.Loader.CurrentWeatherLoader;
import com.example.androidui.Loader.WeatherForecastLoader;
import com.example.androidui.api.model.CurrentWeather;
import com.example.androidui.api.model.DailyForecast;
import com.example.androidui.api.model.WeatherForecast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckBox water_control_sprinkle;
    TextView controlTemp, controlHumidity;
    ArrayList<WeatherCast> weatherCasts = new ArrayList<>();
    ListView weatherCastList;
    ArrayList<WaterArea> locationsList = new ArrayList<>();

    public static final int CURRENT_WEATHER_LOADER_ID = 24;
    public static final int DAILY_WEATHER_LOADER_ID = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();

        controlTemp = findViewById(R.id.controlTemp);
        controlHumidity = findViewById(R.id.controlHumidity);

        weatherCastList = findViewById(R.id.weathercastList);

        setLoader();

        recyclerView = findViewById(R.id.waterAreaList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        WaterAreaViewAdapter mAdapter = new WaterAreaViewAdapter(this, locationsList);
        recyclerView.setAdapter(mAdapter);

        water_control_sprinkle = findViewById(R.id.water_control_sprinkle);

    }

    private void setLoader() {
        Bundle bundle = new Bundle();

        CurrentWeatherForecastLoaderManager currentWeatherLoaderManager = new CurrentWeatherForecastLoaderManager();
        getSupportLoaderManager().initLoader(CURRENT_WEATHER_LOADER_ID, bundle, currentWeatherLoaderManager);

        WeatherForecastLoaderManager weatherForecastLoaderManager = new WeatherForecastLoaderManager();
        getSupportLoaderManager().initLoader(DAILY_WEATHER_LOADER_ID, bundle, weatherForecastLoaderManager);
    }

    private void setInitialData(){
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

    public class CurrentWeatherForecastLoaderManager implements LoaderManager.LoaderCallbacks<CurrentWeather> {

        @NotNull
        @Override
        public Loader<CurrentWeather> onCreateLoader(int id, Bundle args) throws NullPointerException{
            if (id == CURRENT_WEATHER_LOADER_ID)
                return new CurrentWeatherLoader(getLayoutInflater().getContext());
            throw new NullPointerException();
        }

        @Override
        public void onLoadFinished(@NonNull Loader<CurrentWeather> loader, CurrentWeather data) {
            String tmpTemp = (int)data.getTemp() + "\u00B0";
            String tmpHumidity = data.getHumidity() + "%";
            controlTemp.setText(tmpTemp);
            controlHumidity.setText(tmpHumidity);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<CurrentWeather> loader) {

        }
    }

    public class WeatherForecastLoaderManager implements LoaderManager.LoaderCallbacks<WeatherForecast> {

        @NonNull
        @Override
        public Loader<WeatherForecast> onCreateLoader(int id, @Nullable Bundle args) throws NullPointerException{
            if (id == DAILY_WEATHER_LOADER_ID)
                return new WeatherForecastLoader(getLayoutInflater().getContext());
            throw new NullPointerException();
        }

        @Override
        public void onLoadFinished(@NonNull Loader<WeatherForecast> loader, WeatherForecast data) {
            for (DailyForecast dailyForecast : data.getDaily()) {
                weatherCasts.add(new WeatherCast(dailyForecast.getDate(), R.drawable.partly_cloudy, (int)dailyForecast.getTemp().component1() + "\u00B0"));
            }

            WeatherForecastAdapter weatherForecastAdapter = new WeatherForecastAdapter(MainActivity.this, R.layout.item_weathercast, weatherCasts);
            weatherCastList.setAdapter(weatherForecastAdapter);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<WeatherForecast> loader) {

        }
    }

}