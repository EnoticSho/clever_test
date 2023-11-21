package ru.clevertec.product.exception;

public class ProductCanNotBeNull extends RuntimeException{
        public ProductCanNotBeNull() {
            super("Product cannot be null");
        }
}
