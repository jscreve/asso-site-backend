package com.enrsolidr.energyanalysis.repository;

import com.enrsolidr.energyanalysis.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, Long> {
    @Query("{ 'paymentDate' : { $gte: ?0, $lte: ?1 } }")
    List<Payment> findAllPaymentsByDate(Date mindate, Date maxDate);
}
