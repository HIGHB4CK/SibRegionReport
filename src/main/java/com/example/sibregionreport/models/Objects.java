package com.example.sibregionreport.models;

import java.time.LocalDate;

public class Objects {
    int id;
    LocalDate date;
    String object_name; // Destination in DB
    String material_name;
    float tons;

    public Objects(int id, LocalDate date, String object_name, String material_name, float tons) {
        this.id = id;
        this.tons = tons;
        this.material_name = material_name;
        this.object_name = object_name;
        this.date = date;
    }

    public int getId() { return id;}

    public LocalDate getDate() {
        return date;
    }

    public String getObject_name() {
        return object_name;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public float getTons() {
        return tons;
    }
}
