package com.alzaidan.inventory.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_ref", nullable = false, unique = true, length = 20)
    private String transactionRef;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "amount_paid", precision = 15, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "change_due", precision = 15, scale = 2)
    private BigDecimal changeDue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Sale() {}

    // ── Lifecycle ─────────────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    public void addItem(SaleItem item) {
        items.add(item);
        item.setSale(this);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()                     { return id; }
    public String getTransactionRef()       { return transactionRef; }
    public String getCustomerName()         { return customerName; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public BigDecimal getTotalAmount()      { return totalAmount; }
    public BigDecimal getAmountPaid()       { return amountPaid; }
    public BigDecimal getChangeDue()        { return changeDue; }
    public Status getStatus()               { return status; }
    public User getCreatedBy()              { return createdBy; }
    public List<SaleItem> getItems()        { return items; }
    public LocalDateTime getCreatedAt()     { return createdAt; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(Long id)                          { this.id = id; }
    public void setTransactionRef(String ref)           { this.transactionRef = ref; }
    public void setCustomerName(String customerName)    { this.customerName = customerName; }
    public void setPaymentMethod(PaymentMethod method)  { this.paymentMethod = method; }
    public void setTotalAmount(BigDecimal total)        { this.totalAmount = total; }
    public void setAmountPaid(BigDecimal amountPaid)    { this.amountPaid = amountPaid; }
    public void setChangeDue(BigDecimal changeDue)      { this.changeDue = changeDue; }
    public void setStatus(Status status)                { this.status = status; }
    public void setCreatedBy(User createdBy)            { this.createdBy = createdBy; }
    public void setItems(List<SaleItem> items)          { this.items = items; }
    public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt = createdAt; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String transactionRef;
        private String customerName;
        private PaymentMethod paymentMethod;
        private BigDecimal totalAmount;
        private BigDecimal amountPaid;
        private BigDecimal changeDue;
        private Status status;
        private User createdBy;

        public Builder id(Long id)                         { this.id = id; return this; }
        public Builder transactionRef(String ref)          { this.transactionRef = ref; return this; }
        public Builder customerName(String name)           { this.customerName = name; return this; }
        public Builder paymentMethod(PaymentMethod method) { this.paymentMethod = method; return this; }
        public Builder totalAmount(BigDecimal total)       { this.totalAmount = total; return this; }
        public Builder amountPaid(BigDecimal paid)         { this.amountPaid = paid; return this; }
        public Builder changeDue(BigDecimal change)        { this.changeDue = change; return this; }
        public Builder status(Status status)               { this.status = status; return this; }
        public Builder createdBy(User createdBy)           { this.createdBy = createdBy; return this; }

        public Sale build() {
            Sale s = new Sale();
            s.id = this.id; s.transactionRef = this.transactionRef;
            s.customerName = this.customerName; s.paymentMethod = this.paymentMethod;
            s.totalAmount = this.totalAmount; s.amountPaid = this.amountPaid;
            s.changeDue = this.changeDue; s.status = this.status;
            s.createdBy = this.createdBy;
            return s;
        }
    }

    // ── Enums ─────────────────────────────────────────────────────────────────
    public enum PaymentMethod { CASH, BANK_TRANSFER, POS, MOBILE_MONEY }
    public enum Status        { COMPLETED, PENDING, CANCELLED }
}
