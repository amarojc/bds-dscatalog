package com.amarojc.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amarojc.dscatalog.dtos.ProductDTO;
import com.amarojc.dscatalog.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductResource {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable){
		Page<ProductDTO> products = productService.findAllPagedProduct(pageable);
		return ResponseEntity.ok().body(products);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		return ResponseEntity.ok().body(productService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO){
		productDTO = productService.insertProduct(productDTO);
		
		URI uri = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(productDTO.getId()).toUri();							
		
		return ResponseEntity.created(uri).body(productDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id,
			@RequestBody ProductDTO productDTO){
		return ResponseEntity.ok().body(productService.updateProduct(id, productDTO));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

}
