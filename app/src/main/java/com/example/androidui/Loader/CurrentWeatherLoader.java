package com.example.androidui.Loader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.androidui.api.RetrofitClient;
import com.example.androidui.api.model.CurrentWeather;


import java.util.Objects;

public class CurrentWeatherLoader extends AsyncTaskLoader<CurrentWeather> {
    public CurrentWeatherLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public CurrentWeather loadInBackground() {

        try{
            CurrentWeather currentWeather = Objects.requireNonNull(RetrofitClient.INSTANCE.getCurrentWeather().execute().body()).component2();
            return new CurrentWeather(currentWeather.getTemp(),currentWeather.getHumidity());
        }
        catch (Exception e) {
            Log.d("Retrofit Exception", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
