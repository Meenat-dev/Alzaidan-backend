package com.alzaidan.inventory.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {

    // ── Request ───────────────────────────────────────────────────────────────
    public static class Request {
        @NotBlank(message = "Product name is required") @Size(max = 150)
        private String name;
        @NotBlank(message = "SKU is required") @Size(max = 30)
        private String sku;
        @NotBlank(message = "Category is required")
        private String category;
        @NotNull(message = "Selling price is required")
        @DecimalMin(value = "0.0", inclusive = false)
        private BigDecimal price;
        @NotNull(message = "Cost price is required")
        @DecimalMin(value = "0.0", inclusive = false)
        private BigDecimal cost;
        @Min(0) private Integer stock = 0;
        @Min(0) private Integer minStock = 5;
        @Min(1) private Integer maxStock = 100;
        @NotBlank @Size(max = 20) private String unit;
        @Size(max = 100) private String supplier;

        public String getName()      { return name; }
        public String getSku()       { return sku; }
        public String getCategory()  { return category; }
        public BigDecimal getPrice() { return price; }
        public BigDecimal getCost()  { return cost; }
        public Integer getStock()    { return stock; }
        public Integer getMinStock() { return minStock; }
        public Integer getMaxStock() { return maxStock; }
        public String getUnit()      { return unit; }
        public String getSupplier()  { return supplier; }

        public void setName(String name)           { this.name = name; }
        public void setSku(String sku)             { this.sku = sku; }
        public void setCategory(String category)   { this.category = category; }
        public void setPrice(BigDecimal price)     { this.price = price; }
        public void setCost(BigDecimal cost)       { this.cost = cost; }
        public void setStock(Integer stock)        { this.stock = stock; }
        public void setMinStock(Integer minStock)  { this.minStock = minStock; }
        public void setMaxStock(Integer maxStock)  { this.maxStock = maxStock; }
        public void setUnit(String unit)           { this.unit = unit; }
        public void setSupplier(String supplier)   { this.supplier = supplier; }
    }

    // ── Response ──────────────────────────────────────────────────────────────
    public static class Response {
        private Long id;
        private String name;
        private String sku;
        private String category;
        private BigDecimal price;
        private BigDecimal cost;
        private Integer stock;
        private Integer minStock;
        private Integer maxStock;
        private String unit;
        private String supplier;
        private String status;
        private LocalDateTime lastRestocked;
        private LocalDateTime createdAt;

        public Response() {}

        public Long getId()                      { return id; }
        public String getName()                  { return name; }
        public String getSku()                   { return sku; }
        public String getCategory()              { return category; }
        public BigDecimal getPrice()             { return price; }
        public BigDecimal getCost()              { return cost; }
        public Integer getStock()                { return stock; }
        public Integer getMinStock()             { return minStock; }
        public Integer getMaxStock()             { return maxStock; }
        public String getUnit()                  { return unit; }
        public String getSupplier()              { return supplier; }
        public String getStatus()                { return status; }
        public LocalDateTime getLastRestocked()  { return lastRestocked; }
        public LocalDateTime getCreatedAt()      { return createdAt; }

        public void setId(Long id)                              { this.id = id; }
        public void setName(String name)                        { this.name = name; }
        public void setSku(String sku)                          { this.sku = sku; }
        public void setCategory(String category)                { this.category = category; }
        public void setPrice(BigDecimal price)                  { this.price = price; }
        public void setCost(BigDecimal cost)                    { this.cost = cost; }
        public void setStock(Integer stock)                     { this.stock = stock; }
        public void setMinStock(Integer minStock)               { this.minStock = minStock; }
        public void setMaxStock(Integer maxStock)               { this.maxStock = maxStock; }
        public void setUnit(String unit)                        { this.unit = unit; }
        public void setSupplier(String supplier)                { this.supplier = supplier; }
        public void setStatus(String status)                    { this.status = status; }
        public void setLastRestocked(LocalDateTime lastRestocked){ this.lastRestocked = lastRestocked; }
        public void setCreatedAt(LocalDateTime createdAt)       { this.createdAt = createdAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id; private String name; private String sku; private String category;
            private BigDecimal price; private BigDecimal cost; private Integer stock;
            private Integer minStock; private Integer maxStock; private String unit;
            private String supplier; private String status;
            private LocalDateTime lastRestocked; private LocalDateTime createdAt;

            public Builder id(Long id)                              { this.id = id; return this; }
            public Builder name(String name)                        { this.name = name; return this; }
            public Builder sku(String sku)                          { this.sku = sku; return this; }
            public Builder category(String category)                { this.category = category; return this; }
            public Builder price(BigDecimal price)                  { this.price = price; return this; }
            public Builder cost(BigDecimal cost)                    { this.cost = cost; return this; }
            public Builder stock(Integer stock)                     { this.stock = stock; return this; }
            public Builder minStock(Integer minStock)               { this.minStock = minStock; return this; }
            public Builder maxStock(Integer maxStock)               { this.maxStock = maxStock; return this; }
            public Builder unit(String unit)                        { this.unit = unit; return this; }
            public Builder supplier(String supplier)                { this.supplier = supplier; return this; }
            public Builder status(String status)                    { this.status = status; return this; }
            public Builder lastRestocked(LocalDateTime lr)          { this.lastRestocked = lr; return this; }
            public Builder createdAt(LocalDateTime createdAt)       { this.createdAt = createdAt; return this; }

            public Response build() {
                Response r = new Response();
                r.id = id; r.name = name; r.sku = sku; r.category = category;
                r.price = price; r.cost = cost; r.stock = stock;
                r.minStock = minStock; r.maxStock = maxStock; r.unit = unit;
                r.supplier = supplier; r.status = status;
                r.lastRestocked = lastRestocked; r.createdAt = createdAt;
                return r;
            }
        }
    }
}
