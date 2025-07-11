package com.team.dev.repository;

import com.team.dev.dto.ActiviteResponseDto;
import com.team.dev.model.Activite;
import com.team.dev.model.Champ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ActiviteRepository extends CrudRepository<Activite, Long> {

	@Query("Select c from Activite c Where c.nom like :key and c.entrepriseId =:entrepriseId")
	Page<Activite> findByNomContainingAndEntrepriseId(Pageable pageable, @Param("key") String nom, Long entrepriseId);

	boolean existsActiviteByNom(String nom);
	
	Page<Activite>findAll(Pageable pageable);
	
	@Query("Select a from Activite a Where a.nom like %?1%")
	List<Activite> findByNom(String nom);
	
	@Query("Select a from Activite a Where a.nom like %?1%")
	public Page<Activite> findAll(Pageable pageable, String keyword);
	
//public List<Activite> findAll(String keyword);
	@Query("SELECT new com.team.dev.dto.ActiviteResponseDto( a.id, a.nom, p.titrePlanification) from Activite a Join a.planifications p")
    List<ActiviteResponseDto> getActivitesByPlanification();
}
