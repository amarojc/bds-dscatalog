package com.amarojc.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.amarojc.dscatalog.repositories.ProductRepository;
import com.amarojc.dscatalog.services.exceptions.DatabaseIntegrityViolationException;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 100L;
		dependentId = 4L;
		
		/* Configurando os comportamentos simulados do ProductRepository
		  utilizando o Mockito */
		
		//Não irá fazer nada caso o id exista
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		
		//Deverá lançar uma exceção caso o id não exista
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		
		//Deverá lançar uma exceção em caso de violação de integridade
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
	}
	
	/**
	 *@Test delete não deve fazer nada caso o id exista ao ser chamado apenas uma vez.
	 */
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			productService.deleteProduct(existingId);
		});
		
		//Assertions do Mockito
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}
	
	/**
	 *@Test delete deve lançar uma exceção caso o id não exista.
	 */
	@Test
	public void deteleShouldThrowObjectNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			productService.deleteProduct(nonExistingId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	/**
	 * @Test deve lançar um exceção caso tentativa de violação de integridade no banco de dados
	 */
	@Test
	public void deleteShouldThrowDatabaseIntegrityViolationExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseIntegrityViolationException.class, () -> {
			productService.deleteProduct(dependentId);
		});
		
		Mockito.verify(productRepository,Mockito.times(1)).deleteById(dependentId);
	}
}
