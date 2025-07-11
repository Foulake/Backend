package com.team.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@ToString
@Table(name = "localites")
public class Localite extends BaseEntity{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom de la localité !!")
	private String nom;
	
	@NotBlank(message = "Veuillez entrer la description de la localitée !!")
	private String description;

	@Column(name = "entrepriseId")
	private Long entrepriseId;
	
	@OneToMany(mappedBy = "localite", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Champ> champs;
	
//	@OneToMany(mappedBy = "localite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private List<Magasin> magasins;

	public Localite() {}
	
	public Localite(String nom, String description, List<Champ> champs) {
		super();
		this.nom = nom;
		this.description = description;
		this.champs = champs;
	}
	
	
	public void addChamp(Champ champ) {
		champs.add(champ);
	}
	public void removeChamp(Champ champ) {
		champs.remove(champ);
	}
	
}
