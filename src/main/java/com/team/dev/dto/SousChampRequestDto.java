package com.team.dev.dto;

import lombok.Data;

@Data
public class SousChampRequestDto {
	
	private Long id ;
	
	private String nom;
	private String imageUrl;
	private String numero;
	private String typeCulture;
	private String nomChamp;
	private Long champId;
	private Long entrepriseId;
}
