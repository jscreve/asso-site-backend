package com.enrsolidr.energyanalysis.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginMemberResource extends ResourceSupport {
    private String username;
    private String password;
}


