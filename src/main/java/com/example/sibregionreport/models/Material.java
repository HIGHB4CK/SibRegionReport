package com.example.sibregionreport.models;

import java.time.LocalDate;

public class Material {
    int id;
    LocalDate date;
    String material_name;
    String object_name; // Destination in DB
    Short trip_num;
    float tons;

    public Material(int id, LocalDate date, String material_name, String object_name, Short trip_num, float tons) {
        this.id = id;
        this.date = date;
        this.material_name = material_name;
        this.object_name = object_name;
        this.trip_num = trip_num;
        this.tons = tons;
    }

    public int getId() { return id;}

    public LocalDate getDate() {
        return date;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public Short getTrip_num() {
        return trip_num;
    }

    public String getObject_name() {
        return object_name;
    }

    public float getTons() {
        return tons;
    }
}
