package com.example.sibregionreport.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Objects {
    LocalDate date;
    String object_name; // Destination in DB
    String material_name;
    Short tons;
}
