package com.example.sibregionreport.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Objects {
    LocalDate date;
    String object_name; // Destination in DB
    String material_name;
    Short tons;
}
