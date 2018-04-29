package com.enrsolidr.energyanalysis.entity;

import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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

    private User user;
    //organized by year
    private Set<String> memberPayments = new HashSet<String>();
}
