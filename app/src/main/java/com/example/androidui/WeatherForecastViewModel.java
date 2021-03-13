package com.example.androidui;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidui.api.RetrofitClient;
import com.example.androidui.api.model.CurrentWeather;
import com.example.androidui.api.model.CurrentWeatherForecast;
import com.example.androidui.api.model.DailyForecast;
import com.example.androidui.api.model.WeatherForecast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherForecastViewModel extends ViewModel {
   private MutableLiveData <CurrentWeather> currentWeatherMutableLiveData;
   private MutableLiveData <List<WeatherCast>> weatherCastMutableLiveData;
   private LiveData <List<WeatherCast>> weatherCastLiveData;

    public WeatherForecastViewModel() {
        currentWeatherMutableLiveData = new MutableLiveData<>();
        weatherCastMutableLiveData = new MutableLiveData<>();
        weatherCastLiveData = weatherCastMutableLiveData;
    }

    public MutableLiveData<CurrentWeather> getCurrentWeatherMutableLiveData() {
        return currentWeatherMutableLiveData;
    }

    public LiveData <List<WeatherCast>> getWeatherCastLiveData() {
        return weatherCastLiveData;
    }

    public  MutableLiveData <List<WeatherCast>> getWeatherCastMutableLiveData() {
        return weatherCastMutableLiveData;
    }

    public void getWeatherForecast() {
        RetrofitClient.INSTANCE.getWeatherForecast().subscribeOn(Schedulers.io()).subscribe(new DisposableSingleObserver<WeatherForecast>() {
            @Override
            public void onSuccess(@NonNull WeatherForecast weatherForecast) {
                ArrayList<WeatherCast> weatherCasts = new ArrayList<>();

                for (DailyForecast dailyForecast : weatherForecast.component1()) {
                    weatherCasts.add(new WeatherCast(dailyForecast.getDate(), R.drawable.partly_cloudy, (int)dailyForecast.getTemp().component1() + "\u00B0"));
                }
                weatherCastMutableLiveData.postValue(weatherCasts);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });

        RetrofitClient.INSTANCE.getCurrentWeather().subscribeOn(Schedulers.io()).subscribe(new DisposableSingleObserver<CurrentWeatherForecast>() {
            @Override
            public void onSuccess(@NonNull CurrentWeatherForecast currentWeatherForecast) {
                currentWeatherMutableLiveData.postValue(currentWeatherForecast.getWeather());
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });

    }
}
