package com.example.sibregionreport.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Columns {
    public Columns(int id, LocalDate date, String state_num, String driver, String customer, String from, String destination, LocalTime shift_time_start, LocalTime shift_time_ending, float fuel, int mileage, float hours_num, Short trip_num, String material_name, float tons) {
        this.id = id;
        this.date = date;
        this.state_num = state_num;
        this.driver = driver;
        this.customer = customer;
        this.from = from;
        this.destination = destination;
        this.shift_time_start = shift_time_start;
        this.shift_time_ending = shift_time_ending;
        this.fuel = fuel;
        this.mileage = mileage;
        this.hours_num = hours_num;
        this.trip_num = trip_num;
        this.material_name = material_name;
        this.tons = tons;
    }

    int id;
    LocalDate date;
    String state_num;
    String driver;
    String customer;
    String from;
    String destination;
    LocalTime shift_time_start;
    LocalTime shift_time_ending ;
    float fuel;
    int mileage;
    float hours_num;
    Short trip_num;
    String material_name;
    float tons;

    public int getId() { return id;}

    public LocalDate getDate() {
        return date;
    }

    public String getState_num() {
        return state_num;
    }

    public String getDriver() {
        return driver;
    }

    public String getCustomer() {
        return customer;
    }

    public String getFrom() {
        return from;
    }

    public String getDestination() {
        return destination;
    }

    public LocalTime getShift_time_start() {
        return shift_time_start;
    }

    public float getFuel() {
        return fuel;
    }

    public LocalTime getShift_time_ending() {
        return shift_time_ending;
    }

    public int getMileage() {
        return mileage;
    }

    public float getHours_num() {
        return hours_num;
    }

    public Short getTrip_num() {
        return trip_num;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public float getTons() {
        return tons;
    }
}
