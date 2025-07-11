package com.team.dev.repository;

import com.team.dev.model.Activite;
import com.team.dev.model.SousChamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SousChampRepository extends CrudRepository<SousChamp, Long>{

	@Query("Select c from SousChamp c Where c.nom like :key and c.entrepriseId=:entrepriseId")
	Page<SousChamp> findByNomContainingAndEntrepriseId(Pageable pageable, @Param("key") String nom, Long entrepriseId);

	//List<SousChamp> findByChampId(Long champId);

	Page<SousChamp> findAll(Pageable pageable);
	
	@Query("Select p from SousChamp p Where p.nom like %?1%"

			//+"or p.id like %?1%"
			+"or p.numero like %?1%")
	public List<SousChamp> findAll(@Param(value = "keyword") String keyword);

}
