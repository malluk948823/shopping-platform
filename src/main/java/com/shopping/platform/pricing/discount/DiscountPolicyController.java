package com.shopping.platform.pricing.discount;

import com.shopping.platform.pricing.discount.model.DiscountPolicy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/discountPolicies")
class DiscountPolicyController {

    private final DiscountPolicyService discountPolicyService;

    public DiscountPolicyController(@RequestBody DiscountPolicyService discountPolicyService) {
        this.discountPolicyService = discountPolicyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiscountPolicy createDiscountPolicy(@RequestBody DiscountPolicy discountPolicy) {
        return discountPolicyService.createDiscountPolicy(discountPolicy);
    }

    @PutMapping("/{id}")
    public DiscountPolicy replaceDiscountPolicy(@PathVariable String id, @RequestBody DiscountPolicy discountPolicy) {
        return discountPolicyService.replaceDiscountPolicy(id, discountPolicy);
    }

    @GetMapping("/{id}")
    public DiscountPolicy getDiscountPolicy(@PathVariable String id) {
        return discountPolicyService.getDiscountPolicy(id);
    }

    @GetMapping
    public List<DiscountPolicy> getAllDiscountPolicies() {
        return discountPolicyService.getAllDiscountPolicies();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiscountPolicy(@PathVariable String id) {
        discountPolicyService.deleteDiscountPolicy(id);
    }
}
