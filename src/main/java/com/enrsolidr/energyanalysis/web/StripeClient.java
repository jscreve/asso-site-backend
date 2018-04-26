package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.exceptions.PaymentException;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StripeClient {

    @Value("${stripe.key}")
    String apiKey;

    StripeClient() {
    }

    public Charge chargeCreditCard(String token, int amount) throws Exception {
        try {
            Stripe.apiKey = apiKey;
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", (amount));
            chargeParams.put("currency", "EUR");
            chargeParams.put("source", token);
            Charge charge = Charge.create(chargeParams);
            return charge;
        } catch(Exception e) {
            throw new PaymentException(e);
        }
    }
}