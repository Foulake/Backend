package com.team.dev.repository;

import com.team.dev.model.Recolte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecolteRepository extends JpaRepository<Recolte,Long> {
    //@Query("Select c from Recolte c Where c.startDate like :key and c.entrepriseId =:entrepriseId")

    @Query("Select c from Recolte c Where c.typeCulture like :key and c.entrepriseId =:entrepriseId")
    Page<Recolte> findByTypeCultureContainingAndEntrepriseId(Pageable pageable,@Param("key") String typeCulture, Long entrepriseId);

    List<Recolte> findDistinctByEntrepriseIdAndStartDateBetween(Long entrepriseI,@Param(value = "startDate") Date startDate, @Param(value = "endDate") Date endDate);

}
