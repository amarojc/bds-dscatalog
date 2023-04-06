package com.amarojc.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarojc.dscatalog.dtos.CategoryDTO;
import com.amarojc.dscatalog.entities.Category;
import com.amarojc.dscatalog.repositories.CategoryRepository;
import com.amarojc.dscatalog.services.exceptions.DatabaseIntegrityViolationException;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository; 
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPagedCategory(Pageable pageable){
		Page<Category> list =  categoryRepository.findAll(pageable);		
		return list.map(cat -> new CategoryDTO(cat));
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> categoryOptional = categoryRepository.findById(id);
		Category category = categoryOptional
				.orElseThrow(
					() -> new ObjectNotFoundException("Categoria não encontrada / Category not found")
				);
		return new CategoryDTO(category);
	}
	
	@Transactional
	public CategoryDTO insertCategory(CategoryDTO categoryDTO) {
		return new CategoryDTO(categoryRepository.save(new Category(categoryDTO)));
	}
	
	@Transactional
	public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
		try {			
			Category category = categoryRepository.getReferenceById(id);
			category.setName(categoryDTO.getNameCategory());
			
			return new CategoryDTO(categoryRepository.save(category));
		}catch(EntityNotFoundException ex) {
			throw new ObjectNotFoundException("Id não encontrado / not found: " + id);
		}
	}
	
	
	public void deleteCategory(Long id) {
		try{
			categoryRepository.deleteById(id);
			
		}catch(EmptyResultDataAccessException ex) {
			throw new ObjectNotFoundException("Id não encontrado / not found: " + id);
		
		}catch(DataIntegrityViolationException dex) {
		
			throw new DatabaseIntegrityViolationException("Violação de integridade / Integrity violation");
		}
	}
}
