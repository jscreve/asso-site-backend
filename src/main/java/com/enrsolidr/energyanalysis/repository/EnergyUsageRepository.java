package com.enrsolidr.energyanalysis.repository;

import com.enrsolidr.energyanalysis.model.EnergyUsage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface EnergyUsageRepository extends MongoRepository<EnergyUsage, Long> {

}
