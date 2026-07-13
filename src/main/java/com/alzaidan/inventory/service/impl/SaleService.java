package com.alzaidan.inventory.service.impl;

import com.alzaidan.inventory.dto.SaleDto;
import com.alzaidan.inventory.entity.InventoryLog;
import com.alzaidan.inventory.entity.Product;
import com.alzaidan.inventory.entity.Sale;
import com.alzaidan.inventory.entity.SaleItem;
import com.alzaidan.inventory.entity.User;
import com.alzaidan.inventory.exception.GlobalExceptionHandler.BusinessRuleException;
import com.alzaidan.inventory.exception.GlobalExceptionHandler.InsufficientStockException;
import com.alzaidan.inventory.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.alzaidan.inventory.repository.InventoryLogRepository;
import com.alzaidan.inventory.repository.ProductRepository;
import com.alzaidan.inventory.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;

    private static final AtomicLong TXN_COUNTER = new AtomicLong(0);

    public SaleService(
            SaleRepository saleRepository,
            ProductRepository productRepository,
            InventoryLogRepository inventoryLogRepository) {

        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.inventoryLogRepository = inventoryLogRepository;
    }

    @Transactional(readOnly = true)
    public List<SaleDto.Response> getAll(String search) {

        List<Sale> sales = StringUtils.hasText(search)
                ? saleRepository.search(search)
                : saleRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();

        return sales.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SaleDto.Response getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public SaleDto.Response checkout(SaleDto.CheckoutRequest request, User createdBy) {

        List<Product> products = request.getItems().stream()
                .map(item -> productRepository.findById(item.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found: " + item.getProductId())))
                .toList();

        for (int i = 0; i < request.getItems().size(); i++) {

            SaleDto.CartItem cartItem = request.getItems().get(i);
            Product product = products.get(i);

            if (product.getStock() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for '" + product.getName() +
                                "'. Available: " + product.getStock() +
                                ", Requested: " + cartItem.getQuantity());
            }
        }

        Sale.PaymentMethod paymentMethod =
                parsePaymentMethod(request.getPaymentMethod());

        BigDecimal total = BigDecimal.ZERO;

        // Create Sale manually
        Sale sale = new Sale();
        sale.setTransactionRef(generateRef());
        sale.setCustomerName(request.getCustomerName());
        sale.setPaymentMethod(paymentMethod);
        sale.setStatus(Sale.Status.COMPLETED);
        sale.setCreatedBy(createdBy);

        for (int i = 0; i < request.getItems().size(); i++) {

            SaleDto.CartItem cartItem = request.getItems().get(i);
            Product product = products.get(i);

            BigDecimal lineTotal =
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            total = total.add(lineTotal);

            // Create SaleItem manually
            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(product);
            saleItem.setQuantity(cartItem.getQuantity());
            saleItem.setUnitPrice(product.getPrice());
            saleItem.setUnitCost(product.getCost());
            saleItem.setLineTotal(lineTotal);

            sale.addItem(saleItem);

            int before = product.getStock();
            int after = before - cartItem.getQuantity();

            product.setStock(after);
            productRepository.save(product);

            // Create InventoryLog manually
            InventoryLog log = new InventoryLog();
            log.setProduct(product);
            log.setMovementType(InventoryLog.MovementType.SALE);
            log.setQuantityChange(-cartItem.getQuantity());
            log.setStockBefore(before);
            log.setStockAfter(after);
            log.setReason("Sale: " + sale.getTransactionRef());
            log.setPerformedBy(createdBy);

            inventoryLogRepository.save(log);
        }

        sale.setTotalAmount(total);

        BigDecimal paid = request.getAmountPaid() != null
                ? request.getAmountPaid()
                : total;

        BigDecimal change = paid.subtract(total).max(BigDecimal.ZERO);

        sale.setAmountPaid(paid);
        sale.setChangeDue(change);

        return toResponse(saleRepository.save(sale));
    }

    private Sale findOrThrow(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sale not found: " + id));
    }

    private String generateRef() {
        long count = saleRepository.count() + TXN_COUNTER.incrementAndGet();
        return "TXN-" + String.format("%03d", count);
    }

    private Sale.PaymentMethod parsePaymentMethod(String method) {
        return switch (method.toUpperCase().replace(" ", "_")) {
            case "CASH" -> Sale.PaymentMethod.CASH;
            case "BANK_TRANSFER", "TRANSFER" -> Sale.PaymentMethod.BANK_TRANSFER;
            case "POS" -> Sale.PaymentMethod.POS;
            case "MOBILE_MONEY" -> Sale.PaymentMethod.MOBILE_MONEY;
            default -> throw new BusinessRuleException(
                    "Unknown payment method: " + method);
        };
    }

    public SaleDto.Response toResponse(Sale sale) {

        List<SaleDto.ItemResponse> items = sale.getItems().stream()
                .map(item -> SaleDto.ItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .sku(item.getProduct().getSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .lineTotal(item.getLineTotal())
                        .build())
                .toList();

        return SaleDto.Response.builder()
                .id(sale.getId())
                .transactionRef(sale.getTransactionRef())
                .customerName(sale.getCustomerName())
                .paymentMethod(sale.getPaymentMethod().name())
                .totalAmount(sale.getTotalAmount())
                .amountPaid(sale.getAmountPaid())
                .changeDue(sale.getChangeDue())
                .status(sale.getStatus().name())
                .items(items)
                .createdAt(sale.getCreatedAt())
                .build();
    }
}





//package com.alzaidan.inventory.service.impl;
//
//import com.alzaidan.inventory.dto.SaleDto;
//import com.alzaidan.inventory.entity.*;
//import com.alzaidan.inventory.exception.GlobalExceptionHandler.*;
//import com.alzaidan.inventory.repository.InventoryLogRepository;
//import com.alzaidan.inventory.repository.ProductRepository;
//import com.alzaidan.inventory.repository.SaleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicLong;
//
//@Service
//@RequiredArgsConstructor
//public class SaleService {
//
//    private final SaleRepository saleRepository;
//    private final ProductRepository productRepository;
//    private final InventoryLogRepository inventoryLogRepository;
//
//    private static final AtomicLong TXN_COUNTER = new AtomicLong(0);
//
//    @Transactional(readOnly = true)
//    public List<SaleDto.Response> getAll(String search) {
//        List<Sale> sales = StringUtils.hasText(search)
//                ? saleRepository.search(search)
//                : saleRepository.findAll().stream()
//                        .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
//                        .toList();
//
//        return sales.stream()
//                .map(this::toResponse)
//                .toList();
//    }
//
//    @Transactional(readOnly = true)
//    public SaleDto.Response getById(Long id) {
//        return toResponse(findOrThrow(id));
//    }
//
//    @Transactional
//    public SaleDto.Response checkout(SaleDto.CheckoutRequest request, User createdBy) {
//
//        List<Product> products = request.getItems().stream()
//                .map(item -> productRepository.findById(item.getProductId())
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Product not found: " + item.getProductId())))
//                .toList();
//
//        for (int i = 0; i < request.getItems().size(); i++) {
//            SaleDto.CartItem cartItem = request.getItems().get(i);
//            Product product = products.get(i);
//
//            if (product.getStock() < cartItem.getQuantity()) {
//                throw new InsufficientStockException(
//                        "Insufficient stock for '" + product.getName() +
//                        "'. Available: " + product.getStock() +
//                        ", Requested: " + cartItem.getQuantity());
//            }
//        }
//
//        Sale.PaymentMethod paymentMethod =
//                parsePaymentMethod(request.getPaymentMethod());
//
//        BigDecimal total = BigDecimal.ZERO;
//
//        Sale sale = Sale.builder()
//                .transactionRef(generateRef())
//                .customerName(request.getCustomerName())
//                .paymentMethod(paymentMethod)
//                .status(Sale.Status.COMPLETED)
//                .createdBy(createdBy)
//                .build();
//
//        for (int i = 0; i < request.getItems().size(); i++) {
//
//            SaleDto.CartItem cartItem = request.getItems().get(i);
//            Product product = products.get(i);
//
//            BigDecimal lineTotal = product.getPrice()
//                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
//
//            total = total.add(lineTotal);
//
//            sale.addItem(
//                    SaleItem.builder()
//                            .product(product)
//                            .quantity(cartItem.getQuantity())
//                            .unitPrice(product.getPrice())
//                            .unitCost(product.getCost())
//                            .lineTotal(lineTotal)
//                            .build());
//
//            int before = product.getStock();
//            int after = before - cartItem.getQuantity();
//
//            product.setStock(after);
//            productRepository.save(product);
//
//            inventoryLogRepository.save(
//                    InventoryLog.builder()
//                            .product(product)
//                            .movementType(InventoryLog.MovementType.SALE)
//                            .quantityChange(-cartItem.getQuantity())
//                            .stockBefore(before)
//                            .stockAfter(after)
//                            .reason("Sale: " + sale.getTransactionRef())
//                            .performedBy(createdBy)
//                            .build());
//        }
//
//        sale.setTotalAmount(total);
//
//        BigDecimal paid = request.getAmountPaid() != null
//                ? request.getAmountPaid()
//                : total;
//
//        BigDecimal change = paid.subtract(total).max(BigDecimal.ZERO);
//
//        sale.setAmountPaid(paid);
//        sale.setChangeDue(change);
//
//        return toResponse(saleRepository.save(sale));
//    }
//
//    private Sale findOrThrow(Long id) {
//        return saleRepository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "Sale not found: " + id));
//    }
//
//    private String generateRef() {
//        long count = saleRepository.count() + TXN_COUNTER.incrementAndGet();
//        return "TXN-" + String.format("%03d", count);
//    }
//
//    private Sale.PaymentMethod parsePaymentMethod(String method) {
//        return switch (method.toUpperCase().replace(" ", "_")) {
//            case "CASH" -> Sale.PaymentMethod.CASH;
//            case "BANK_TRANSFER", "TRANSFER" -> Sale.PaymentMethod.BANK_TRANSFER;
//            case "POS" -> Sale.PaymentMethod.POS;
//            case "MOBILE_MONEY" -> Sale.PaymentMethod.MOBILE_MONEY;
//            default ->
//                    throw new BusinessRuleException(
//                            "Unknown payment method: " + method);
//        };
//    }
//
//    public SaleDto.Response toResponse(Sale sale) {
//
//        List<SaleDto.ItemResponse> items = sale.getItems().stream()
//                .map(item -> SaleDto.ItemResponse.builder()
//                        .productId(item.getProduct().getId())
//                        .productName(item.getProduct().getName())
//                        .sku(item.getProduct().getSku())
//                        .quantity(item.getQuantity())
//                        .unitPrice(item.getUnitPrice())
//                        .lineTotal(item.getLineTotal())
//                        .build())
//                .toList();
//
//        return SaleDto.Response.builder()
//                .id(sale.getId())
//                .transactionRef(sale.getTransactionRef())
//                .customerName(sale.getCustomerName())
//                .paymentMethod(sale.getPaymentMethod().name())
//                .totalAmount(sale.getTotalAmount())
//                .amountPaid(sale.getAmountPaid())
//                .changeDue(sale.getChangeDue())
//                .status(sale.getStatus().name())
//                .items(items)
//                .createdAt(sale.getCreatedAt())
//                .build();
//    }
//}
