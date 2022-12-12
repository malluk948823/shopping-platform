package com.shopping.platform.pricing.discount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPolicy {
    private String identifier;
    private String name;
    private DiscountType type;
    private List<DiscountRule> discountRules;
}
