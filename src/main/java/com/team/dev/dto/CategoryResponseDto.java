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
public class CategoryResponseDto {
	
	private Long id;
	private String nom;
	private Long entrepriseId;
	private String nomProduict;
	private List<String> ProductNames;
	
	private List<CategoryRequestDto> content;
	    private int pageNo;
	    private int pageSize;
	    private long totalElements;
	    private int totalPages;
	    private boolean last;
	    
	public CategoryResponseDto(Long id , String nom, String nomProduict){
		this.id = id;
		this.nom = nom;
		this.nomProduict = nomProduict;
	}

}
