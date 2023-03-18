package com.amarojc.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.amarojc.dscatalog.dtos.CategoryDTO;

@Entity
@Table(name = "tb_category")
public class Category implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategory;
	private String name;
	
	//Gravando no padrão UTC
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updateAt;
	
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
	
	public Category(CategoryDTO categoryDTO) {
		this.idCategory = categoryDTO.getIdCategory();
		this.name = categoryDTO.getNameCategory();
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdateAt() {
		return updateAt;
	}
	
	//Ao criar e/ou atualizar uma categoria será armazenado o instante atual
	@PrePersist
	public void preCreatedPersist() {
		createdAt = Instant.now();
	}
	
	@PreUpdate
	public void preUpdatePersist() {
		updateAt = Instant.now();
	}
	
}
