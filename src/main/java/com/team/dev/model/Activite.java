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
@Table(name = "activites")
public class Activite extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom de activite!!")
	@Size(min = 2, max = 75)
	private String nom;
	private String description;

	@Column(name = "entrepriseId")
	private Long entrepriseId;

//	@NotBlank(message = "Veuillez entrer le titre de l'activité !!")
//	@Size(min = 2, max = 75)
//	private String titreActivite;
	

	@OneToMany(mappedBy = "activite", cascade = CascadeType.ALL)
	private List<Planification> planifications;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Champ champ;
	
	public Activite() {}
	
	public Activite(@NotBlank(message = "Veuillez entrer le nom activite!!") @Size(min = 2, max = 75) String nom,
			 List<Planification> planifications, Champ champ) {
		super();
		this.nom = nom;
		this.planifications = planifications;
		this.champ = champ;
	}

	public void addPlanification(Planification planification) {
		planifications.add(planification);
		
	}
	
	public void removePlanification(Planification planification) {
		planifications.remove(planification);
		
	}
	
	
}
