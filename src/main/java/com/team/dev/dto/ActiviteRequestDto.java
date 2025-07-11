package com.team.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)

public class ActiviteRequestDto {
	
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom de l'activite!!")
	@Size(min = 2, max = 75)
	private String nom;
	private String champNom;
	private String description;
	private String imageUrl;
	private Long champId;


}
