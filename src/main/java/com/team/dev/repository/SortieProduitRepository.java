package com.team.dev.repository;

import com.team.dev.dto.TypeSortie;
import com.team.dev.model.SortieProduit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SortieProduitRepository extends JpaRepository<SortieProduit, Long>, JpaSpecificationExecutor<SortieProduit> {


	public Page<SortieProduit> findAll(Pageable pageable);

	@Query("select sum(m.quantite) from SortieProduit m where m.product.id = :idProduct")
	BigDecimal stockReelProduct(@Param("idProduct") Long idProduct);

	//@Query("Select p from SortieProduit p Where  p.entrepriseId =:entrepriseId")
	public Page<SortieProduit> findAllByEntrepriseIdAndUserId(Pageable pageable, Long idUser, Long entrepriseId);
	public Page<SortieProduit> findAllByTypeSortieAndEntrepriseId(Pageable pageable, TypeSortie keyword, Long entrepriseId);
	public Page<SortieProduit> findSortieProduitsByTypeSortieAndEntrepriseIdAndUserId(Pageable pageable, TypeSortie keyword, Long entrepriseId, Long userId);

	List<SortieProduit> findAllByProductId(Long idProduct);

	Page<SortieProduit> findAllByEntrepriseIdAndUserIdAndDateSortieBetween(Pageable pageable, Long entrepriseId, Long userId, LocalDateTime dateSortie, LocalDateTime dateSortie2);
	List<SortieProduit> findAllByEntrepriseIdAndUserIdAndDateSortieBetween(Long entrepriseId, Long userId, LocalDateTime dateSortie, LocalDateTime dateSortie2);
}
