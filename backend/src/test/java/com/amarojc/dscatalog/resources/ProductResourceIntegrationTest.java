package com.amarojc.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.amarojc.dscatalog.dtos.ProductDTO;
import com.amarojc.dscatalog.tests.ProductFactory;
import com.amarojc.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	private String operatorUserName;
	private String operatorPassword;
	
	private String adminUserName;
	private String adminPassword;
	
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000l;
		countTotalProducts = 25L;
		productDTO = ProductFactory.createProductDTO();
	
		operatorUserName = "alex@gmail.com";
		operatorPassword = "123456";
		
		adminUserName = "maria@gmail.com";
		adminPassword = "123456";
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{
		
		ResultActions result = mockMvc.perform(
				 get("/products?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON)
				);
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		//result.andExpect(jsonPath("$.content").isNotEmpty());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
		
		
	}
				
	@Test
	public void updateShouldReturnProductDTOWhenIdExist() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUserName, adminPassword);
						
		ResultActions result = mockMvc.perform(
				put("/products/{id}", existingId)
				.content(jsonBody)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				);	
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUserName, adminPassword);
		
		ResultActions result = mockMvc.perform(
				put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				);
		
		result.andExpect(status().isNotFound());
	}
}
