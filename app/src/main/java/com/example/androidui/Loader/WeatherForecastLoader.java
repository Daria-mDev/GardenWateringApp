package com.example.androidui.Loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.androidui.api.RetrofitClient;
import com.example.androidui.api.model.WeatherForecast;

import java.io.IOException;

public class WeatherForecastLoader extends AsyncTaskLoader<WeatherForecast> {
    WeatherForecast weatherForecast;

    public WeatherForecastLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public WeatherForecast loadInBackground() {
        try{
            weatherForecast = RetrofitClient.INSTANCE.getWeatherForecast().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherForecast;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (weatherForecast != null)
            deliverResult(weatherForecast);

        if (weatherForecast == null || takeContentChanged())
            forceLoad();
    }
}
