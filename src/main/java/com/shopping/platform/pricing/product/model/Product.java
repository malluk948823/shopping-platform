package com.shopping.platform.pricing.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String identifier;
    private String name;
    private BigDecimal price;
    private String discountPolicy;
}
