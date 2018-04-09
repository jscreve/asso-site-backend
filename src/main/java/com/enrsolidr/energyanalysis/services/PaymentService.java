package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.model.EnergyUsage;
import com.enrsolidr.energyanalysis.model.Payment;
import com.enrsolidr.energyanalysis.repository.EnergyUsageRepository;
import com.enrsolidr.energyanalysis.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPayments(){
        List<Payment> list = new ArrayList<>();
        paymentRepository.findAll().forEach(e -> list.add(e));
        return list;
    }

    public List<Payment> getPaymentsByDate(Date fromDate, Date toDate){
        List<Payment> list = new ArrayList<>();
        paymentRepository.findAllPaymentsByDate(fromDate, toDate).forEach(e -> list.add(e));
        return list;
    }

    public Payment savePayment(Payment payment) {
        payment = paymentRepository.save(payment);
        return payment;
    }
}
