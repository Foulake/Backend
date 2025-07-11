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
public class LocaliteResponseDto {
	
	private Long id;
	private String nom;
	private String description;
	private String champName;
	private Long entrepriseId;
	private List<String> ChampNames;
	
	private List<LocaliteRequestDto> content;
	private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    
	public LocaliteResponseDto( String nom, String champName) {
		super();
		//this.id = id;
		this.nom = nom;
		this.champName = champName;
	}
	
	public LocaliteResponseDto(Long id , String nom, String champName) {
	
		//this.id = id;
		this.nom = nom;
		this.champName = champName;
	}
	
	public LocaliteResponseDto(Long id , String nom){
		this.id = id;
		this.nom = nom;
	}

}
