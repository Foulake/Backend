package com.team.dev.dto;

import com.team.dev.model.Entreprise;
import com.team.dev.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Le prénom est requis !")
    private String prenom;
    @NotBlank(message = "Le nom est requis !")
    private String nom;
    @NotBlank(message = "L'e-mail est requis !")
    @Email(message = "Entre un e-mail valide !")
    private String email;
    private String imageUrl;
    @NotBlank(message = "Le mot de passe est requis !")
    private String password;
    private Entreprise entreprise;
    private Long entrepriseId;
    //private String rawPassword;
    private  boolean isEnabled;
    private boolean isFirstLogin= true;
    private String roleName;
    private Collection<Role> roles = new ArrayList<>();

    public void addRole(Role roleUser) {
        this.roles.add(roleUser);
    }
}
