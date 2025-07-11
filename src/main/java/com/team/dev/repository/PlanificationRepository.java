package com.team.dev.repository;

import com.team.dev.dto.LocaliteResponseDto;
import com.team.dev.model.Planification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanificationRepository extends CrudRepository<Planification, Long>{
	
	Page<Planification> findAll(Pageable pageable);
	
	@Query("SELECT new com.team.dev.dto.LocaliteResponseDto( l.nom, c.nomChamp) from Localite l Join l.champs c")
	List<LocaliteResponseDto> getActivitesByChamp();
	
	@Query("Select p from Planification p Where p.titrePlanification like %?1%")
	List<Planification> findByTitrePlanification(String titrePlanification);
	
	@Query("Select p from Planification p Where p.titrePlanification like %?1%"
			+"or p.titrePlanification like %?1%"
			//+"or p.id like %?1%"
	)
	//public List<Planification> findAll(String keyword);
	Page<Planification> findAll(Pageable pageable, String keyword);



}
