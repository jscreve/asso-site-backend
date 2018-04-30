package com.enrsolidr.energyanalysis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class ApplicationUser {
    @Id
    private String id;
    @Column
    private String username;
    @Column
    //@JsonIgnore
    private String password;

    private List<String> authorities = Collections.singletonList("ROLE_ADMIN");
}
