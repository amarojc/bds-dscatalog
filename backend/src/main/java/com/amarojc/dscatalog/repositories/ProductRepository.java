package com.amarojc.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amarojc.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
