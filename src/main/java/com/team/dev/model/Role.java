package com.team.dev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "roleName", nullable = false)
	private String roleName;

	public Role(String roleName){
		this.roleName = roleName;
	}

}
