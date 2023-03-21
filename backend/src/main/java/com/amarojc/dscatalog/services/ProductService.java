package com.amarojc.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarojc.dscatalog.dtos.CategoryDTO;
import com.amarojc.dscatalog.dtos.ProductDTO;
import com.amarojc.dscatalog.entities.Category;
import com.amarojc.dscatalog.entities.Product;
import com.amarojc.dscatalog.repositories.CategoryRepository;
import com.amarojc.dscatalog.repositories.ProductRepository;
import com.amarojc.dscatalog.services.exceptions.DatabaseIntegrityViolationException;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPagedProduct(PageRequest pageRequest){
		Page<Product> list = productRepository.findAll(pageRequest);
		return list.map(prod -> new ProductDTO(prod));
		//return list.map(prod -> new ProductDTO(prod, prod.getCategories()));		
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		Product product = productOptional
					.orElseThrow(
							() -> new ObjectNotFoundException("Id não encontrado / not found " + id)
					  );
		return new ProductDTO(product, product.getCategories());
	}
	
	
	@Transactional
	public ProductDTO insertProduct(ProductDTO productDTO){
		Product product = new Product();
		copyDtoToEntity(productDTO, product);
		product = productRepository.save(product);
		return new ProductDTO(product, product.getCategories());
	}
	
	
	
	@Transactional
	public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
		try {
			Product product = productRepository.getReferenceById(id);		
			copyDtoToEntity(productDTO, product);			
			product = productRepository.save(product);
			
			return new ProductDTO(product, product.getCategories());
		}catch(EntityNotFoundException e){
			throw new ObjectNotFoundException("Produto não encontrado / Product not found " + id);
		}
	}
	
	public void deleteProduct(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectNotFoundException("Produto não encontrado / Product not found " + id);
		}catch(DataIntegrityViolationException dx) {
			throw new DatabaseIntegrityViolationException("Violação de integridade / Integrety Violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product product) {
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setDate(dto.getDate());
		product.setImgUrl(dto.getImgUrl());
		product.setPrice(dto.getPrice());
		
		product.getCategories().clear();
		
		for(CategoryDTO catDTO : dto.getCategories()) {
			Category category = categoryRepository.getReferenceById(catDTO.getIdCategory());
			product.getCategories().add(category);
		}		
	}
}
