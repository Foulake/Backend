package com.team.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
public class ChampResponseDto {
	
	private Long id;
	
	@NotBlank(message = "Veuillez entrer le nom de la vane!!")
	@Size(min = 2, max = 75)
	private String nomChamp;
	private String superficie;
	private String imageUrl;
	private String downloadUrl;
	private String nomLocalite;
	
	/** new **/

	private String localiteNom;
	private Long entrepriseId;
	private Long localiteId;

	//private String nomChamp;
	
	private String nom;
	
	private List<String> activiteNames;
	
	public ChampResponseDto(Long id, String nomChamp, String nom) {
		super();
		this.id = id;
		this.nomChamp = nomChamp;
		this.nom = nom;
	}
	 

    private List<ChampRequestDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

}

