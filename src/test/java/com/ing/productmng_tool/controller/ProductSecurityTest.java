package com.ing.productmng_tool.controller;

import com.ing.productmng_tool.security.SecurityConfig;
import com.ing.productmng_tool.security.CustomAccessDeniedHandler;
import com.ing.productmng_tool.security.CustomAuthenticationEntryPoint;
import com.ing.productmng_tool.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ing.productmng_tool.service.ProductService;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class,
        CustomAccessDeniedHandler.class,
        CustomAuthenticationEntryPoint.class,
        GlobalExceptionHandler.class})
class ProductSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService service;

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

    // 401 - no authentication
    @Test
    void shouldReturn401_whenNoAuthentication() throws Exception {

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isUnauthorized());
    }

    // 403 - wrong role
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn403_whenUserAccessesAdminEndpoint() throws Exception {

        String json = """
        {
            "name": "Gun",
            "description": "Arctic Warfare Police",
            "price": 1000
        }
        """;

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
    }

    // 200 - admin allowed
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminAccess() throws Exception {

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }
}