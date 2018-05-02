package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResource {
    private User user;
    private String username;
    private String password;
}


