package com.alzaidan.inventory.service.impl;

import com.alzaidan.inventory.dto.ProductDto;
import com.alzaidan.inventory.entity.Product;
import com.alzaidan.inventory.exception.GlobalExceptionHandler.*;
import com.alzaidan.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Response> getAll(String search, String category) {

        Product.Category cat = null;

        if (StringUtils.hasText(category) &&
                !category.equalsIgnoreCase("All")) {
            try {
                cat = Product.Category.fromLabel(category);
            } catch (IllegalArgumentException ignored) {
            }
        }

        String q = StringUtils.hasText(search) ? search : null;

        return productRepository.search(q, cat)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDto.Response getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Response> getLowStock() {
        return productRepository.findLowStockProducts()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductDto.Response create(ProductDto.Request request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException(
                    "SKU '" + request.getSku() + "' already exists.");
        }

        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku().toUpperCase())
                .category(Product.Category.fromLabel(request.getCategory()))
                .price(request.getPrice())
                .cost(request.getCost())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .minStock(request.getMinStock() != null ? request.getMinStock() : 5)
                .maxStock(request.getMaxStock() != null ? request.getMaxStock() : 100)
                .unit(request.getUnit())
                .supplier(request.getSupplier())
                .build();

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductDto.Response update(Long id, ProductDto.Request request) {

        Product product = findOrThrow(id);

        if (productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new DuplicateResourceException(
                    "SKU '" + request.getSku() + "' is already in use.");
        }

        product.setName(request.getName());
        product.setSku(request.getSku().toUpperCase());
        product.setCategory(Product.Category.fromLabel(request.getCategory()));
        product.setPrice(request.getPrice());
        product.setCost(request.getCost());
        product.setMinStock(request.getMinStock() != null ? request.getMinStock() : 5);
        product.setMaxStock(request.getMaxStock() != null ? request.getMaxStock() : 100);
        product.setUnit(request.getUnit());
        product.setSupplier(request.getSupplier());

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        productRepository.delete(findOrThrow(id));
    }

    public Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));
    }

    public ProductDto.Response toResponse(Product p) {

        return ProductDto.Response.builder()
                .id(p.getId())
                .name(p.getName())
                .sku(p.getSku())
                .category(p.getCategory().getLabel())
                .price(p.getPrice())
                .cost(p.getCost())
                .stock(p.getStock())
                .minStock(p.getMinStock())
                .maxStock(p.getMaxStock())
                .unit(p.getUnit())
                .supplier(p.getSupplier())
                .status(p.getStatus())
                .lastRestocked(p.getLastRestocked())
                .createdAt(p.getCreatedAt())
                .build();
    }
}