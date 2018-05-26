package com.enrsolidr.energyanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Linky {
    private boolean activated = false;
    private Integer threshold = 0;
    private String username = null;
    private String password = null;
    private Double energyYesterday = 0.0d;
    private Double lowestPowerYesterday = 0.0d;
}

