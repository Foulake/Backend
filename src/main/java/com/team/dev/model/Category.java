package com.team.dev.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="categories")
public class Category extends  BaseEntity{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotBlank(message = "Veuillez entrer le nom de la categorie !!")
	private String nom;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products ;

	@Column(name = "entrepriseId")
	private Long entrepriseId;
	
	public Category() {}
	public Category(String nom, List<Product> products) {
		super();
		this.nom = nom;
		this.products = products;
	}
	
	public void addProduct(Product product) {
		products.add(product);
	}
	public void removeProduct(Product product) {
		products.remove(product);
	}
	
	

}
