package com.example.sibregionreport.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Objects {
    LocalDate date;
    String material;
    Short tons;
}
