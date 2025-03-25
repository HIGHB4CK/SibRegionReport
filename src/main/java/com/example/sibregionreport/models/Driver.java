package com.example.sibregionreport.models;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Driver {
    LocalDate date;
    String name;
    int mileage;
    Short trip_num;
    Short hours_cnt;
    Short fuelSpend;
}
