package com.shopping.platform.pricing.product;

import com.shopping.platform.pricing.product.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class ProductRepository {
    private final Map<String, Product> productsStorage = new ConcurrentHashMap<>();

    public void saveProduct(Product product) {
        productsStorage.put(product.getIdentifier(), product);
    }

    public void deleteProduct(String identifier) {
        productsStorage.remove(identifier);
    }

    public Product getProduct(String identifier) {
        return productsStorage.get(identifier);
    }

    public Collection<Product> getAllProducts() {
        return productsStorage.values();
    }
}
