package com.shopping.platform.pricing.product;

import com.shopping.platform.pricing.discount.DiscountPolicyService;
import com.shopping.platform.pricing.discount.model.DiscountPolicy;
import com.shopping.platform.pricing.discount.model.DiscountRule;
import com.shopping.platform.pricing.discount.model.DiscountType;
import com.shopping.platform.pricing.product.model.Product;
import com.shopping.platform.pricing.product.model.ProductOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    DiscountPolicyService discountPolicyService;

    @Test
    void shouldCalculatePriceWhenNoDiscountPolicy() {
        Product product = productService.createProduct(Product.builder()
                .price(BigDecimal.TEN)
                .build());

        ProductOrder result = productService.calculatePrice(product.getIdentifier(), new ProductOrder(100));

        assertThat(result.getAmount()).isEqualTo(100);
        assertThat(result.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    void shouldCalculatePriceWhenPercentagePolicy() {
        DiscountPolicy winterSale = discountPolicyService.createDiscountPolicy(DiscountPolicy.builder()
                .name("winter sale")
                .type(DiscountType.PERCENTAGE_BASED)
                .discountRules(List.of(new DiscountRule(BigDecimal.valueOf(15))))
                .build());

        Product product = productService.createProduct(Product.builder()
                .price(BigDecimal.TEN)
                .discountPolicy(winterSale.getIdentifier())
                .build());

        ProductOrder result = productService.calculatePrice(product.getIdentifier(), new ProductOrder(100));

        assertThat(result.getAmount()).isEqualTo(100);
        assertThat(result.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(850));
    }

    @Test
    void shouldCalculatePriceWhenAmountPolicy() {
        DiscountPolicy summerSale = discountPolicyService.createDiscountPolicy(DiscountPolicy.builder()
                .name("summer sale")
                .type(DiscountType.AMOUNT_BASED)
                .discountRules(List.of(
                        new DiscountRule(3, BigDecimal.valueOf(10)),
                        new DiscountRule(5, BigDecimal.valueOf(20)),
                        new DiscountRule(10, BigDecimal.valueOf(50)))
                )
                .build());

        Product product = productService.createProduct(Product.builder()
                .price(BigDecimal.TEN)
                .discountPolicy(summerSale.getIdentifier())
                .build());

        ProductOrder result0 = productService.calculatePrice(product.getIdentifier(), new ProductOrder(0));
        ProductOrder result1 = productService.calculatePrice(product.getIdentifier(), new ProductOrder(1));
        ProductOrder result3 = productService.calculatePrice(product.getIdentifier(), new ProductOrder(3));
        ProductOrder result5 = productService.calculatePrice(product.getIdentifier(), new ProductOrder(5));
        ProductOrder result10 = productService.calculatePrice(product.getIdentifier(), new ProductOrder(10));
        ProductOrder result20 = productService.calculatePrice(product.getIdentifier(), new ProductOrder(20));

        assertThat(result0.getAmount()).isEqualTo(0);
        assertThat(result0.getTotalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result1.getAmount()).isEqualTo(1);
        assertThat(result1.getTotalPrice()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(result3.getAmount()).isEqualTo(3);
        assertThat(result3.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(20));
        assertThat(result5.getAmount()).isEqualTo(5);
        assertThat(result5.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(30));
        assertThat(result10.getAmount()).isEqualTo(10);
        assertThat(result10.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(result20.getAmount()).isEqualTo(20);
        assertThat(result20.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }
}