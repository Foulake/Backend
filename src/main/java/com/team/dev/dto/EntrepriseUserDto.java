package com.team.dev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EntrepriseUserDto {
    @NotNull(message = "Les données de l'entreprise sont requises")
    EntrepriseRequestDto entrepriseDto;
    @NotNull(message = "Les données de l'utilisateur sont requises")
    RegisterRequest request;
}
