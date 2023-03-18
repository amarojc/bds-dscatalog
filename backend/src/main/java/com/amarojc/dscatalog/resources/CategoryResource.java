package com.amarojc.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amarojc.dscatalog.dtos.CategoryDTO;
import com.amarojc.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService categoryService;
	

	@GetMapping()
	public ResponseEntity<Page<CategoryDTO>> findAll(
				@RequestParam(value = "page", defaultValue = "0") Integer page,
				@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
				@RequestParam(value = "direction", defaultValue = "ASC") String direction,
				@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
			){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<CategoryDTO> list = categoryService.findAllPagedCategory(pageRequest);
		
		return ResponseEntity.ok().body(list); 
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO categoryDTO = categoryService.findById(id);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	@PostMapping()
	public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO categoryDTO){
		 categoryDTO = categoryService.insertCategory(categoryDTO);		 
		 //Constroi a estrutura da URL para ser inserida no cabeçalho da resposta.
		 URI uri = ServletUriComponentsBuilder
				 	.fromCurrentRequest()
				 	.path("/{id}")
				 	.buildAndExpand(categoryDTO.getIdCategory()).toUri();
		 
		 //Created -> retorna o Status 201 com o endereço do novo recurso criado
		 return ResponseEntity.created(uri).body(categoryDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
		categoryDTO = categoryService.updateCategory(id, categoryDTO);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
