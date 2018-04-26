package com.enrsolidr.energyanalysis.entity;

import com.enrsolidr.energyanalysis.model.TransactionType;
import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment")
public class Payment {
    @Id
    private String id;
    private TransactionType transactionType;
    private User user;
    private Integer amount;
    private String token;
    private Date paymentDate;
}


