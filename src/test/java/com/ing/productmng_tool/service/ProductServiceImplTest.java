package com.ing.productmng_tool.service;

import com.ing.productmng_tool.exception.DuplicateProductException;
import com.ing.productmng_tool.exception.ProductNotFoundException;
import com.ing.productmng_tool.mapper.ProductMapper;
import com.ing.productmng_tool.model.entity.Product;
import com.ing.productmng_tool.model.entity.dto.ChangePriceRequest;
import com.ing.productmng_tool.model.entity.dto.ProductRequest;
import com.ing.productmng_tool.model.entity.dto.ProductResponse;
import com.ing.productmng_tool.repository.ProductRepository;
import com.ing.productmng_tool.service.impl.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Cristiano Ronaldo Man. United Home Jersey 2009",
                "Pay tribute to the style and class of Cristiano Ronaldo with the 2008 Man. United Away Jersey " +
                        "– Long Sleeve, worn during the season that cemented his place in English football history.",
                new BigDecimal("90.00"));
    }

    @Test
    void getProductById_shouldThrowException_whenProductNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> service.getProductById(1L));

        verify(repository).findById(1L);
    }

    @Test
    void changePrice_shouldUpdatePriceCorrectly() {

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Product.class))).thenReturn(null);

        ChangePriceRequest request = new ChangePriceRequest(new BigDecimal("100.00"));

        service.changePrice(1L, request);

        assertEquals(new BigDecimal("100.00"), product.getPrice());
        verify(repository).save(product);
    }

    @Test
    void createProduct_shouldThrowDuplicateException_whenDataIntegrityViolation() {

        when(mapper.toEntity(any(ProductRequest.class))).thenReturn(product);

        when(repository.save(any(Product.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThrows(DuplicateProductException.class,
                () -> service.createProduct(
                        new ProductRequest("Cristiano Ronaldo Man. United Home Jersey 2009",
                                "Pay tribute to the style and class of Cristiano Ronaldo with the 2008 " +
                                        "Man. United Away Jersey – Long Sleeve, worn during the season that cemented " +
                                        "his place in English football history.",
                                new BigDecimal("90"))
                ));
    }

    @Test
    void createProduct_shouldReturnSavedProduct() {

        ProductRequest request =
                new ProductRequest("Socks", "Nike comfy socks", new BigDecimal("30"));

        Product entity = new Product("Socks", "Nike comfy socks", new BigDecimal("30"));

        Product saved = new Product("Socks", "Nike comfy socks", new BigDecimal("30"));

        try {
            var idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(saved, 1L);
        } catch (Exception ignored) {}

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenCallRealMethod();

        ProductResponse response = service.createProduct(request);

        assertEquals("Socks", response.name());
        verify(repository).save(entity);
    }

    @Test
    void getProductById_shouldReturnProduct() {

        Product product = new Product("Protein Bar", "24g protein per 100g", new BigDecimal("5"));

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toResponse(product))
                .thenReturn(new ProductResponse(
                        1L,
                        "Protein Bar",
                        "24g protein per 100g",
                        new BigDecimal("5"),
                        null,
                        null
                ));

        ProductResponse response = service.getProductById(1L);

        assertEquals("Protein Bar", response.name());
    }

    @Test
    void changePrice_shouldThrowException_whenProductNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        ChangePriceRequest request =
                new ChangePriceRequest(new BigDecimal("2500"));

        assertThrows(ProductNotFoundException.class,
                () -> service.changePrice(1L, request));
    }
}