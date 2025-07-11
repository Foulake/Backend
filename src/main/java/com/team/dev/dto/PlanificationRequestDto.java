package com.team.dev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanificationRequestDto {

	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le titre de la planificatiion !!")
	@Size(min = 2, max = 75)
	private String titrePlanification;
	private String description;
	
	//@NotBlank(message = "Veuillez entrer la date de debut !!")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date_start;

	//@NotBlank(message = "Veuillez entrer la date de fin !!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date_end;

	private String imageUrl;
	private String activiteNom;
	private String status;
	private Long activiteId;
	private Long entrepriseId;
}
