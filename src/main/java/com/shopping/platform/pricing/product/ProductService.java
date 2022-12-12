package com.shopping.platform.pricing.product;

import com.shopping.platform.pricing.discount.DiscountPolicyService;
import com.shopping.platform.pricing.discount.model.DiscountPolicy;
import com.shopping.platform.pricing.discount.model.DiscountRule;
import com.shopping.platform.pricing.exception.ValidationException;
import com.shopping.platform.pricing.product.model.Product;
import com.shopping.platform.pricing.product.model.ProductOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountPolicyService discountPolicyService;

    public ProductService(ProductRepository productRepository, DiscountPolicyService discountPolicyService) {
        this.productRepository = productRepository;
        this.discountPolicyService = discountPolicyService;
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        product.setIdentifier(UUID.randomUUID().toString());

        productRepository.saveProduct(product);

        return product;
    }

    private void validateProduct(Product product) {
        String discountPolicyId = product.getDiscountPolicy();
        if (discountPolicyId != null && !discountPolicyService.containsDiscountPolicy(discountPolicyId)) {
            throw new ValidationException("Discount policy does not exist: " + discountPolicyId);
        }
    }

    public Product replaceProduct(String identifier, Product product) {
        if (productRepository.getProduct(identifier) == null) {
            throw new ValidationException("Product does not exist: " + identifier);
        }

        validateProduct(product);
        product.setIdentifier(identifier);

        productRepository.saveProduct(product);

        return product;
    }

    public Product getProduct(String identifier) {
        return productRepository.getProduct(identifier);
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts().stream()
                .sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
    }

    public ProductOrder calculatePrice(String identifier, ProductOrder productOrder) {
        Product product = productRepository.getProduct(identifier);

        if (product == null) {
            throw new ValidationException("Product does not exist: " + identifier);
        }

        DiscountPolicy discountPolicy = discountPolicyService.getDiscountPolicy(product.getDiscountPolicy());
        BigDecimal standardPrice = product.getPrice().multiply(BigDecimal.valueOf(productOrder.getAmount()));

        if (discountPolicy == null || discountPolicy.getType() == null) {
            productOrder.setTotalPrice(standardPrice);
            return productOrder;
        }

        BigDecimal discountValue = discountPolicy.getDiscountRules().stream()
                .sorted(Comparator.comparing(DiscountRule::getFromAmount).reversed())
                .filter(rule -> rule.getFromAmount().compareTo(productOrder.getAmount()) <= 0)
                .findFirst()
                .orElseGet(() -> new DiscountRule(0, BigDecimal.ZERO))
                .getDiscountValue();
        switch (discountPolicy.getType()) {
            case PERCENTAGE_BASED: {
                productOrder.setTotalPrice(standardPrice.subtract(standardPrice.multiply(discountValue.multiply(new BigDecimal("0.01")))));
                return productOrder;
            }
            case AMOUNT_BASED: {
                productOrder.setTotalPrice(standardPrice.subtract(discountValue));
                return productOrder;
            }
        }

        return productOrder;
    }

    public void deleteProduct(String identifier) {
        productRepository.deleteProduct(identifier);
    }
}
