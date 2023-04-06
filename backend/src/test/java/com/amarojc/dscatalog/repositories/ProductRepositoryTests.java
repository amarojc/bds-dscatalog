package com.amarojc.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.amarojc.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	private long existIngId; 
	private long notExistingId;
	
	@BeforeEach
	void setUp() throws Exception{
		existIngId = 1L;
		notExistingId = 300L;
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
	
}
