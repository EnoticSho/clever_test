package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.utils.ProductTestData;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperImplTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    void toProduct_ShouldCreateProductFromDtoWhenGivenValidProductDto() {
        //Given
        ProductDto expectedDto = ProductTestData.builder()
                .build()
                .buildProductDto();

        //When
        Product result = productMapper.toProduct(expectedDto);

        //Then
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, null)
                .hasFieldOrPropertyWithValue(Product.Fields.name, expectedDto.name())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expectedDto.description())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expectedDto.price())
                .hasFieldOrPropertyWithValue(Product.Fields.created, null);
    }

    @Test
    void toInfoProductDto_ShouldCreateInfoProductDtoFromProductWhenGivenValidProduct() {
        //Given
        Product expected = ProductTestData.builder()
                .build()
                .buildProduct();

        //When
        InfoProductDto result = productMapper.toInfoProductDto(expected);

        //Then
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice());
    }

    @Test
    void merge_ShouldUpdateProductFromDtoWhenGivenValidProductAndProductDto() {
        //Given
        Product expectedProduct = ProductTestData.builder()
                .build()
                .buildProduct();

        ProductDto expectedDto = ProductTestData.builder()
                .withName("New Name")
                .withDescription("New Description")
                .withPrice(BigDecimal.valueOf(19.99))
                .build()
                .buildProductDto();

        //When
        Product updatedProduct = productMapper.merge(expectedProduct, expectedDto);

        //Then
        assertThat(updatedProduct)
                .isNotNull()
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expectedProduct.getUuid())
                .hasFieldOrPropertyWithValue(Product.Fields.name, expectedDto.name())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expectedDto.description())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expectedDto.price())
                .hasFieldOrPropertyWithValue(Product.Fields.created, expectedProduct.getCreated());
    }
}