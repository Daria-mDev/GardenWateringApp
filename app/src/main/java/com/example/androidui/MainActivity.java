package com.example.androidui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidui.api.RetrofitClient;
import com.example.androidui.api.model.CurrentWeatherForecast;
import com.example.androidui.api.model.DailyForecast;
import com.example.androidui.api.model.WeatherForecast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckBox water_control_sprinkle;
    TextView controlTemp, controlHumidity;
    ArrayList<WeatherCast> weatherCasts = new ArrayList<>();
    ListView weatherCastList;
    ArrayList<WaterArea> locationsList = new ArrayList<>();
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlTemp = findViewById(R.id.controlTemp);
        controlHumidity = findViewById(R.id.controlHumidity);

        weatherCastList = findViewById(R.id.weathercastList);

        setInitialData();

        recyclerView = findViewById(R.id.waterAreaList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        WaterAreaViewAdapter mAdapter = new WaterAreaViewAdapter(this, locationsList);
        recyclerView.setAdapter(mAdapter);

        water_control_sprinkle = findViewById(R.id.water_control_sprinkle);

    }

    private void setInitialData(){
        setWeatherForecast();
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
        threadPoolExecutor = new ThreadPoolExecutor(
                4, 8, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(8));

        threadPoolExecutor.submit(() -> {
            RetrofitClient.INSTANCE.getCurrentWeather().enqueue(new Callback<CurrentWeatherForecast>() {
                @Override
                public void onResponse(@NotNull Call<CurrentWeatherForecast> call, @NotNull Response<CurrentWeatherForecast> response) {
                    String tmpTemp = ((int) Objects.requireNonNull(response.body()).component2().component1()) + "\u00B0";
                    String tmpHumidity = response.body().component2().component2() + "%";
                    MainActivity.this.runOnUiThread(() -> {
                        controlTemp.setText(tmpTemp);
                        controlHumidity.setText(tmpHumidity);
                    });
                }

                @Override
                public void onFailure(Call<CurrentWeatherForecast> call, Throwable throwable) {
                    Log.d("THREAD", "onFailure CurrentWeatherForecast");
                }
            });

            RetrofitClient.INSTANCE.getWeatherForecast().enqueue(new Callback<WeatherForecast>() {
                @Override
                public void onResponse(@NotNull Call<WeatherForecast> call, @NotNull Response<WeatherForecast> response) {
                    List<DailyForecast> dailyForecastList = Objects.requireNonNull(response.body()).component1();

                    for (DailyForecast dailyForecast : dailyForecastList) {
                        weatherCasts.add(new WeatherCast(dailyForecast.getDate(), R.drawable.partly_cloudy, (int)dailyForecast.getTemp().component1() + "\u00B0"));
                    }

                    MainActivity.this.runOnUiThread(() -> {
                        WeatherForecastAdapter weatherForecastAdapter = new WeatherForecastAdapter(MainActivity.this, R.layout.item_weathercast, weatherCasts);
                        weatherCastList.setAdapter(weatherForecastAdapter);
                    });
                }

                @Override
                public void onFailure(Call<WeatherForecast> call, Throwable throwable) {
                    Log.d("THREAD", "onFailure WeatherForecast");
                }
            });

        });
    }


            @Override
            protected void onDestroy() {
                super.onDestroy();
                threadPoolExecutor.shutdown();
    }

}