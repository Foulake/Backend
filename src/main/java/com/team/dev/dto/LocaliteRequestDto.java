package com.team.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.team.dev.model.Champ;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
public class LocaliteRequestDto {
	
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom de la localité !!")
	@Size(min = 2, max = 75)
	private String nom;
	

	@NotBlank(message = "Veuillez entrer la description de la localitée !!")
	@Size(min = 2, max = 75)
	private String description;

	private List<Champ> champs;
	
	
	/** new codes  ProductNames**/
	private List<String> ChampNames;
	

}
