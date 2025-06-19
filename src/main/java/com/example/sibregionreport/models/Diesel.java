package com.example.sibregionreport.models;

import java.time.LocalDate;

public class Diesel {
    int id;
    LocalDate date;
    String state_num;
    String driver_name;
    int mileage;
    float fuel_size;

    public Diesel(int id, LocalDate date, String state_num, String driver_name, int mileage, float fuel_size) {
        this.id = id;
        this.date = date;
        this.state_num = state_num;
        this.driver_name = driver_name;
        this.mileage = mileage;
        this.fuel_size = fuel_size;
    }

    public int getId() { return id;}

    public LocalDate getDate() {
        return date;
    }

    public String getState_num() {
        return state_num;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public int getMileage() {
        return mileage;
    }

    public float getFuel_size() {
        return fuel_size;
    }



}
