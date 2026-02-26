package com.ing.productmng_tool.controller;


import com.ing.productmng_tool.model.entity.dto.ChangePriceRequest;
import com.ing.productmng_tool.model.entity.dto.ProductRequest;
import com.ing.productmng_tool.model.entity.dto.ProductResponse;
import com.ing.productmng_tool.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * REST controller responsible for managing product resources.
 *
 * <p>Provides CRUD operations under the base path
 * {@code /api/products}.</p>
 *
 * <p>This controller acts as a boundary layer between
 * HTTP requests and the service layer. It performs input validation
 * and delegates business logic to {@link ProductService}.</p>
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    /**
     * Constructs a new ProductController.
     *
     * @param service product service handling business logic
     */
    public ProductController(ProductService service) {
        this.service = service;
    }

    /**
     * Creates a new product.
     *
     * @param request validated product creation request
     * @return created product with HTTP 201 (Created)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = service.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id product identifier
     * @return product details with HTTP 200 (OK)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    /**
     * Retrieves all available products.
     *
     * @return list of products with HTTP 200 (OK)
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    /**
     * Updates the price of a specific product.
     *
     * @param id      product identifier
     * @param request validated request containing the new price
     * @return updated product with HTTP 200 (OK)
     */
    @PatchMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> changePrice(
            @PathVariable Long id,
            @Valid @RequestBody ChangePriceRequest request) {

        return ResponseEntity.ok(service.changePrice(id, request));
    }

    /**
     * Deletes a product by its identifier.
     *
     * @param id product identifier
     * @return HTTP 204 (No Content) if deletion succeeds
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}