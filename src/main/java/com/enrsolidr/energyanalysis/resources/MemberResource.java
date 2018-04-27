package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResource extends ResourceSupport {
    private User user;
    private Set<String> memberPayments;
}


