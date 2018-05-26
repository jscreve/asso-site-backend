package com.enrsolidr.energyanalysis.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputLinkyResource {
    private String username;
    private String linkyUsername;
    private String linkyPassword;
}
