package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.utils.ProductTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryProductRepositoryTest {

    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    void shouldReturnProductWhenProductWithGivenIdExists() {
        // Given
        Product expected = ProductTestData.builder()
                .build()
                .buildProduct();
        repository.save(expected);

        // When
        Optional<Product> foundProduct = repository.findById(expected.getUuid());

        // Then
        assertTrue(foundProduct.isPresent(), "Product should be found");
        assertEquals(expected, foundProduct.get(), "Found product should match the saved product");
    }

    @Test
    void shouldReturnEmptyOptionalWhenProductWithGivenIdDoesNotExist() {
        // Given
        UUID uuid = UUID.fromString("c249fc5b-4a25-4212-83ca-2c6ec0d57d0b");

        // When
        Optional<Product> foundProduct = repository.findById(uuid);

        // Then
        assertFalse(foundProduct.isPresent(), "Product should not be found");
    }

    @Test
    void shouldSaveAndReturnProduct() {
        // Given
        Product expected = ProductTestData.builder()
                .withUuid(null)
                .build()
                .buildProduct();

        // When
        Product savedProduct = repository.save(expected);

        // Then
        assertThat(savedProduct)
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName());
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
        Product expected = ProductTestData.builder()
                .build()
                .buildProduct();
        repository.save(expected);

        // When
        repository.delete(expected.getUuid());

        // Then
        assertFalse(repository.findById(expected.getUuid()).isPresent(), "Product should be deleted");
    }

    @Test
    void shouldReturnAllSavedProducts() {
        // Given
        Product expected1 = ProductTestData.builder()
                .withUuid(null)
                .build()
                .buildProduct();
        Product expected2 = ProductTestData.builder()
                .withUuid(UUID.fromString("ebc3c5b1-aeaa-44f5-8d8a-bfcc53de36e6"))
                .withName("ProductName1")
                .withDescription("ProductDescription1")
                .withPrice(BigDecimal.valueOf(120))
                .withCreated(LocalDateTime.of(2023, 11, 13, 12, 34))
                .build()
                .buildProduct();
        repository.save(expected1);
        repository.save(expected2);

        // When
        List<Product> allProducts = repository.findAll();

        // Then
        assertEquals(2, allProducts.size(), "All saved products should be returned");
        assertTrue(allProducts.containsAll(Arrays.asList(expected1, expected2)), "All saved products should be in the returned list");
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsAreSaved() {
        // When
        List<Product> allProducts = repository.findAll();

        // Then
        assertTrue(allProducts.isEmpty(), "When no products are saved, an empty list should be returned");
    }
}