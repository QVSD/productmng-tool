package com.ing.productmng_tool.model.entity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO used for creating a new product.
 *
 * <p>This object represents client input for the
 * {@code POST /api/products} endpoint.</p>
 *
 * <p>Validation is performed at the controller boundary to ensure:
 * <ul>
 *     <li>Product name is not blank</li>
 *     <li>Name length does not exceed 255 characters</li>
 *     <li>Price is provided and strictly greater than zero</li>
 * </ul>
 * </p>
 *
 * <p>This DTO protects the domain model from invalid external input.</p>
 *
 * @param name        product name (required, max 255 chars)
 * @param description optional product description (max 1000 chars)
 * @param price       product price (must be positive)
 */
public record ProductRequest(

        @NotBlank(message = "Product name must not be blank")
        @Size(max = 255, message = "Maximum number of characters for product name is 255")
        String name,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @NotNull(message = "Provide a not null price")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price value must be greater than 0")
        BigDecimal price
) {}