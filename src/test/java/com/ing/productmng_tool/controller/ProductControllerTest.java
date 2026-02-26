package com.ing.productmng_tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.productmng_tool.exception.GlobalExceptionHandler;
import com.ing.productmng_tool.exception.ProductNotFoundException;
import com.ing.productmng_tool.model.entity.dto.ProductRequest;
import com.ing.productmng_tool.model.entity.dto.ProductResponse;
import com.ing.productmng_tool.security.CustomAccessDeniedHandler;
import com.ing.productmng_tool.security.CustomAuthenticationEntryPoint;
import com.ing.productmng_tool.security.SecurityConfig;
import com.ing.productmng_tool.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authorization.AuthorizationDeniedException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({
        SecurityConfig.class,
        CustomAccessDeniedHandler.class,
        CustomAuthenticationEntryPoint.class,
        GlobalExceptionHandler.class,
        ProductControllerTest.TestConfig.class
})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test configuration that provides a mocked ProductService bean.
     */
    @TestConfiguration
    static class TestConfig {

        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    @BeforeEach
    void resetMock() {
        Mockito.reset(service);
    }

    // ------------------------------
    // 200 OK
    // ------------------------------

    @Test
    @WithMockUser(roles = "USER")
    void getProduct_shouldReturn200() throws Exception {

        ProductResponse response = new ProductResponse(
                1L,
                "Steak",
                "From Romanian territory",
                new BigDecimal("1000.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(service.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Steak"));
    }

    // ------------------------------
    // 404 Not Found
    // ------------------------------

    @Test
    @WithMockUser(roles = "USER")
    void getProduct_shouldReturn404_whenNotFound() throws Exception {

        when(service.getProductById(1L))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    // ------------------------------
    // 400 Bad Request (validation)
    // ------------------------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_shouldReturn400_whenInvalid() throws Exception {

        ProductRequest request = new ProductRequest(
                "",
                "mskl",
                new BigDecimal("100")
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ------------------------------
    // 401 Unauthorized
    // ------------------------------

    @Test
    void getProduct_shouldReturn401_whenUnauthenticated() throws Exception {

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isUnauthorized());
    }

    // ------------------------------
    // 403 Forbidden
    // ------------------------------

    @Test
    @WithMockUser(roles = "USER")
    void createProduct_shouldReturn403_whenWrongRole() throws Exception {

        ProductRequest request = new ProductRequest(
                "Laptop",
                "desc",
                new BigDecimal("1000")
        );

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}