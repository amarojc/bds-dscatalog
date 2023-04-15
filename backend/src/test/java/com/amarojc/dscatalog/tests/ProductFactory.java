package com.amarojc.dscatalog.tests;

import java.time.Instant;

import com.amarojc.dscatalog.dtos.ProductDTO;
import com.amarojc.dscatalog.entities.Category;
import com.amarojc.dscatalog.entities.Product;

public class ProductFactory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png",
				Instant.parse("2023-10-20T03:00:00Z"));
		product.getCategories().add(new Category(1L, "Electronics"));

		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

}
