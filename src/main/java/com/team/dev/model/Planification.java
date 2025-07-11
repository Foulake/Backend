package com.team.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "planifications")
public class Planification extends BaseEntity{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le titre de la planificatiion !!")
	@Size(min = 2, max = 75)
	private String titrePlanification;

	@Column(columnDefinition = "varchar(10) default 'NON'")
	private String status;

	//@NotBlank(message = "Veuillez entrer la date de debut !!")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date_start;
	private String imageUrl;
	private String description;
	//@NotBlank(message = "Veuillez entrer la date de fin !!")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date_end;
	@Column(name = "entrepriseId")
	private Long entrepriseId;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Activite activite;


}
