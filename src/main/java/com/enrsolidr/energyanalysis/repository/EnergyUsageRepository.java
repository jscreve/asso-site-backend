package com.enrsolidr.energyanalysis.repository;

import com.enrsolidr.energyanalysis.entity.EnergyUsage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnergyUsageRepository extends MongoRepository<EnergyUsage, Long> {

}
