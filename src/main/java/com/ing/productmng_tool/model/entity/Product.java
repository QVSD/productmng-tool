package com.ing.productmng_tool.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * This entity is persisted in the {@code products} table and models
 * the core business concept of a sellable product.
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * Primary key of the product.
     * Generated automatically using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The display name of the product.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Optional detailed description of the product.
     * Limited to 1000 characters.
     */
    @Column(length = 1000)
    private String description;

    /**
     * Monetary price of the product.
     *
     * <p>Uses precision 19 and scale 4 to safely represent financial values.</p>
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    /**
     * Timestamp representing when the product was created.
     * Automatically set on initial persistence.
     * Not updatable.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp representing the last time the product was updated.
     * Automatically updated on modification.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Version field used for optimistic locking.
     *
     * <p>Ensures that concurrent updates do not overwrite each other silently.</p>
     */
    @Version
    private Long version;

    /**
     * Default constructor required by JPA.
     */
    public Product() {
    }

    /**
     * Creates a new product instance.
     *
     * @param name        product name (must not be null)
     * @param description product description
     * @param price       monetary price (must not be null)
     */
    public Product(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Lifecycle callback executed before the entity is persisted.
     * Initializes audit timestamps.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback executed before the entity is updated.
     * Updates the modification timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * @return the unique identifier of the product
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the product price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @return last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @return current optimistic locking version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Updates the product name.
     *
     * @param name new product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the product description.
     *
     * @param description new product description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Updates the product price.
     *
     * @param price new product price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
