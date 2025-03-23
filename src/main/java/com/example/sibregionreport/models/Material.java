package com.example.sibregionreport.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Material {
    LocalDate date;
    String material_name;
    String object_name; // Destination in DB
    Short trip_num;
    Short tons;
}
