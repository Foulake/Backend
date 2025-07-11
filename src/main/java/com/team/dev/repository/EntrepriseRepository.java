package com.team.dev.repository;

import com.team.dev.model.Activite;
import com.team.dev.model.Entreprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

    @Query("Select c from Entreprise c Where c.nom like :key")
    Page<Entreprise> findByNomContaining(Pageable pageable, @Param("key") String nom);

    Page<Entreprise> findAll(Pageable pageable);
    boolean existsEntrepriseByNom(String nom);
}
