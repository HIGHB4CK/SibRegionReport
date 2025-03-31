package com.example.sibregionreport.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    LocalDate date;
    String material_name;
    String object_name; // Destination in DB
    Short trip_num;
    Short tons;
}
