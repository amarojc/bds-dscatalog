package com.amarojc.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.amarojc.dscatalog.dtos.ProductDTO;
import com.amarojc.dscatalog.entities.Category;
import com.amarojc.dscatalog.entities.Product;
import com.amarojc.dscatalog.repositories.CategoryRepository;
import com.amarojc.dscatalog.repositories.ProductRepository;
import com.amarojc.dscatalog.services.exceptions.DatabaseIntegrityViolationException;
import com.amarojc.dscatalog.services.exceptions.ObjectNotFoundException;
import com.amarojc.dscatalog.tests.CategoryFactory;
import com.amarojc.dscatalog.tests.ProductFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		dependentId = 4L;
		product = ProductFactory.createProduct();
		category = CategoryFactory.createCategory();
		page = new PageImpl<>(List.of(product));
		productDTO = ProductFactory.createProductDTO();

		/* Configurando os comportamentos simulados do ProductRepository utilizando o
		   Mockito */

		// Não irá fazer nada caso o id exista
		Mockito.doNothing().when(productRepository).deleteById(existingId);

		// Deve lançar uma exceção caso o id não exista
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

		// Quando passar um objeto qualquer, do tipo pageable, deve retorna um page contendo uma lista de produtos.
		Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

		Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}

	/**
	 * @Test deve retornar o ProdutoDTO quando o id existir
	 */
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() {
		ProductDTO result = productService.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingId, result.getId());
		Mockito.verify(productRepository).findById(existingId);
	}

	/**
	 * @Test deve lançar uma exceção quando o id não existir
	 */
	@Test
	public void findByIdShouldThrowObjectNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			productService.findById(nonExistingId);
		});

		Mockito.verify(productRepository).findById(nonExistingId);
	}

	/**
	 * @Test deve atualizar e retornar o ProdutoDTO quando o id existir
	 */
	@Test
	public void updateShouldReturnProductDTOWhenIdExist() {
		ProductDTO result = productService.updateProduct(existingId, productDTO);

		Assertions.assertNotNull(result);

		Mockito.verify(productRepository).getReferenceById(existingId);
		Mockito.verify(categoryRepository).getReferenceById(existingId);
	}

	/**
	 * @Test deve lançar uma exceção quando o id não existir
	 */
	@Test
	public void updateShouldThrowObjectNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			productService.updateProduct(nonExistingId, productDTO);
		});

		Mockito.verify(productRepository).getReferenceById(nonExistingId);
	}

	/**
	 * @Test deve retornar um Page
	 */
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.findAllPagedProduct(pageable);

		Assertions.assertNotNull(result);
		Mockito.verify(productRepository).findAll(pageable);
	}

	/**
	 * @Test delete não deve fazer nada caso o id exista ao ser chamado apenas uma
	 *       vez.
	 */
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			productService.deleteProduct(existingId);
		});

		// Assertions do Mockito
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}

	/**
	 * @Test delete deve lançar uma exceção caso o id não exista.
	 */
	@Test
	public void deteleShouldThrowObjectNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			productService.deleteProduct(nonExistingId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
	}

	/**
	 * @Test deve lançar um exceção caso tentativa de violação de integridade 
	 * no banco de dados
	 */
	@Test
	public void deleteShouldThrowDatabaseIntegrityViolationExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseIntegrityViolationException.class, () -> {
			productService.deleteProduct(dependentId);
		});

		Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
	}

}
