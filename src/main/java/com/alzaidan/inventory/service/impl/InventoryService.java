package com.alzaidan.inventory.service.impl;

import com.alzaidan.inventory.dto.InventoryDto;
import com.alzaidan.inventory.dto.ProductDto;
import com.alzaidan.inventory.entity.InventoryLog;
import com.alzaidan.inventory.entity.Product;
import com.alzaidan.inventory.entity.User;
import com.alzaidan.inventory.repository.InventoryLogRepository;
import com.alzaidan.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryLogRepository logRepository;
    private final ProductService productService;

    public InventoryService(ProductRepository productRepository,
                            InventoryLogRepository logRepository,
                            ProductService productService) {
        this.productRepository = productRepository;
        this.logRepository = logRepository;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Response> getAllInventory() {
        return productRepository.findAll()
                .stream()
                .map(productService::toResponse)
                .toList();
    }

    @Transactional
    public ProductDto.Response restock(
            Long productId,
            InventoryDto.RestockRequest request,
            User performedBy) {

        Product product = productService.findOrThrow(productId);

        int before = product.getStock();
        int after = before + request.getQuantity();

        product.setStock(after);
        product.setLastRestocked(LocalDateTime.now());

        productRepository.save(product);

        InventoryLog log = new InventoryLog();
        log.setProduct(product);
        log.setMovementType(InventoryLog.MovementType.RESTOCK);
        log.setQuantityChange(request.getQuantity());
        log.setStockBefore(before);
        log.setStockAfter(after);
        log.setReason(
                request.getReason() != null
                        ? request.getReason()
                        : "Manual restock"
        );
        log.setPerformedBy(performedBy);

        logRepository.save(log);

        return productService.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<InventoryLog> getLogsForProduct(Long productId) {
        return logRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Transactional(readOnly = true)
    public List<InventoryLog> getRecentLogs() {
        return logRepository.findTop50ByOrderByCreatedAtDesc();
    }
}