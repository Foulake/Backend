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

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
public class MagasinRequestDto {
	
	/** new codes  id**/
	private Long id;
	@NotBlank(message = "Veuillez entrer le nom du magasin !!")
	@Size(min = 2, max = 125,  message = "La taille doit être comprise entre 2-125 ")
	private String nomMagasin;
	private String nomChamp;
	private String nomLocalite;
	/** new codes  ProductNames**/
	private List<String> ProductNames;
	
	

}
