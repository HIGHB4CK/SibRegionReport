package com.example.sibregionreport.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Diesel {
    LocalDate date;
    int fuel_size;
    String driver;
}
