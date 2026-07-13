package com.alzaidan.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleDto {

    // ── Cart Item ─────────────────────────────────────────────────────────────
    public static class CartItem {
        @NotNull(message = "Product ID is required")
        private Long productId;
        @NotNull @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        public Long getProductId()      { return productId; }
        public Integer getQuantity()    { return quantity; }
        public void setProductId(Long productId)    { this.productId = productId; }
        public void setQuantity(Integer quantity)   { this.quantity = quantity; }
    }

    // ── Checkout Request ──────────────────────────────────────────────────────
    public static class CheckoutRequest {
        @NotBlank(message = "Customer name is required") @Size(max = 100)
        private String customerName;
        @NotBlank(message = "Payment method is required")
        private String paymentMethod;
        private BigDecimal amountPaid;
        @NotEmpty(message = "Cart cannot be empty") @Valid
        private List<CartItem> items;

        public String getCustomerName()         { return customerName; }
        public String getPaymentMethod()        { return paymentMethod; }
        public BigDecimal getAmountPaid()       { return amountPaid; }
        public List<CartItem> getItems()        { return items; }

        public void setCustomerName(String customerName)    { this.customerName = customerName; }
        public void setPaymentMethod(String paymentMethod)  { this.paymentMethod = paymentMethod; }
        public void setAmountPaid(BigDecimal amountPaid)    { this.amountPaid = amountPaid; }
        public void setItems(List<CartItem> items)          { this.items = items; }
    }

    // ── Item Response ─────────────────────────────────────────────────────────
    public static class ItemResponse {
        private Long productId;
        private String productName;
        private String sku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;

        public ItemResponse() {}

        public Long getProductId()       { return productId; }
        public String getProductName()   { return productName; }
        public String getSku()           { return sku; }
        public Integer getQuantity()     { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public BigDecimal getLineTotal() { return lineTotal; }

        public void setProductId(Long productId)        { this.productId = productId; }
        public void setProductName(String productName)  { this.productName = productName; }
        public void setSku(String sku)                  { this.sku = sku; }
        public void setQuantity(Integer quantity)       { this.quantity = quantity; }
        public void setUnitPrice(BigDecimal unitPrice)  { this.unitPrice = unitPrice; }
        public void setLineTotal(BigDecimal lineTotal)  { this.lineTotal = lineTotal; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long productId; private String productName; private String sku;
            private Integer quantity; private BigDecimal unitPrice; private BigDecimal lineTotal;

            public Builder productId(Long productId)        { this.productId = productId; return this; }
            public Builder productName(String productName)  { this.productName = productName; return this; }
            public Builder sku(String sku)                  { this.sku = sku; return this; }
            public Builder quantity(Integer quantity)       { this.quantity = quantity; return this; }
            public Builder unitPrice(BigDecimal unitPrice)  { this.unitPrice = unitPrice; return this; }
            public Builder lineTotal(BigDecimal lineTotal)  { this.lineTotal = lineTotal; return this; }

            public ItemResponse build() {
                ItemResponse r = new ItemResponse();
                r.productId = productId; r.productName = productName; r.sku = sku;
                r.quantity = quantity; r.unitPrice = unitPrice; r.lineTotal = lineTotal;
                return r;
            }
        }
    }

    // ── Sale Response ─────────────────────────────────────────────────────────
    public static class Response {
        private Long id;
        private String transactionRef;
        private String customerName;
        private String paymentMethod;
        private BigDecimal totalAmount;
        private BigDecimal amountPaid;
        private BigDecimal changeDue;
        private String status;
        private List<ItemResponse> items;
        private LocalDateTime createdAt;

        public Response() {}

        public Long getId()                     { return id; }
        public String getTransactionRef()       { return transactionRef; }
        public String getCustomerName()         { return customerName; }
        public String getPaymentMethod()        { return paymentMethod; }
        public BigDecimal getTotalAmount()      { return totalAmount; }
        public BigDecimal getAmountPaid()       { return amountPaid; }
        public BigDecimal getChangeDue()        { return changeDue; }
        public String getStatus()               { return status; }
        public List<ItemResponse> getItems()    { return items; }
        public LocalDateTime getCreatedAt()     { return createdAt; }

        public void setId(Long id)                          { this.id = id; }
        public void setTransactionRef(String ref)           { this.transactionRef = ref; }
        public void setCustomerName(String name)            { this.customerName = name; }
        public void setPaymentMethod(String method)         { this.paymentMethod = method; }
        public void setTotalAmount(BigDecimal total)        { this.totalAmount = total; }
        public void setAmountPaid(BigDecimal amountPaid)    { this.amountPaid = amountPaid; }
        public void setChangeDue(BigDecimal changeDue)      { this.changeDue = changeDue; }
        public void setStatus(String status)                { this.status = status; }
        public void setItems(List<ItemResponse> items)      { this.items = items; }
        public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt = createdAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id; private String transactionRef; private String customerName;
            private String paymentMethod; private BigDecimal totalAmount;
            private BigDecimal amountPaid; private BigDecimal changeDue;
            private String status; private List<ItemResponse> items; private LocalDateTime createdAt;

            public Builder id(Long id)                          { this.id = id; return this; }
            public Builder transactionRef(String ref)           { this.transactionRef = ref; return this; }
            public Builder customerName(String name)            { this.customerName = name; return this; }
            public Builder paymentMethod(String method)         { this.paymentMethod = method; return this; }
            public Builder totalAmount(BigDecimal total)        { this.totalAmount = total; return this; }
            public Builder amountPaid(BigDecimal amountPaid)    { this.amountPaid = amountPaid; return this; }
            public Builder changeDue(BigDecimal changeDue)      { this.changeDue = changeDue; return this; }
            public Builder status(String status)                { this.status = status; return this; }
            public Builder items(List<ItemResponse> items)      { this.items = items; return this; }
            public Builder createdAt(LocalDateTime createdAt)   { this.createdAt = createdAt; return this; }

            public Response build() {
                Response r = new Response();
                r.id = id; r.transactionRef = transactionRef; r.customerName = customerName;
                r.paymentMethod = paymentMethod; r.totalAmount = totalAmount;
                r.amountPaid = amountPaid; r.changeDue = changeDue;
                r.status = status; r.items = items; r.createdAt = createdAt;
                return r;
            }
        }
    }
}
