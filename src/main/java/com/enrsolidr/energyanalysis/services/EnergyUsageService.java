package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.entity.EnergyUsage;
import com.enrsolidr.energyanalysis.repository.EnergyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnergyUsageService {

    @Autowired
    private EnergyUsageRepository energyUsageRepository;

    public List<EnergyUsage> getAllEnergyUsage(){
        List<EnergyUsage> list = new ArrayList<>();
        energyUsageRepository.findAll().forEach(e -> list.add(e));
        return list;
    }

    public EnergyUsage saveEnergyUsage(EnergyUsage energyUsage) {
        energyUsage = energyUsageRepository.save(energyUsage);
        return energyUsage;
    }
}
