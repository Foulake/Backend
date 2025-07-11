package com.team.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails{

	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Le nom est réquis")
	@Column(name = "nom")
	private String nom;

	@NotBlank(message = "Le prénom est réquis")
	@Column(name = "prenom")
	private String prenom;

	//@NotBlank(message = "L'image est réquise")
	@Column(name = "imageUrl")
	private String imageUrl;

	@NotBlank(message = "L'E-mail est réquis")
	@Column(name = "email", nullable = false, unique = true)
	@Email(message = "Entrer un email valide !")
	private String email;

	@NotBlank(message = "Le mot de passe est réquis")
	@Column(name = "password", nullable = true)
	@Size(min = 4, message = "Entre au moins 4 caractères")
	private String password;
	private  boolean isEnabled;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products ;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SortieProduit> sortieProduits;

	@ManyToOne
	@JoinColumn(name = "entrepriseId")
	private Entreprise entreprise;

	private boolean isFirstLogin= true;

	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Role> roles = new ArrayList<>();

	public User(String email, String password, boolean isEnabled, Set<Role> roles) {
		this.email = email;
		this.password = password;
		this.isEnabled = isEnabled;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role role : roles) {

			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		return authorities;
	}


	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {

		return email;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return isEnabled;
	}


    public void addProduct(Product product) {
		products.add(product);
    }

	public void removeProduct(Product product) {
		products.remove(product);
	}

	public void removeRole(Role rol) {
		this.roles.remove(rol);
	}

	public void addRole(Role roleUser) {
		this.roles.add(roleUser);
	}
}
