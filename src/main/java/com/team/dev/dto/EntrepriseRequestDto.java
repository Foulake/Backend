package com.team.dev.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class EntrepriseRequestDto {

    private Long idEntreprise;

    @NotBlank(message = "Veuillez entrer le nom de l'entreprise !!")
    private String nom;

    @NotBlank(message = "Veuillez entrer le nom de l'email !!")
    private String email;

    @NotBlank(message = "Veuillez entrer l'adresse de l'entreprise !!")
    private String adresse;

    @NotBlank(message = "Veuillez entrer l'adresse de l'entreprise !!")
    private String contact;

    private String description;

    private String imageUrl;

    private String userNames;
}
