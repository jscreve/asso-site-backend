package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.EnergyUsage;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResource;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResourceAssembler;
import com.enrsolidr.energyanalysis.services.EnergyUsageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    private static final Logger logger = LoggerFactory.getLogger(EnergyController.class);

    @Autowired
    EnergyUsageService energyUsageService;

    @RequestMapping(value = "/usage", method = RequestMethod.POST)
    public ResponseEntity<?> computeEnergyUsage(@RequestBody EnergyUsage energyUsage) {
        logger.info("Creating energy model : {}", energyUsage);

        energyUsage = energyUsageService.saveEnergyUsage(energyUsage);

        EnergyUsageResourceAssembler resourceAssembler = new EnergyUsageResourceAssembler(EnergyController.class, EnergyUsageResource.class);
        EnergyUsageResource resource = resourceAssembler.toResource(energyUsage);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}