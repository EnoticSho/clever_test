package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.utils.ProductTestData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void shouldReturnInfoProductDtoWhenProductExists() {
        //Given
        UUID uuid = UUID.fromString("3ecb77f7-0114-47a7-ada7-3ec685d202a7");

        Product product = ProductTestData.builder()
                .build()
                .buildProduct();

        InfoProductDto infoProductDto = ProductTestData.builder()
                .build()
                .buildInfoProductDto();

        when(productRepository.findById(uuid))
                .thenReturn(Optional.of(product));

        when(productMapper.toInfoProductDto(product))
                .thenReturn(infoProductDto);

        //When
        InfoProductDto result = productService.get(uuid);

        //Then
        assertEquals(infoProductDto, result);
        verify(productRepository).findById(uuid);
        verify(productMapper).toInfoProductDto(product);
    }

    @Test
    public void shouldThrowExceptionWhenProductDoesNotExist() {
        //Given
        UUID uuid = UUID.fromString("3ecb77f7-0114-47a7-ada7-3ec685d202a7");
        when(productRepository.findById(uuid))
                .thenReturn(Optional.empty());

        //When
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.get(uuid));

        //Then
        assertEquals(String.format("Product with uuid: %s not found", uuid), exception.getMessage());

        verify(productRepository)
                .findById(uuid);

        verifyNoInteractions(productMapper);
    }

    @Test
    public void shouldReturnListOfInfoProductDtoWhenProductsExist() {
        //Given
        Product product = ProductTestData.builder()
                .build()
                .buildProduct();

        InfoProductDto infoProductDto = ProductTestData.builder()
                .build()
                .buildInfoProductDto();

        List<Product> products = Collections.singletonList(product);
        List<InfoProductDto> expected = Collections.singletonList(infoProductDto);

        when(productRepository.findAll())
                .thenReturn(products);

        when(productMapper.toInfoProductDto(product))
                .thenReturn(infoProductDto);

        //When
        List<InfoProductDto> result = productService.getAll();

        //Then
        assertEquals(expected, result);

        verify(productRepository)
                .findAll();

        verify(productMapper)
                .toInfoProductDto(product);
    }

    @Test
    public void shouldReturnEmptyListWhenNoProductsExist() {
        //Given
        when(productRepository.findAll())
                .thenReturn(Collections.emptyList());

        //When
        List<InfoProductDto> result = productService.getAll();

        //Then
        assertTrue(result.isEmpty());

        verify(productRepository)
                .findAll();

        verifyNoInteractions(productMapper);
    }

    @Test
    public void shouldCreateProductAndReturnUuid() {
        //Given
        Product product = ProductTestData.builder()
                .build()
                .buildProduct();

        ProductDto productDto = ProductTestData.builder()
                .build()
                .buildProductDto();

        when(productMapper.toProduct(productDto))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(product);

        //When
        UUID result = productService.create(productDto);

        //Then
        assertEquals(product.getUuid(), result);

        verify(productMapper)
                .toProduct(productDto);

        verify(productRepository)
                .save(product);
    }

    @Test
    public void shouldUpdateExistingProduct() {
        //Given
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        Product product = ProductTestData.builder()
                .build()
                .buildProduct();

        ProductDto productDto = ProductTestData.builder()
                .withName("New name")
                .withDescription("New description")
                .build()
                .buildProductDto();

        when(productRepository.findById(product.getUuid()))
                .thenReturn(Optional.of(product));

        when(productMapper.merge(product, productDto))
                .thenReturn(product);

        //When
        productService.update(product.getUuid(), productDto);

        //Then
        verify(productRepository)
                .findById(product.getUuid());

        verify(productMapper)
                .merge(product, productDto);

        verify(productRepository)
                .save(productCaptor.capture());

        assertEquals(product, productCaptor.getValue());
    }

    @Test
    public void shouldDeleteExistingProduct() {
        //Given
        UUID uuid = UUID.fromString("3ecb77f7-0114-47a7-ada7-3ec685d202a7");
        Product product = ProductTestData.builder()
                .build()
                .buildProduct();
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));

        //When
        productService.delete(uuid);

        //Then
        verify(productRepository).findById(uuid);
        verify(productRepository).delete(uuid);
    }
}