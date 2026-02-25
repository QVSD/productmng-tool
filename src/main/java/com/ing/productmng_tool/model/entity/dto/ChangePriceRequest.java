package com.ing.productmng_tool.model.entity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO used to update the price of an existing product.
 *
 * <p>This object is consumed by the
 * {@code PATCH /api/products/{id}/price} endpoint.</p>
 *
 * <p>Validation ensures the new price:
 * <ul>
 *     <li>Is not null</li>
 *     <li>Is strictly greater than zero</li>
 * </ul>
 * </p>
 *
 * @param newPrice the updated price value (must be positive)
 */
public record ChangePriceRequest(

        @NotNull(message = "New price must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal newPrice
) {}