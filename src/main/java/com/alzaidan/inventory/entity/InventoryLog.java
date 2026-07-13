package com.alzaidan.inventory.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_logs")
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @Column(name = "stock_before", nullable = false)
    private Integer stockBefore;

    @Column(name = "stock_after", nullable =false)
    private Integer stockAfter;

    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public InventoryLog() {}

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public Integer getStockBefore() {
        return stockBefore;
    }

    public Integer getStockAfter() {
        return stockAfter;
    }

    public String getReason() {
        return reason;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public void setStockBefore(Integer stockBefore) {
        this.stockBefore = stockBefore;
    }

    public void setStockAfter(Integer stockAfter) {
        this.stockAfter = stockAfter;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final InventoryLog log = new InventoryLog();

        public Builder product(Product product) {
            log.product = product;
            return this;
        }

        public Builder movementType(MovementType movementType) {
            log.movementType = movementType;
            return this;
        }

        public Builder quantityChange(Integer quantityChange) {
            log.quantityChange = quantityChange;
            return this;
        }

        public Builder stockBefore(Integer stockBefore) {
            log.stockBefore = stockBefore;
            return this;
        }

        public Builder stockAfter(Integer stockAfter) {
            log.stockAfter = stockAfter;
            return this;
        }

        public Builder reason(String reason) {
            log.reason = reason;
            return this;
        }

        public Builder performedBy(User performedBy) {
            log.performedBy = performedBy;
            return this;
        }

        public InventoryLog build() {
            return log;
        }
    }

    public enum MovementType {
        RESTOCK,
        SALE,
        ADJUSTMENT,
        RETURN
    }
}
