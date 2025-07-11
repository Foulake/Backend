package com.team.dev.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class ActiviteResponseDto {
	
	private Long id;
	private Long champId;
	private String nom;
	private String champNom;
	private String description;
	private String imageUrl;
	private Long entrepriseId;
	//private List<String> ChampNames;
	//private String planificationName;
	
	 private List<ActiviteRequestDto> content;
	    private int pageNo;
	    private int pageSize;
	    private long totalElements;
	    private int totalPages;
	    private boolean last;
	    
		public ActiviteResponseDto(Long id, String nom) {
			super();
			this.id = id;
			this.nom = nom;
			//this.planificationName = planificationName;
		}
}
