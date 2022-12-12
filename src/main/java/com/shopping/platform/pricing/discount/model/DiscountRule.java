package com.shopping.platform.pricing.discount.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountRule {
    private Integer fromAmount;
    private BigDecimal discountValue;

    public DiscountRule() {
    }

    public DiscountRule(BigDecimal discountValue) {
        this.fromAmount = 1;
        this.discountValue = discountValue;
    }

    public DiscountRule(Integer fromAmount, BigDecimal discountValue) {
        this.fromAmount = fromAmount;
        this.discountValue = discountValue;
    }
}

