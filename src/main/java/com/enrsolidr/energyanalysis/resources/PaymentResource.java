package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.model.TransactionType;
import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResource {
    private User user;
    private Integer amount;
    private String token;
}


