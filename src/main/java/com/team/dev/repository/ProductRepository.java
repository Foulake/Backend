 package com.team.dev.repository;


 import com.team.dev.model.Product;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;
 import org.springframework.stereotype.Repository;

 import java.math.BigDecimal;
 import java.util.List;

 

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	boolean existsProductByCode(String code);
	Page<Product> findAll(Pageable pageable);
	
	@Query("Select p from Product p Where p.name like %?1%")
	List<Product> findByName(String name);

    @Query("Select c from Product c Where c.name like :key and c.entrepriseId =:entrepriseId")
    Page<Product> findByNameContainingAndEntrepriseId(Pageable pageable, @Param("key") String name, Long entrepriseId);

	//boolean countProductByQte(boolean b);
}