package com.shopping.platform.pricing.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductOrder {
    private Integer amount;
    private BigDecimal totalPrice;

    public ProductOrder(Integer amount) {
        this.amount = amount;
    }
}
