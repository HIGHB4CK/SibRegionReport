package com.example.sibregionreport.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Diesel {
    LocalDate date;
    String state_num;
    int mileage;
    int fuel_size;
}
