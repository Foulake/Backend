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
//@JsonInclude(value = Include.NON_DEFAULT)
public class ProductRequestDto {
	
	private Long id;
	
	@NotBlank(message = "Veuillez entrer le nom du produit")
    @Size(min = 2, max = 128, message = "La taille doit être comprise entre 2 - 128 ")
	private String name;

	@NotBlank(message = "Veuillez entrer le code du produit")
	@Size(min = 2, max = 8, message = "La taille doit être comprise entre 2 - 8 ")
	private String code;

	private double qte;
	
	private Long magasinId;
    
	private Long categoryId;
	private Long entrepriseId;
	
	private Long userId;
	private String imageName;

    private float price;
    
    private String categoryNom;
	private String magasinNom;
	private String email;

}
