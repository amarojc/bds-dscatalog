package com.amarojc.dscatalog.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.amarojc.dscatalog.entities.Category;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCategory;
	
	@NotBlank(message = "Campo obrigatório!")
	private String nameCategory;

	public CategoryDTO() {
	}

	public CategoryDTO(Long idCategory, String nameCategory) {
		super();
		this.idCategory = idCategory;
		this.nameCategory = nameCategory;
	}

	public CategoryDTO(Category category) {
		this.idCategory = category.getIdCategory();
		this.nameCategory = category.getName();
	}

	public Long getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}

	public String getNameCategory() {
		return nameCategory;
	}

	public void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}
	
	
}
