package com.team.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "entreprises")
public class Entreprise extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entrepriseId;

    @NotBlank(message = "Veuillez entrer le nom de l'entreprise !!")
    private String nom;

    @NotBlank(message = "Veuillez entrer l'email !!")
    private String email;

    @NotBlank(message = "Veuillez entrer l'adresse de l'entreprise !!")
    private String adresse;

    @NotBlank(message = "Veuillez entrer l'adresse de l'entreprise !!")
    private String contact;

    private String description;
    private String imageUrl;

    @OneToMany(mappedBy = "entreprise")
    private List<User> users;


}
