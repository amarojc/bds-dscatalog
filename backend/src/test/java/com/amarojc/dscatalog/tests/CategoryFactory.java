package com.amarojc.dscatalog.tests;

import com.amarojc.dscatalog.dtos.CategoryDTO;
import com.amarojc.dscatalog.entities.Category;

public class CategoryFactory {

	public static Category createCategory() {
		return new Category(1L, "Electronics");
	}
	
	public static CategoryDTO createCategoryDTO() {
		return new CategoryDTO(createCategory());
	}
}
