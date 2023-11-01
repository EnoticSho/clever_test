package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public InfoProductDto get(UUID uuid) {
        return productRepository.findById(uuid)
                .map(productMapper::toInfoProductDto)
                .orElseThrow(() -> new ProductNotFoundException(uuid));
    }

    @Override
    public List<InfoProductDto> getAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toInfoProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        product.setCreated(LocalDateTime.now());
        return productRepository.save(product).getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException(uuid));
        productMapper.merge(product, productDto);
        productRepository.save(product);
    }

    @Override
    public void delete(UUID uuid) {
        if (productRepository.findById(uuid).isEmpty()) {
            throw new ProductNotFoundException(uuid);
        }
        productRepository.delete(uuid);
    }
}
