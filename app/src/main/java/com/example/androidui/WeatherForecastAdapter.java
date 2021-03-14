package com.example.androidui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
public class WeatherForecastAdapter extends ArrayAdapter<WeatherCast> {

    private LayoutInflater inflater;
    private int layout;
    private List<WeatherCast> weatherCasts;

    public WeatherForecastAdapter(Context context, int resource, List<WeatherCast> weatherCasts) {
        super(context, resource, weatherCasts);
        this.weatherCasts = weatherCasts;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_weathercast);
        TextView dateView = (TextView) view.findViewById(R.id.date_weathercast);
        TextView tempView = (TextView) view.findViewById(R.id.date_temperature);

        WeatherCast state = weatherCasts.get(position);

        dateView.setText(state.getDate());
        imageView.setImageResource(state.getImageResource());
        tempView.setText(state.getTemperature());

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public void setWeatherCasts(List<WeatherCast> weatherCasts) {
        this.weatherCasts = weatherCasts;
        notifyDataSetChanged();
    }
}
