package com.team.dev.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.dev.dto.TypeSortie;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "sortieProducts")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SortieProduit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ; 
	
	@Column(name = "quantite")
	//@NotBlank(message = "La quantité est réquise !")
	private BigDecimal quantite;
	
	@Column(name = "dateSortie")
	//@DateTimeFormat(iso = ISO.DATE_TIME)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dateSortie;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private TypeSortie typeSortie;

	@Column(name = "entrepriseId")
	private Long entrepriseId;

	@ManyToOne
	private Product product;
	
	@ManyToOne
	private User user;
	


}
