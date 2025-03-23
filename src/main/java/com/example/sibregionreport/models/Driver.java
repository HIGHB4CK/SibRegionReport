package com.example.sibregionreport.models;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Driver {
    LocalDate date;
    String state_num;
    int km;
    String from;
    String to;
    LocalTime how_long_work;
}
