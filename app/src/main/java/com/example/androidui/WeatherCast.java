package com.example.androidui;

public class WeatherCast {
    private String date;
    private int imageResource;
    private String temperature;

    public WeatherCast(String date, int imageResource, String temperature){
        this.date=date;
        this.imageResource=imageResource;
        this.temperature=temperature;
    }

        public String getDate() {
            return this.date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTemperature() {
            return this.temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public int getImageResource() {
            return this.imageResource;
        }

        public void setImageResource(int imageResource) {
            this.imageResource = imageResource;
        }
}
