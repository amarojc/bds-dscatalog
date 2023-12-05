package com.amarojc.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amarojc.dscatalog.entities.Category;
import com.amarojc.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	/* Regra não funciona no PostgreSQL"
	@Query( "SELECT DISTINCT product FROM Product product "
			+ "INNER JOIN product.categories cats "
			+ "WHERE (:category IS NULL OR :category IN cats) "
			+ "AND (LOWER(product.name) LIKE LOWER(CONCAT('%',:name,'%')))")
	Page<Product> find(Category category, String name, Pageable pageable);
	*/

	//Regra não funciona no h2... analisar
	@Query( "SELECT DISTINCT product FROM Product product "
			+ "INNER JOIN product.categories cats "
			+ "WHERE (COALESCE(:categories) IS NULL OR cats IN :categories) "
			+ "AND (:name = '' OR LOWER(product.name) LIKE LOWER(CONCAT('%',:name,'%')))")
	Page<Product> find(List<Category> categories, String name, Pageable pageable);
	
	//JOIN FETCH não funciona no Page
	@Query("SELECT product FROM Product product "
			+ "JOIN FETCH product.categories "
			+ "WHERE product IN :products")
	List<Product> findProductsWithCategories(List<Product> products);
}
