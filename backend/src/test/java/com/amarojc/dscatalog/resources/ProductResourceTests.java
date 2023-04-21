package com.amarojc.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.amarojc.dscatalog.dtos.ProductDTO;
import com.amarojc.dscatalog.services.ProductService;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;
import com.amarojc.dscatalog.tests.ProductFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	//Endpoint para fazer testes na camada web
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentID;
	
	private ProductDTO productDTO;
	
	//Objeto concreto
	private PageImpl<ProductDTO> page;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		
		productDTO = ProductFactory.createProductDTO();
			
		page = new PageImpl<>(List.of(productDTO));
				
		/* Configuração dos comportamentos 
		  	simulados de ProductService */
		
		when(productService.findAllPagedProduct(any())).thenReturn(page);
		
		when(productService.findById(existingId)).thenReturn(productDTO);		
		when(productService.findById(nonExistingId)).thenThrow(ObjectNotFoundException.class);
		//Ou...
		//doThrow(ObjectNotFoundException.class).when(productService).findById(nonExistingId);
		
		when(productService.insertProduct(any())).thenReturn(productDTO);
		
		when(productService.updateProduct(eq(existingId), any())).thenReturn(productDTO);
		when(productService.updateProduct(eq(nonExistingId), any())).thenThrow(ObjectNotFoundException.class);
		//Ou...
		//doThrow(ObjectNotFoundException.class).when(productService).updateProduct(eq(nonExistingId), any());
		
		doNothing().when(productService).deleteProduct(existingId);
		doThrow(ObjectNotFoundException.class).when(productService).deleteProduct(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(productService).deleteProduct(dependentID);
	
	}
	
	/**
	 * @Test deve retornar para requisição uma lista de produtos do tipo DTO, 
	 * no formato json.
	 */
	@Test
	public void findAllPagedProductShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON)); 
		
		result.andExpect(status().isOk());
		
		//ou utilizar...
		//mockMvc.perform(get("/products")).andExpect(status().isOk());
	}
	
	/**
	 * @Test deve retornar para a requisição um produto do tipo DTO, 
	 * no formato json, caso id existente;
	 */
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
		//ou utilizar...
		/*
		  mockMvc.perform(get("/products/{id}", existingId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists());
		 */
	}
	
	/**
	 * @Test deve retornar objeto não encontrato, 
	 * na tentativa de buscar o produto, caso id do produto não exista.
	 */
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotIdExist() throws Exception{
		mockMvc.perform(get("/products/{id}", nonExistingId)).andExpect(status().isNotFound());
	}
	
	
	/**
	 * @Test deve criar um novo produto e retornar para requisão 
	 * o novo produto do tipo DTO criado, no formato json.
	 */
	@Test
	public void insertProductShouldReturnProductDTOWhenProductCreated() throws Exception {
	
		String jsonBody = objectMapper.writeValueAsString(productDTO);
				
		ResultActions result =
				mockMvc.perform(post("/products")
								.content(jsonBody)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								);
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	
	/**
	 * @Test deve atualizar o produto caso id exista e 
	 * retornar para a requisição o product do tipo DTO atualizado.
	 */
	@Test
	public void updateProductShouldReturnProductDTOWhenIdExist() throws Exception{
		//Converte o productDTO para string para criar o corpo da requisição 
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	/**
	 * @Test deve retornar objeto não encontrado, na tentativa de atualizar o produto, caso o id não exista.
	 */
	@Test
	public void updateProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	/**
	 * @Test deve retornar a requisição sem conteúdo caso id exista.
	 */
	@Test
	public void deleteProductShouldReturnNoContentWhenIdExist() throws Exception{
		mockMvc.perform(delete("/products/{id}", existingId)
						.accept(MediaType.APPLICATION_JSON)
						).andExpect(status().isNoContent());
		
	}
	
	/**
	 * @Test deve retornar objeto não encontrado, 
	 * na tentativa de excluir o produto, caso o id não exista
	 */
	@Test
	public void deleteProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		mockMvc.perform(delete("/products/{id}", nonExistingId)
								.accept(MediaType.APPLICATION_JSON)
							  ).andExpect(status().isNotFound());								
	}
	
}
