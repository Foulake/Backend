package com.team.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
@Builder
public class SortieDto {
	
	private Long id;

	private BigDecimal quantite;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime dateSortie;
	@Enumerated(EnumType.STRING)
	private TypeSortie typeSortie;
	private String ProductNames;
	private String UserNames;
	//private List<Product> products;
	
	private Long productId;
	private Long userId;
	private Long entrepriseId;
	
}
