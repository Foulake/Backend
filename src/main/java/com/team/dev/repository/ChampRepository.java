package com.team.dev.repository;

import com.team.dev.dto.ChampResponseDto;
import com.team.dev.model.Champ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChampRepository extends JpaRepository<Champ, Long> {

	@Query("SELECT new com.team.dev.dto.ChampResponseDto( c.id, c.nomChamp, a.nom) from Champ c Join c.activites a")
	List<ChampResponseDto> getChampsByActivite();

	Page<Champ> findAll(Pageable pageable);

	boolean existsChampByNomChamp(String nomChamp);
	
	@Query("Select c from Champ c Where c.nomChamp like %?1%")
	List<Champ> findByNomChamp(String keyword);
	
	@Query("Select c from Champ c Where c.nomChamp like :key and c.entrepriseId =:entrepriseId")
	Page<Champ> findByNomChampContainingAndEntrepriseId(Pageable pageable, @Param("key") String nomChamp, Long entrepriseId);



}
