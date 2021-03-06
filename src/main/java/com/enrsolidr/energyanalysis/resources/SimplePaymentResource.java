package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.model.TransactionType;
import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplePaymentResource extends ResourceSupport {
    private String email;
    private Integer amount;
    private String token;
}


