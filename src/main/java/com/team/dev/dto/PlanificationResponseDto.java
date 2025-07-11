package com.team.dev.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
public class PlanificationResponseDto {
	private Long id;
	private String titrePlanification;
	private Date date_start;
	private Long entrepriseId;
	private Date date_end;
	private String activiteNom;
	private String status;
	private String imageUrl;
	private String description;

	
	 private List<PlanificationRequestDto> content;
	    private int pageNo;
	    private int pageSize;
	    private long totalElements;
	    private int totalPages;
	    private boolean last;

}
