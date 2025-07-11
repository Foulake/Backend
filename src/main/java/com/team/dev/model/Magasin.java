package com.team.dev.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@ToString
@Table(name="magasins")
public class Magasin extends  BaseEntity{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom du magasin !!")
	@Size(min = 2, max = 125,  message = "La taille doit être comprise entre 2-125 ")
	private String nomMagasin;

	@Column(name = "entrepriseId")
	private Long entrepriseId;

//	@ManyToOne
//	private Localite localite;
	
	@OneToMany(mappedBy = "magasin", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products;

	public Magasin() {}
	
	public Magasin(String nomMagasin) {
		this.nomMagasin = nomMagasin;
	}
	public Magasin(String nomMagasin, List<Product> products) {
		super();
		this.nomMagasin = nomMagasin;
		this.products = products;
	}
	
	public void addProduct(Product product) {
		products.add(product);
	}
	public void removeProduct(Product product) {
		products.remove(product);
	}

}
