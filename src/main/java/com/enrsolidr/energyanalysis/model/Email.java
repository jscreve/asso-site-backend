package com.enrsolidr.energyanalysis.model;

import com.enrsolidr.energyanalysis.web.EnergyController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(EnergyController.class);

    private String name;
    private String email;
    private String subject;
    private String message;
}

