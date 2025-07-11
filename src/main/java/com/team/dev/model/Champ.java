package com.team.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "champs")
public class Champ extends BaseEntity{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom de la vane!!")
	private String nomChamp;
	private String superficie;

	private String imageUrl;
	private String downloadUrl;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Localite localite;

	@Column(name = "entrepriseId")
	private Long entrepriseId;

	@OneToMany(mappedBy = "champ", cascade = CascadeType.ALL)
	private List<Activite> activites;
	
//	@OneToMany(mappedBy = "champ", cascade = CascadeType.ALL)
//	private List<SousChamp> sousChamps;

	@OneToMany(mappedBy = "Champ", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Recolte> recoltes;

	public Champ() {}

	public void addActivite(Activite activite) {
		activites.add(activite);
	}
	
	public void removeActivite(Activite activite) {
		activites.remove(activite);
	}



}
