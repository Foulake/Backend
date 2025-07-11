package com.team.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "products")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    @NotBlank(message = "Veuillez entrer le nom du produit")
    @Size(min = 2, max = 128, message = "La taille doit être comprise entre 2 - 128 ")
    private String name;

    @NotBlank(message = "Veuillez entrer le code du produit")
    @Size(min = 2, max = 8, message = "La taille doit être comprise entre 2 - 8 ")
    private String code;

	//@NotBlank(message = "La quantité du produit est requise !")
    private BigDecimal qte;

	private String imageName;
    
    @ManyToOne
	private Magasin magasin;
    
    @ManyToOne
	private Category category;

    @Column(name = "entrepriseId")
    private Long entrepriseId;
     
    @ManyToOne
	private  User user;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<SortieProduit> sortieProduits;

    private float price;

}
