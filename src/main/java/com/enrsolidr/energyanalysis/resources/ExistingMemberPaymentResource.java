package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.model.TransactionType;
import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExistingMemberPaymentResource {
    private String email;
    private Integer amount;
    private String token;
}


