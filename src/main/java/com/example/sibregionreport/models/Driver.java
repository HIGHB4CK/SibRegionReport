package com.example.sibregionreport.models;

import java.time.LocalDate;

public class Driver {
    int id;
    LocalDate date;
    String name;
    int mileage;
    Short trip_num;
    float hours_cnt;
    float fuelSpend;

    public Driver(int id, LocalDate date, String name, int mileage, Short trip_num, float hours_cnt, float fuelSpend) {
        this.id = id;
        this.trip_num = trip_num;
        this.date = date;
        this.name = name;
        this.mileage = mileage;
        this.hours_cnt = hours_cnt;
        this.fuelSpend = fuelSpend;
    }

    public int getId() { return id;}

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public int getMileage() {
        return mileage;
    }

    public Short getTrip_num() {
        return trip_num;
    }

    public float getFuelSpend() {
        return fuelSpend;
    }

    public float getHours_cnt() {
        return hours_cnt;
    }
}
