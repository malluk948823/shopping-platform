package com.shopping.platform.pricing.discount;

import com.shopping.platform.pricing.discount.model.DiscountPolicy;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class DiscountPolicyRepository {
    private final Map<String, DiscountPolicy> discountPoliciesStorage = new ConcurrentHashMap<>();

    public void saveDiscountPolicy(DiscountPolicy discountPolicy) {
        discountPoliciesStorage.put(discountPolicy.getIdentifier(), discountPolicy);
    }

    public void deleteDiscountPolicy(String identifier) {
        discountPoliciesStorage.remove(identifier);
    }

    public DiscountPolicy getDiscountPolicy(String identifier) {
        return discountPoliciesStorage.get(identifier);
    }

    public Collection<DiscountPolicy> getAllDiscountPolicies() {
        return discountPoliciesStorage.values();
    }

    public boolean containsDiscountPolicy(String identifier) {
        return discountPoliciesStorage.get(identifier) != null;
    }
}
