package com.enrsolidr.energyanalysis.entity;

import lombok.Data;

@Data
public class HomeDataModel {
    private int appartmentSize;
    private int inhabitants;
    private String electricityAndGas = new String();
}
