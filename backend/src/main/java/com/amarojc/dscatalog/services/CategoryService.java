package com.amarojc.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarojc.dscatalog.dtos.CategoryDTO;
import com.amarojc.dscatalog.entities.Category;
import com.amarojc.dscatalog.repositories.CategoryRepository;
import com.amarojc.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository; 
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAllCategory(){
		List<Category> list =  categoryRepository.findAll();
		return list.stream()
				.map(cat -> new CategoryDTO(cat))
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> categoryOptional = categoryRepository.findById(id);
		Category category = categoryOptional
				.orElseThrow(
					() -> new EntityNotFoundException("Categoria não encontrada / Category not found")
				);
		return new CategoryDTO(category);
	}
	
	@Transactional
	public CategoryDTO insertCategory(CategoryDTO categoryDTO) {
		return new CategoryDTO(categoryRepository.save(new Category(categoryDTO)));
	}
}
