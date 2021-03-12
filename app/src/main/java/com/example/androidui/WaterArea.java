package com.example.androidui;

public class WaterArea {
    private boolean planWater;
    private String location;
    private boolean isWatering;

    public WaterArea(boolean planWater, String location, boolean isWatering){
        this.planWater=planWater;
        this.location=location;
        this.isWatering=isWatering;
    }
    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getPlanWater() {
        return this.planWater;
    }

    public void setPlanWater(boolean planWater) {
        this.planWater = planWater;
    }

    public boolean getIsWatering() {
        return this.isWatering;
    }

}
