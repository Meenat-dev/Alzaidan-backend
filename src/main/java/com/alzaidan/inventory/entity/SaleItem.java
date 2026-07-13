package com.alzaidan.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_items")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unit_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "line_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineTotal;

    public SaleItem() {}

    public Long getId() {
        return id;
    }

    public Sale getSale() {
        return sale;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final SaleItem item = new SaleItem();

        public Builder sale(Sale sale) {
            item.sale = sale;
            return this;
        }

        public Builder product(Product product) {
            item.product = product;
            return this;
        }

        public Builder quantity(Integer quantity) {
            item.quantity = quantity;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            item.unitPrice = unitPrice;
            return this;
        }

        public Builder unitCost(BigDecimal unitCost) {
            item.unitCost = unitCost;
            return this;
        }

        public Builder lineTotal(BigDecimal lineTotal) {
            item.lineTotal = lineTotal;
            return this;
        }

        public SaleItem build() {
            return item;
        }
    }
}
