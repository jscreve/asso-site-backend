package com.enrsolidr.energyanalysis.model;

import com.enrsolidr.energyanalysis.web.EnergyController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment")
public class Payment {
    @Id
    private String id;

    private User user;
    private Integer amount;
    private String token;
    private Date paymentDate;
}

