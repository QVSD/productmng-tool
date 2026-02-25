package com.ing.productmng_tool.mapper;

import com.ing.productmng_tool.model.entity.Product;
import com.ing.productmng_tool.model.entity.dto.ProductRequest;
import com.ing.productmng_tool.model.entity.dto.ProductResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting between Product entity
 * and its corresponding Data Transfer Objects (DTOs).
 *
 * <p>This component isolates mapping logic from business logic,
 * keeping the service layer clean and focused on domain behavior.</p>
 *
 * <p>Manual mapping is used instead of automated tools
 * to maintain explicit control and readability.</p>
 */
@Component
public class ProductMapper {

    /**
     * Converts a {@link ProductRequest} into a {@link Product} entity.
     *
     * <p>Used when creating new products.</p>
     *
     * @param request incoming request DTO
     * @return mapped Product entity or null if request is null
     */
    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        return new Product(
                request.name(),
                request.description(),
                request.price()
        );
    }

    /**
     * Converts a {@link Product} entity into a {@link ProductResponse}.
     *
     * <p>Used when returning product data to the client.</p>
     *
     * @param product persisted product entity
     * @return response DTO or null if product is null
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    /**
     * Updates an existing Product entity using data from a request.
     *
     * <p>Used in PUT or PATCH operations.</p>
     *
     * @param product existing entity to update
     * @param request request containing new values
     */
    public void updateEntity(Product product, ProductRequest request) {
        if (product == null || request == null) {
            return;
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
    }
}