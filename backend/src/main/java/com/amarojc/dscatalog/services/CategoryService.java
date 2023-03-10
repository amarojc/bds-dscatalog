package com.amarojc.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarojc.dscatalog.dtos.CategoryDTO;
import com.amarojc.dscatalog.entities.Category;
import com.amarojc.dscatalog.repositories.CategoryRepository;

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
}
