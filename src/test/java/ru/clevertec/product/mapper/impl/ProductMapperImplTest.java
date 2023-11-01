package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperImplTest {

    private final ProductMapper productMapper = new ProductMapperImpl();
    private final UUID fixedUuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
    private final LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 12, 0);

    @Test
    void toProduct_ShouldCreateProductFromDto_WhenGivenValidProductDto() {
        ProductDto productDto = new ProductDto("Test Name", "Test Description", BigDecimal.valueOf(99.99));
        Product result = productMapper.toProduct(productDto);
        assertNotNull(result, "Result should not be null");
        assertNull(result.getUuid(), "UUID of the new product should be null");
        assertEquals("Test Name", result.getName(), "Product name does not match");
        assertEquals("Test Description", result.getDescription(), "Product description does not match");
        assertEquals(BigDecimal.valueOf(99.99), result.getPrice(), "Product price does not match");
        assertNull(result.getCreated(), "Creation date of the new product should be null");
    }

    @Test
    void toInfoProductDto_ShouldCreateInfoProductDtoFromProduct_WhenGivenValidProduct() {
        Product product = new Product(fixedUuid, "Test Name", "Test Description", BigDecimal.valueOf(99.99), fixedDateTime);
        InfoProductDto result = productMapper.toInfoProductDto(product);
        assertNotNull(result, "Result should not be null");
        assertEquals(fixedUuid, result.uuid(), "Product UUID does not match");
        assertEquals("Test Name", result.name(), "Product name does not match");
        assertEquals("Test Description", result.description(), "Product description does not match");
        assertEquals(BigDecimal.valueOf(99.99), result.price(), "Product price does not match");
    }

    @Test
    void merge_ShouldUpdateProductFromDto_WhenGivenValidProductAndProductDto() {
        Product product = new Product(fixedUuid, "Old Name", "Old Description", BigDecimal.valueOf(49.99), fixedDateTime);
        ProductDto productDto = new ProductDto("New Name", "New Description", BigDecimal.valueOf(19.99));
        Product updatedProduct = productMapper.merge(product, productDto);
        assertNotNull(updatedProduct, "Result should not be null");
        assertEquals(fixedUuid, updatedProduct.getUuid(), "Product UUID should remain the same");
        assertEquals("New Name", updatedProduct.getName(), "Product name should be updated");
        assertEquals("New Description", updatedProduct.getDescription(), "Product description should be updated");
        assertEquals(BigDecimal.valueOf(19.99), updatedProduct.getPrice(), "Product price should be updated");
        assertEquals(fixedDateTime, updatedProduct.getCreated(), "Creation date of the product should remain the same");
    }
}