package com.enrsolidr.energyanalysis.repository;

import com.enrsolidr.energyanalysis.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, Long> {

}
