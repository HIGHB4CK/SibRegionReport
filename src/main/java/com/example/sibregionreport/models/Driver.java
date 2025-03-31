package com.example.sibregionreport.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    LocalDate date;
    String name;
    int mileage;
    Short trip_num;
    Short hours_cnt;
    Short fuelSpend;
}
