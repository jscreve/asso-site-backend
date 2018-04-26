package com.enrsolidr.energyanalysis.entity;

import lombok.Data;

@Data
public class GazDataModel {
    private boolean gazHeating = false;
    private boolean gazWaterHeating = false;
    private boolean gazCooking = false;
    private int knownkWhPerYear = 0;
}
