package com.ing.productmng_tool.model.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Response DTO representing product data returned to clients.
 *
 * <p>This object is used in responses for:
 * <ul>
 *     <li>{@code GET /api/products}</li>
 *     <li>{@code GET /api/products/{id}}</li>
 *     <li>{@code POST /api/products}</li>
 * </ul>
 * </p>
 *
 * <p>Internal persistence details such as the optimistic locking version
 * are intentionally not exposed to the client.</p>
 *
 * @param id         unique identifier of the product
 * @param name       product name
 * @param description product description
 * @param price      product price
 * @param createdAt  creation timestamp
 * @param updatedAt  last modification timestamp
 */
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}