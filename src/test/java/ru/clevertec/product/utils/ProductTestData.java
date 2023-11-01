package ru.clevertec.product.utils;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(setterPrefix = "with")
@Data
public class ProductTestData {

    @Builder.Default
    private UUID uuid = UUID.fromString("c249fc5b-4a25-4212-83ca-2c6ec0d57d0b");

    @Builder.Default
    private String name = "ProductName";

    @Builder.Default
    private String description = "ProductDescription";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(100);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.of(2023, 10, 15, 12, 34);

    public Product buildProduct() {
        return new Product(uuid, name, description, price, created);
    }

    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }
}
