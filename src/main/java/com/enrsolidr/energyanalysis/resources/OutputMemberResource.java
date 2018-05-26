package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.model.Linky;
import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputMemberResource extends ResourceSupport {
    private User user;
    private Linky linky;
    private List<String> authorities;
    private String username;
    private String password;
}


