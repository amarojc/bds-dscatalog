package com.amarojc.dscatalog.entities;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long idCategory;
	private String name;
	
	public Category() {
	}

	public Category(Long idCategory, String name) {
		super();
		this.idCategory = idCategory;
		this.name = name;
	}

	//Mais rápido, mas não 100% garantido...
	@Override
	public int hashCode() {
		return Objects.hash(idCategory);
	}

	//Mais lento, mas confiável para compara a igualdade entre obj
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(idCategory, other.idCategory);
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Category: ");
		builder.append("IdCategory: " + idCategory);
		builder.append("Name: " + name);
		
		return builder.toString();
	}

	public Long getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
