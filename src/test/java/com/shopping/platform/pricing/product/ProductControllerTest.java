package com.shopping.platform.pricing.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.platform.pricing.product.model.Product;
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
class ProductControllerTest {

    static final String UUID_REGEX = "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}";
    final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldCreateProduct() throws Exception {
        Product product = Product.builder()
                .name("car")
                .price(BigDecimal.TEN)
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identifier").value(matchesRegex(UUID_REGEX)))
                .andExpect(jsonPath("$.name").value("car"))
                .andExpect(jsonPath("$.price").value(10));
    }

    @Test
    @Order(2)
    void shouldGetAllProducts() throws Exception {
        List<Product> products = getAllProducts();

        assertThat(products)
                .hasSize(1)
                .extracting(Product::getName)
                .containsExactlyInAnyOrder("car");
    }

    private List<Product> getAllProducts() throws Exception {
        String response = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, new TypeReference<>() {
        });
    }

    @Test
    @Order(3)
    void shouldGetProductForGivenId() throws Exception {
        List<Product> products = getAllProducts();

        String response = mockMvc.perform(get("/products/" + products.get(0).getIdentifier()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Product product = objectMapper.readValue(response, Product.class);

        assertThat(product).isEqualTo(products.get(0));
    }

    @Test
    @Order(5)
    void shouldReplaceProductForGivenId() throws Exception {
        String identifier = getAllProducts().get(0).getIdentifier();

        Product product = Product.builder()
                .identifier(identifier)
                .name("bike")
                .price(BigDecimal.ONE)
                .build();

        mockMvc.perform(put("/products/" + identifier)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value(identifier))
                .andExpect(jsonPath("$.name").value("bike"))
                .andExpect(jsonPath("$.price").value(1));
    }

    @Test
    @Order(6)
    void shouldDeleteDiscountPolicyForGivenId() throws Exception {
        String identifier = getAllProducts().get(0).getIdentifier();

        mockMvc.perform(delete("/products/" + identifier)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}