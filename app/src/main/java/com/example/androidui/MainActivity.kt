
package com.example.androidui

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    var recyclerView : RecyclerView? = null
    var mAdapter : WaterAreaViewAdapter? = null
    var layoutManager : RecyclerView.LayoutManager? = null
    var water_control_sprinkle : CheckBox? = null
    var controlTemp : TextView? = null
    var controlHumidity : TextView? = null
    var weatherCasts : MutableList<WeatherCast>? = null
    var weatherCastList : ListView? = null
    var locationsList : MutableList<WaterArea>? = null
    var stateAdapter : WeatherForecastAdapter? = null
    var weatherForecastViewModel : WeatherForecastViewModel? = null

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_main)

        setInitialData()
        controlTemp = findViewById(R.id.controlTemp)
        controlHumidity = findViewById(R.id.controlHumidity)

        weatherCastList = findViewById(R.id.weathercastList)

        stateAdapter = WeatherForecastAdapter(this, R.layout.item_weathercast, weatherCasts)
        weatherCastList!!.adapter = stateAdapter

        recyclerView = findViewById(R.id.waterAreaList)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager

        mAdapter = WaterAreaViewAdapter(this, locationsList as ArrayList<WaterArea>?)
        recyclerView!!.adapter = mAdapter

        water_control_sprinkle = findViewById(R.id.water_control_sprinkle)

    }

    private fun setInitialData() {
        weatherCasts = ArrayList()
        locationsList = ArrayList()
        weatherCasts!!.add(WeatherCast("February 17, 2021", R.drawable.cloudy, "19 \u00B0"))
        weatherCasts!!.add(WeatherCast("February 18, 2021", R.drawable.rain, "21 \u00B0"))
        weatherCasts!!.add(WeatherCast("February 19, 2021", R.drawable.partly_cloudy, "23 \u00B0"))
        weatherCasts!!.add(WeatherCast("February 20, 2021", R.drawable.cloudy, "19 \u00B0"))
        weatherCasts!!.add(WeatherCast("February 21, 2021", R.drawable.rain, "21 \u00B0"))
        weatherCasts!!.add(WeatherCast("February 22, 2021", R.drawable.partly_cloudy, "23 \u00B0"))
        locationsList!!.add(WaterArea(false, "BACKYARD", false))
        locationsList!!.add(WaterArea(false, "BACK PATIO", true))
        locationsList!!.add(WaterArea(false, "FRONT YARD", false))
        locationsList!!.add(WaterArea(true, "GARDEN", false))
        locationsList!!.add(WaterArea(false, "PORCH", true))
        setWeatherForecast()
    }

    fun onSprinkleClick(view: View?) {
        if (!water_control_sprinkle!!.isChecked) {
            for (waterArea in locationsList!!) {
                waterArea.planWater = false
            }
            val areaViewAdapter = WaterAreaViewAdapter(this, locationsList as ArrayList<WaterArea>?)
            recyclerView!!.adapter = areaViewAdapter
        }
    }

    private fun setWeatherForecast() {
        weatherForecastViewModel = ViewModelProviders.of(this).get(WeatherForecastViewModel::class.java)
        weatherForecastViewModel!!.weatherCastMutableLiveData
                .observe(this) { castList ->
                    if (castList != null) {
                        weatherCasts = castList as MutableList<WeatherCast>?
                        stateAdapter!!.setWeatherCasts(weatherCasts)
                    }
                }

        weatherForecastViewModel!!.currentWeatherMutableLiveData
                .observe(this) { currentWeather ->
                    if (currentWeather != null) {
                        val tmpTemp: String = "${currentWeather.component1().toInt()}\u00B0"
                        val tmpHumidity: String = currentWeather.component2().toString() + "%"
                        controlTemp!!.text = tmpTemp
                        controlHumidity!!.text = tmpHumidity
                    }
                }
        weatherForecastViewModel!!.getWeather()
    }

}