package com.team.dev.repository;

import com.team.dev.dto.LocaliteResponseDto;
import com.team.dev.model.Planification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaningRepository extends JpaRepository<Planification, Long> {

    Page<Planification> findAll(Pageable pageable);

    @Query("SELECT new com.team.dev.dto.LocaliteResponseDto( l.nom, c.nomChamp) from Localite l Join l.champs c")
    List<LocaliteResponseDto> getActivitesByChamp();

     List<Planification> findByTitrePlanification(@Param(value = "titrePlanification") String titrePlanification);

    @Query("Select p from Planification p Where p.titrePlanification like :key and p.entrepriseId =:entrepriseId")
    Page<Planification> findByTitrePlanificationContainingAndEntrepriseId(Pageable pageable, @Param("key") String keyword, Long entrepriseId);

}
