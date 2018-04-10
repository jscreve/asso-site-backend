package com.enrsolidr.energyanalysis.repository;

import com.enrsolidr.energyanalysis.model.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, Long> {
    List<ApplicationUser> findByUsername(String name);
}
