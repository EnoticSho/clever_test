package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryProductRepositoryTest {

    private ProductRepository repository;

    private final UUID productId1 = UUID.fromString("c249fc5b-4a25-4212-83ca-2c6ec0d57d0b");
    private final UUID productId2 = UUID.fromString("7675cf50-6e43-4070-9e2f-74721d46403b");
    private final LocalDateTime fixedDateTime = LocalDateTime.of(2023, 10, 15, 12, 34);

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    void shouldReturnProductWhenProductWithGivenIdExists() {
        // Given
        Product product = new Product(productId1, "ProductName", "ProductDescription", BigDecimal.valueOf(100), fixedDateTime);
        repository.save(product);

        // When
        Optional<Product> foundProduct = repository.findById(productId1);

        // Then
        assertTrue(foundProduct.isPresent(), "Product should be found");
        assertEquals(product, foundProduct.get(), "Found product should match the saved product");
    }

    @Test
    void shouldReturnEmptyOptionalWhenProductWithGivenIdDoesNotExist() {
        // Given & When
        Optional<Product> foundProduct = repository.findById(productId1);

        // Then
        assertFalse(foundProduct.isPresent(), "Product should not be found");
    }

    @Test
    void shouldSaveAndReturnProduct() {
        // Given
        Product product = new Product(null, "ProductName", "ProductDescription", BigDecimal.valueOf(100), fixedDateTime);

        // When
        Product savedProduct = repository.save(product);

        // Then
        assertEquals("ProductName", savedProduct.getName(), "Saved product should have the correct name");
    }

    @Test
    void shouldThrowExceptionWhenTryingToSaveNullProduct() {
        // Given & When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> repository.save(null));
        assertEquals("Product cannot be null", thrown.getMessage(), "Exception message should match");
    }

    @Test
    void shouldDeleteProductWhenProductWithGivenIdExists() {
        // Given
        Product product = new Product(productId1, "ProductName", "ProductDescription", BigDecimal.valueOf(100), fixedDateTime);
        repository.save(product);

        // When
        repository.delete(productId1);

        // Then
        assertFalse(repository.findById(productId1).isPresent(), "Product should be deleted");
    }

    @Test
    void shouldReturnAllSavedProducts() {
        // Given
        Product product1 = new Product(productId1, "ProductName1", "ProductDescription1", BigDecimal.valueOf(100), fixedDateTime);
        Product product2 = new Product(productId2, "ProductName2", "ProductDescription2", BigDecimal.valueOf(200), fixedDateTime);
        repository.save(product1);
        repository.save(product2);

        // When
        List<Product> allProducts = repository.findAll();

        // Then
        assertEquals(2, allProducts.size(), "All saved products should be returned");
        assertTrue(allProducts.containsAll(Arrays.asList(product1, product2)), "All saved products should be in the returned list");
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsAreSaved() {
        // When
        List<Product> allProducts = repository.findAll();

        // Then
        assertTrue(allProducts.isEmpty(), "When no products are saved, an empty list should be returned");
    }
}