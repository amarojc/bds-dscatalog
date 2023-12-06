package com.amarojc.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.amarojc.dscatalog.entities.Product;
import com.amarojc.dscatalog.tests.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	private long existIngId; 
	private long notExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existIngId = 1L;
		notExistingId = 300L;
		countTotalProducts = 25L;
	}
	
	/**
	 * @Test Deve deletar o objeto caso ele exista no Banco de Dados.
	 */
	@Test
	public void deleteShouldDeleteObjectWhenExists() {		
		//Action
		productRepository.deleteById(existIngId);
		Optional<Product> result = productRepository.findById(existIngId);
		//Assert
		//Assertions.assertEquals(Optional.empty(), result);
		//Assertions.assertTrue(result.isEmpty());
		Assertions.assertFalse(result.isPresent());
	}
	
	/**
	 * @Test Deve lançar uma exceção caso o objeto não exista no Banco de Dados.
	 */
	@Test
	public void deleteShouldThrowEmptyResultDataObectWhenIdDoesNotExists() {		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			productRepository.deleteById(notExistingId);
		});
	}
	
	/**
	 * @Test Deve salvar um novo objeto no banco de dados quando id é nulo
	 */
	@Test
	public void saveShouldPersistWithtAutoincrementWhenIdIsNull() {
		Product product = ProductFactory.createProduct();
		product.setId(null);
		
		product = productRepository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());		
	}
	
	/**
	 *@Test findById deve retornar um Optional não vazio quando o id existir
	 */
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		Optional<Product> result = productRepository.findById(existIngId);
		//Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(existIngId, result.get().getId());
	}
	
	/**
	 *@Test findById deve retornar um Optional vazio quando o id não existir
	 */
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
		Optional<Product> result = productRepository.findById(notExistingId);
		Assertions.assertTrue(result.isEmpty());
	}
}
