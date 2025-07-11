package com.team.dev.repository;

import com.team.dev.dto.CategoryResponseDto;
import com.team.dev.model.Category;
import com.team.dev.model.Champ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>{

	@Query("Select c from Category c Where c.nom like :key and c.entrepriseId =:entrepriseId")
	Page<Category> findByNomContainingAndEntrepriseId(Pageable pageable, @Param("key") String nom, Long entrepriseId);
	boolean existsCategorysByNom(String nom);
	Page<Category> findAll(Pageable pageable);

	@Query("SELECT new com.team.dev.dto.CategoryResponseDto(c.id , c.nom, p.name) FROM Category c JOIN c.products p")
	public List<CategoryResponseDto> getCategorysByProduct();
	
	@Query("SELECT m FROM Category m WHERE m.nom like %?1%")
	public List<Category> findByNom(String nom);

}
