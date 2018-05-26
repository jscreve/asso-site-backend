package com.enrsolidr.energyanalysis.resources;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputMemberResource {
    private User user;
    private Boolean linkyActivated;
    private String linkyUsername;
    private String linkyPassword;
    private Integer threshold;
    private String username;
    private String password;

    public static Member fromResource(Member inputEntity, InputMemberResource inputResource) {
        if (inputResource.getLinkyUsername() != null) {
            inputEntity.getLinky().setUsername(inputResource.getLinkyUsername());
        }
        if (inputResource.getLinkyPassword() != null) {
            inputEntity.getLinky().setPassword(inputResource.getLinkyPassword());
        }
        if (inputResource.getLinkyActivated() != null) {
            inputEntity.getLinky().setActivated(inputResource.getLinkyActivated());
        }
        if (inputResource.getUsername() != null) {
            inputEntity.setUsername(inputResource.getUsername());
        }
        if (inputResource.getPassword() != null) {
            inputEntity.setPassword(inputResource.getPassword());
        }
        if (inputResource.getThreshold() != null) {
            inputEntity.getLinky().setThreshold(inputResource.getThreshold());
        }
        return inputEntity;
    }
}


