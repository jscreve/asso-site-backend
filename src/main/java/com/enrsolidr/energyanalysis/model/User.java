package com.enrsolidr.energyanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String last_name;
    private String names;
    private String email;
    private String phone;
    private Address address;
}

