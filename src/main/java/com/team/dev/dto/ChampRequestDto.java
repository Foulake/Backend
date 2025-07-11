package com.team.dev.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(value = Include.NON_DEFAULT)
public class ChampRequestDto {
	
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom du champ!!")
	@Size(min = 2, max = 75)
	private String nomChamp;
	private String superficie;
	private String imageUrl;
	private String downloadUrl;
	private Long entrepriseId;
	private String nomLocalite;
	
	private Long localiteId;
	
	public ChampRequestDto() {}

}
