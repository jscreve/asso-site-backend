package com.enrsolidr.energyanalysis.entity;

import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "member")
public class Member {
    @Id
    private String id;

    @Column
    private String username;

    @Column
    //@JsonIgnore
    private String password;

    private List<String> authorities;

    private User user;
    //organized by year
    private Set<String> memberPayments = new HashSet<String>();
}

