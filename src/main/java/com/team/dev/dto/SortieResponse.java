package com.team.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
public class SortieResponse {
  
	private Long id;
	private double quantite;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime dateSortie;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime dateEnd;
	private Long entrepriseId;
	private String ProductNames;
	private String UserNames;
	private TypeSortie typeSortie;

	private List<SortieDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
