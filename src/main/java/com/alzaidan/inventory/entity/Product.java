package com.alzaidan.inventory.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Category category;

    @Column(name = "selling_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "cost_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "min_stock", nullable = false)
    private Integer minStock = 5;

    @Column(name = "max_stock", nullable = false)
    private Integer maxStock = 100;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(length = 100)
    private String supplier;

    @Column(name = "last_restocked")
    private LocalDateTime lastRestocked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Product() {}

    // ── Lifecycle hooks ───────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Derived status ────────────────────────────────────────────────────────
    @Transient
    public String getStatus() {
        if (stock == null || stock == 0) return "Out of Stock";
        if (stock <= minStock)           return "Low Stock";
        return "Active";
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()                      { return id; }
    public String getName()                  { return name; }
    public String getSku()                   { return sku; }
    public Category getCategory()            { return category; }
    public BigDecimal getPrice()             { return price; }
    public BigDecimal getCost()              { return cost; }
    public Integer getStock()                { return stock; }
    public Integer getMinStock()             { return minStock; }
    public Integer getMaxStock()             { return maxStock; }
    public String getUnit()                  { return unit; }
    public String getSupplier()              { return supplier; }
    public LocalDateTime getLastRestocked()  { return lastRestocked; }
    public LocalDateTime getCreatedAt()      { return createdAt; }
    public LocalDateTime getUpdatedAt()      { return updatedAt; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(Long id)                              { this.id = id; }
    public void setName(String name)                        { this.name = name; }
    public void setSku(String sku)                          { this.sku = sku; }
    public void setCategory(Category category)              { this.category = category; }
    public void setPrice(BigDecimal price)                  { this.price = price; }
    public void setCost(BigDecimal cost)                    { this.cost = cost; }
    public void setStock(Integer stock)                     { this.stock = stock; }
    public void setMinStock(Integer minStock)               { this.minStock = minStock; }
    public void setMaxStock(Integer maxStock)               { this.maxStock = maxStock; }
    public void setUnit(String unit)                        { this.unit = unit; }
    public void setSupplier(String supplier)                { this.supplier = supplier; }
    public void setLastRestocked(LocalDateTime lastRestocked) { this.lastRestocked = lastRestocked; }
    public void setCreatedAt(LocalDateTime createdAt)       { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)       { this.updatedAt = updatedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String sku;
        private Category category;
        private BigDecimal price;
        private BigDecimal cost;
        private Integer stock = 0;
        private Integer minStock = 5;
        private Integer maxStock = 100;
        private String unit;
        private String supplier;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder name(String name)         { this.name = name; return this; }
        public Builder sku(String sku)           { this.sku = sku; return this; }
        public Builder category(Category c)      { this.category = c; return this; }
        public Builder price(BigDecimal price)   { this.price = price; return this; }
        public Builder cost(BigDecimal cost)     { this.cost = cost; return this; }
        public Builder stock(Integer stock)      { this.stock = stock; return this; }
        public Builder minStock(Integer min)     { this.minStock = min; return this; }
        public Builder maxStock(Integer max)     { this.maxStock = max; return this; }
        public Builder unit(String unit)         { this.unit = unit; return this; }
        public Builder supplier(String supplier) { this.supplier = supplier; return this; }

        public Product build() {
            Product p = new Product();
            p.id = this.id; p.name = this.name; p.sku = this.sku;
            p.category = this.category; p.price = this.price; p.cost = this.cost;
            p.stock = this.stock; p.minStock = this.minStock; p.maxStock = this.maxStock;
            p.unit = this.unit; p.supplier = this.supplier;
            return p;
        }
    }

    // ── Category enum ─────────────────────────────────────────────────────────
    public enum Category {
        ELECTRONICS("Electronics"),
        FOOD_AND_BEV("Food & Bev"),
        HOUSEHOLD("Household"),
        CLOTHING("Clothing"),
        OTHERS("Others");

        private final String label;
        Category(String label) { this.label = label; }
        public String getLabel() { return label; }

        public static Category fromLabel(String label) {
            for (Category c : values()) {
                if (c.label.equalsIgnoreCase(label)) return c;
            }
            throw new IllegalArgumentException("Unknown category: " + label);
        }
    }
}
