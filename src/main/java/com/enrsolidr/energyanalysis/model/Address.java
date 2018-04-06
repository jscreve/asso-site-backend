package com.enrsolidr.energyanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String postalCode;
    private String city;
    private String country;
}

