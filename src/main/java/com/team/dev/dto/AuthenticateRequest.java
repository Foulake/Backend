package com.team.dev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {
    @NotBlank(message = "L'e-mail est requis !")
    @Email(message = "Entrer un e-mail valide !")
    private String email;
    @NotBlank(message = "Le mot de passe est requis !")
    private String password;


}
