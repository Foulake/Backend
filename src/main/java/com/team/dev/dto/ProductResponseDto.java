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
public class ProductResponseDto {

	private Long id;
	private String name;
	private String code;
	private float price;
	private double qte;
	private String imageName;
	private String categoryNom;
	private Long categoryId;
	private String magasinNom;
	private Long magasinId;
	private String email;
	private Long userId;
	private Long entrepriseId;

	 private List<ProductRequestDto> content;
	    private int pageNo;
	    private int pageSize;
	    private long totalElements;
	    private int totalPages;
	    private boolean last;


}
