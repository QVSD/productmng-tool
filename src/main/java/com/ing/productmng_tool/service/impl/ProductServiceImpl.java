package com.ing.productmng_tool.service.impl;

import com.ing.productmng_tool.exception.DuplicateProductException;
import com.ing.productmng_tool.exception.ProductNotFoundException;
import com.ing.productmng_tool.mapper.ProductMapper;
import com.ing.productmng_tool.model.entity.Product;
import com.ing.productmng_tool.model.entity.dto.ChangePriceRequest;
import com.ing.productmng_tool.model.entity.dto.ProductRequest;
import com.ing.productmng_tool.model.entity.dto.ProductResponse;
import com.ing.productmng_tool.repository.ProductRepository;
import com.ing.productmng_tool.service.ProductService;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link ProductService}.
 *
 * <p>This service encapsulates business logic related to product management,
 * including creation, retrieval, update, and deletion operations.</p>
 *
 * <p>All write operations are executed within transactional boundaries.
 * Read operations are marked as {@code readOnly = true} for performance optimization.</p>
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    /**
     * Constructs a new ProductServiceImpl.
     *
     * @param repository product persistence repository
     * @param mapper     mapper responsible for entity-DTO conversions
     */
    public ProductServiceImpl(ProductRepository repository,
                              ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Creates a new product.
     *
     * <p>If a product with the same name already exists,
     * a {@link DuplicateProductException} is thrown.</p>
     *
     * @param request request containing product details
     * @return created product as response DTO
     * @throws DuplicateProductException if a product with the same name exists
     */
    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = mapper.toEntity(request);

        try {
            Product saved = repository.save(product);
            log.info("Product created id={} name='{}' price={}", saved.getId(), saved.getName(), saved.getPrice());
            return mapper.toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Create product rejected - duplicate name='{}'", request.name());
            throw new DuplicateProductException("Product with this name already exists");
        }
    }

    /**
     * Retrieves a product by its identifier.
     *
     * @param id product unique identifier
     * @return product response DTO
     * @throws ProductNotFoundException if no product is found with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        return mapper.toResponse(product);
    }

    /**
     * Retrieves all available products.
     *
     * @return list of product response DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Updates the price of an existing product.
     *
     * <p>The update is performed within a transactional context.
     * Optimistic locking is handled via the {@code @Version} field
     * in the {@link Product} entity.</p>
     *
     * @param id      product identifier
     * @param request request containing the new price
     * @return updated product as response DTO
     */
    @Override
    public ProductResponse changePrice(Long id, ChangePriceRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        var oldPrice = product.getPrice();
        product.setPrice(request.newPrice());

        Product updated = repository.save(product);
        log.info("Product price changed id={} oldPrice={} newPrice={}", updated.getId(), oldPrice, updated.getPrice());
        return mapper.toResponse(updated);
    }

    /**
     * Deletes a product by its identifier.
     *
     * @param id product identifier
     * @throws ProductNotFoundException if the product does not exist
     */
    @Override
    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            log.warn("Delete product refused - not found id={}", id);
            throw new ProductNotFoundException("There was no product found with id: " + id);
        }

        repository.deleteById(id);
        log.info("Product deleted id={}", id);
    }
}