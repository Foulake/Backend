package com.team.dev.repository;

import com.team.dev.dto.MagasinResponseDto;
import com.team.dev.model.Activite;
import com.team.dev.model.Magasin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MagasinRepository extends CrudRepository<Magasin, Long>{

	@Query("Select c from Magasin c Where c.nomMagasin like :key and c.entrepriseId =:entrepriseId")
	Page<Magasin> findByNomMagasinContainingAndEntrepriseId(Pageable pageable, @Param("key") String nomMagasin, Long entrepriseId);

	boolean existsByNomMagasin(String nomMagasin);
	Page<Magasin> findAll(Pageable pageable);
	
	@Query("SELECT new com.team.dev.dto.MagasinResponseDto(m.id, m.nomMagasin, p.name) FROM Magasin m JOIN m.products p")
	public List<MagasinResponseDto> getMagasinsByProduct();
	
	@Query("SELECT m FROM Magasin m WHERE m.nomMagasin like %?1%")
	public List<Magasin> findByNomMagasin(String nomMagasin);

}
