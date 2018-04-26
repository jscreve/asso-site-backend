package com.enrsolidr.energyanalysis.entity;

import lombok.Data;

@Data
public class ElectricityDataModel {
    // equipment
    // boolean
    private boolean electricHeating = false;
    private boolean electricBoiler = false;
    private boolean electricCooking = false;
    private boolean electricOven = false;
    private boolean microWaveOven = false;
    private boolean iron = false;
    private boolean vacuum = false;
    private boolean hairDryer = false;

    // numbers
    private int fridge = 0;
    private int TV = 0;
    private int dishWasher = 0;
    private int tumbleDryer = 0;
    private int washingMachine = 0;
    private int PCs = 0;

    private int knownkWhPerYear = 0;
}
