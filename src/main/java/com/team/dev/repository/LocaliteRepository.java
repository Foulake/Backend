package com.team.dev.repository;


import com.team.dev.dto.LocaliteResponseDto;
import com.team.dev.model.Activite;
import com.team.dev.model.Localite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocaliteRepository extends JpaRepository<Localite, Long>{
	@Query("Select c from Localite c Where c.nom like :key and c.entrepriseId =:entrepriseId")
	Page<Localite> findByNomContainingAAndEntrepriseId(Pageable pageable, @Param("key") String nom, Long entrepriseId);

	boolean existsLocalitesByNom(String nom);
	Page<Localite> findAll(Pageable pageable);

	
	@Query("SELECT lo FROM Localite lo WHERE lo.nom like %?1%"
			+"or lo.description like %?1%")
	public List<Localite> findByNom(String nom);

	
	@Query("SELECT new com.team.dev.dto.LocaliteResponseDto( l.id, l.nom, c.nomChamp) from Localite l Join l.champs c")
	List<LocaliteResponseDto> getLocalitesByChamp();

}
