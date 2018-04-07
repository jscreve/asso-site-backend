package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.model.EnergyUsage;
import com.enrsolidr.energyanalysis.model.Payment;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResource;
import com.enrsolidr.energyanalysis.resources.EnergyUsageResourceAssembler;
import com.enrsolidr.energyanalysis.services.EnergyUsageService;
import com.enrsolidr.energyanalysis.services.PaymentService;
import com.enrsolidr.energyanalysis.services.ReceiptService;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private StripeClient stripeClient;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ReceiptService receiptService;

    @Autowired
    PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public Charge chargeCard(@RequestBody Payment payment) throws Exception {
        payment.setPaymentDate(new Date());
        paymentService.savePayment(payment);
        return this.stripeClient.chargeCreditCard(payment.getToken(), payment.getAmount());
    }

    @RequestMapping(value = "/receipt", method = RequestMethod.GET)
    public void generateReceipt() throws Exception {
        receiptService.sendReceipts();
    }
}