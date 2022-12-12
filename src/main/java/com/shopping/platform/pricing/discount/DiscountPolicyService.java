package com.shopping.platform.pricing.discount;

import com.shopping.platform.pricing.discount.model.DiscountPolicy;
import com.shopping.platform.pricing.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiscountPolicyService {
    private final DiscountPolicyRepository discountPolicyRepository;

    public DiscountPolicyService(DiscountPolicyRepository discountPolicyRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
    }

    public DiscountPolicy createDiscountPolicy(DiscountPolicy discountPolicy) {
        discountPolicy.setIdentifier(UUID.randomUUID().toString());

        discountPolicyRepository.saveDiscountPolicy(discountPolicy);

        return discountPolicy;
    }

    public DiscountPolicy replaceDiscountPolicy(String identifier, DiscountPolicy discountPolicy) {
        if (!discountPolicyRepository.containsDiscountPolicy(identifier)) {
            throw new ValidationException("Discount policy does not exist: " + identifier);
        }

        discountPolicy.setIdentifier(identifier);

        discountPolicyRepository.saveDiscountPolicy(discountPolicy);

        return discountPolicy;
    }

    public DiscountPolicy getDiscountPolicy(String identifier) {
        if (identifier == null) {
            return null;
        }
        return discountPolicyRepository.getDiscountPolicy(identifier);
    }

    public List<DiscountPolicy> getAllDiscountPolicies() {
        return discountPolicyRepository.getAllDiscountPolicies()
                .stream()
                .sorted(Comparator.comparing(DiscountPolicy::getName))
                .collect(Collectors.toList());
    }

    public boolean containsDiscountPolicy(String identifier) {
        if (identifier == null) {
            return false;
        }
        return discountPolicyRepository.containsDiscountPolicy(identifier);
    }

    public void deleteDiscountPolicy(String identifier) {
        if (!discountPolicyRepository.containsDiscountPolicy(identifier)) {
            throw new ValidationException("Discount policy does not exist: " + identifier);
        }
        discountPolicyRepository.deleteDiscountPolicy(identifier);
    }
}
