package com.example.androidui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidui.api.RetrofitClient.getCurrentWeather
import com.example.androidui.api.RetrofitClient.getWeatherForecast
import com.example.androidui.api.model.CurrentWeather
import kotlinx.coroutines.launch


class WeatherForecastViewModel : ViewModel() {
    val currentWeatherMutableLiveData: MutableLiveData<CurrentWeather> = MutableLiveData()
    val weatherCastMutableLiveData: MutableLiveData<List<WeatherCast>> = MutableLiveData()
    private val weatherCastLiveData: LiveData<List<WeatherCast>>

    fun  getWeather() {
        viewModelScope.launch {
            val  response = getWeatherForecast()
            if (response.isSuccessful) {
                val weatherCasts: ArrayList<WeatherCast> = ArrayList()
                for (dailyForecast in response.body()!!.daily) {
                    weatherCasts.add(WeatherCast(dailyForecast.getDate(), R.drawable.partly_cloudy, "${dailyForecast.temp.day.toInt()}\u00B0"))
                }
                weatherCastMutableLiveData.postValue(weatherCasts)
            }
        }
        viewModelScope.launch {
            val  response = getCurrentWeather()
            if  (response.isSuccessful) {
                currentWeatherMutableLiveData.postValue(response.body()?.weather)
            }
        }
    }

    init {
        weatherCastLiveData = weatherCastMutableLiveData
    }
}