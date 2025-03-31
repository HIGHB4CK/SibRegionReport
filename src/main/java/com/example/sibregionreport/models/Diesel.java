package com.example.sibregionreport.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diesel {
    LocalDate date;
    String state_num;
    String driver_name;
    int mileage;
    int fuel_size;

}
