package com.shopping.platform.pricing.discount;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.platform.pricing.discount.model.DiscountPolicy;
import com.shopping.platform.pricing.discount.model.DiscountRule;
import com.shopping.platform.pricing.discount.model.DiscountType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DiscountPolicyControllerTest {

    static final String UUID_REGEX = "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}";
    final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldCreatePercentageBasedDiscountPolicy() throws Exception {
        DiscountPolicy discountPolicy = DiscountPolicy.builder()
                .name("winter sale")
                .type(DiscountType.PERCENTAGE_BASED)
                .discountRules(List.of(new DiscountRule(BigDecimal.valueOf(15))))
                .build();

        mockMvc.perform(post("/discountPolicies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountPolicy)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identifier").value(matchesRegex(UUID_REGEX)))
                .andExpect(jsonPath("$.name").value("winter sale"))
                .andExpect(jsonPath("$.type").value("PERCENTAGE_BASED"))
                .andExpect(jsonPath("$.discountRules[0].fromAmount").value(1))
                .andExpect(jsonPath("$.discountRules[0].discountValue").value(15));
    }

    @Test
    @Order(2)
    void shouldCreateAmountBasedDiscountPolicy() throws Exception {
        DiscountPolicy discountPolicy = DiscountPolicy.builder()
                .name("summer sale")
                .type(DiscountType.AMOUNT_BASED)
                .discountRules(List.of(new DiscountRule(BigDecimal.valueOf(15))))
                .build();

        mockMvc.perform(post("/discountPolicies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountPolicy)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identifier").value(matchesRegex(UUID_REGEX)))
                .andExpect(jsonPath("$.name").value("summer sale"))
                .andExpect(jsonPath("$.type").value("AMOUNT_BASED"))
                .andExpect(jsonPath("$.discountRules[0].fromAmount").value(1))
                .andExpect(jsonPath("$.discountRules[0].discountValue").value(15));
    }

    @Test
    @Order(3)
    void shouldGetAllDiscountPolicies() throws Exception {
        List<DiscountPolicy> discountPolicies = getAllDiscountPolicies();

        assertThat(discountPolicies)
                .hasSize(2)
                .extracting(DiscountPolicy::getName)
                .containsExactlyInAnyOrder("summer sale", "winter sale");
    }

    private List<DiscountPolicy> getAllDiscountPolicies() throws Exception {
        String response = mockMvc.perform(get("/discountPolicies"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, new TypeReference<>() {
        });
    }

    @Test
    @Order(4)
    void shouldGetDiscountPolicyForGivenId() throws Exception {
        List<DiscountPolicy> discountPolicies = getAllDiscountPolicies();

        String response = mockMvc.perform(get("/discountPolicies/" + discountPolicies.get(0).getIdentifier()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DiscountPolicy discountPolicy = objectMapper.readValue(response, DiscountPolicy.class);

        assertThat(discountPolicy).isEqualTo(discountPolicies.get(0));
    }

    @Test
    @Order(5)
    void shouldReplaceDiscountPolicyForGivenId() throws Exception {
        String identifier = getAllDiscountPolicies().get(0).getIdentifier();

        DiscountPolicy discountPolicy = DiscountPolicy.builder()
                .identifier(identifier)
                .name("autumn sale")
                .type(DiscountType.PERCENTAGE_BASED)
                .discountRules(List.of(new DiscountRule(BigDecimal.valueOf(10))))
                .build();

        mockMvc.perform(put("/discountPolicies/" + identifier)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountPolicy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value(identifier))
                .andExpect(jsonPath("$.name").value("autumn sale"))
                .andExpect(jsonPath("$.type").value("PERCENTAGE_BASED"))
                .andExpect(jsonPath("$.discountRules[0].fromAmount").value(1))
                .andExpect(jsonPath("$.discountRules[0].discountValue").value(10));
    }

    @Test
    @Order(6)
    void shouldDeleteDiscountPolicyForGivenId() throws Exception {
        String identifier = getAllDiscountPolicies().get(0).getIdentifier();

        mockMvc.perform(delete("/discountPolicies/" + identifier)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}