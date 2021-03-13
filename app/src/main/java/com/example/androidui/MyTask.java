package com.example.androidui;

import android.os.AsyncTask;
import android.util.Log;

import com.example.androidui.api.RetrofitClient;
import com.example.androidui.api.model.CurrentWeatherForecast;
import com.example.androidui.api.model.DailyForecast;
import com.example.androidui.api.model.WeatherForecast;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTask extends AsyncTask<Void, Void, Void> {
    String tmpTemp, tmpHumidity;
    WeakReference<MainActivity> mainActivityWeakReference;
    MainActivity mainActivity;

    public MyTask (MainActivity context) {
        mainActivityWeakReference = new WeakReference<>(context);
        mainActivity = mainActivityWeakReference.get();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        RetrofitClient.INSTANCE.getCurrentWeather().enqueue(new Callback<CurrentWeatherForecast>() {
            @Override
            public void onResponse(@NotNull Call<CurrentWeatherForecast> call, @NotNull Response<CurrentWeatherForecast> response) {
                tmpTemp = ((int) Objects.requireNonNull(response.body()).component2().component1()) + "\u00B0";
                tmpHumidity = response.body().component2().component2() + "%";
                mainActivity.runOnUiThread(() -> {
                    mainActivity.controlTemp.setText(tmpTemp);
                    mainActivity.controlHumidity.setText(tmpHumidity);
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
                    mainActivity.weatherCasts.add(new WeatherCast(dailyForecast.getDate(), R.drawable.partly_cloudy, (int)dailyForecast.getTemp().component1() + "\u00B0"));
                }

                mainActivity.runOnUiThread(() -> {
                    WeatherForecastAdapter weatherForecastAdapter = new WeatherForecastAdapter(mainActivity, R.layout.item_weathercast, mainActivity.weatherCasts);
                    mainActivity.weatherCastList.setAdapter(weatherForecastAdapter);
                });
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable throwable) {
                Log.d("THREAD", "onFailure WeatherForecast");
            }
        });

        return null;
    }
}
