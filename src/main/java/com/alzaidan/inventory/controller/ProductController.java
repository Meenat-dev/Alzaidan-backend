package com.alzaidan.inventory.controller;

import com.alzaidan.inventory.dto.ProductDto;
import com.alzaidan.inventory.service.impl.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Product Management", description = "Create, read, update and delete products in the catalogue")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ── GET ALL / SEARCH ──────────────────────────────────────────────────────
    @Operation(
        summary = "List or search products",
        description = "Returns all products. Optionally filter by a search term (name or SKU) and/or a category label " +
                      "(Electronics, Food & Bev, Household, Clothing, Others). Leave both blank to return everything."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product list returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised — missing or invalid token")
    })
    @GetMapping
    public ResponseEntity<List<ProductDto.Response>> getAll(
            @Parameter(description = "Search term matched against product name or SKU")
            @RequestParam(required = false) String search,
            @Parameter(description = "Category label to filter by, e.g. Electronics")
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(productService.getAll(search, category));
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────
    @Operation(
        summary = "Get product by ID",
        description = "Returns a single product record by its database ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found and returned"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.Response> getById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // ── GET LOW STOCK ─────────────────────────────────────────────────────────
    @Operation(
        summary = "Get low-stock products",
        description = "Returns all products whose current stock is at or below their configured minimum stock threshold " +
                      "but greater than zero. Used to populate the low-stock alert banner on the Dashboard."
    )
    @ApiResponse(responseCode = "200", description = "Low-stock products returned")
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto.Response>> getLowStock() {
        return ResponseEntity.ok(productService.getLowStock());
    }

    // ── CREATE ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Create a new product",
        description = "Adds a new product to the catalogue. The SKU must be unique across the system. " +
                      "Roles allowed: ADMINISTRATOR, INVENTORY_OFFICER."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error — check field constraints"),
        @ApiResponse(responseCode = "409", description = "Conflict — SKU already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorised"),
        @ApiResponse(responseCode = "403", description = "Forbidden — insufficient role")
    })
    @PostMapping
    public ResponseEntity<ProductDto.Response> create(
            @Valid @RequestBody ProductDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Update an existing product",
        description = "Updates all editable fields of an existing product by ID. Stock quantity is managed through " +
                      "the Inventory module. Roles allowed: ADMINISTRATOR, INVENTORY_OFFICER."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Conflict — SKU already used by another product"),
        @ApiResponse(responseCode = "401", description = "Unauthorised"),
        @ApiResponse(responseCode = "403", description = "Forbidden — insufficient role")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto.Response> update(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProductDto.Request request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @Operation(
        summary = "Delete a product",
        description = "Permanently removes a product from the catalogue. This action cannot be undone. " +
                      "Role allowed: ADMINISTRATOR only."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product deleted — no content returned"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorised"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMINISTRATOR role required")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}