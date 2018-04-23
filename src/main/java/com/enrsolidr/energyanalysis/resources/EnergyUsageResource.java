package com.enrsolidr.energyanalysis.resources;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnergyUsageResource extends ResourceSupport {
    private int electricEnergyUsage = 0;
    private int gazEnergyUsage = 0;
    private int averageEnergy = 0;
}
