package com.team.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team.dev.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class AuthenticateResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private Long id;

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
    //private String rawPassword;
    private boolean isEnabled;
    private boolean isFirstLogin= true;
    private String entreprise;
    private Long entrepriseId;
    private Collection<Role> roles = new ArrayList<>();

}
