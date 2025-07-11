package com.team.dev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CategoryRequestDto {
	/** new codes  id**/
	private Long id;
	
	@NotBlank(message = "Veuillez entrer le nom de la categorie !!")
	@Size(min = 2, max = 125,  message = "La taille doit être comprise entre 2-125 ")
	private String nom;
	/** new codes  ProductNames**/
	private List<String> ProductNames;

}
