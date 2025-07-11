package com.team.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "sousChamps")
public class SousChamp extends BaseEntity{

	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom de souschamp !!")
	@Size(min = 2, max = 75)
	private String nom;
	private String typeCulture;

	@Size(min = 2, max = 25)
	private String numero;
	private String imageUrl;

	@Column(name = "entrepriseId")
	private Long entrepriseId;
	
//	@ManyToOne(fetch = FetchType.EAGER, optional = false)
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	@JsonIgnore
//	private Champ champ;
//	@OneToMany(mappedBy = "sousChamp", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Recolte> recoltes;
	public SousChamp() {}
	
	public SousChamp( String nom, String numero) {
		super();
		this.nom = nom;
		this.numero = numero;
	}

}
