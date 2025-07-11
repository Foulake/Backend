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
public class MagasinResponseDto {
	
	private Long id;
	private String nomMagasin;
	private String nomChamp;
	private String nomProduit;
	private String description;
	private Long entrepriseId;
	private List<String> ProductNames;
	
	private List<MagasinRequestDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    
	
	public MagasinResponseDto(Long id, String nomChamp, String nomProduit) {
		this.id = id;
		this.nomProduit = nomProduit;
		this.nomChamp = nomChamp;
	}
}
