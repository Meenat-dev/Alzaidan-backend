package com.alzaidan.inventory.controller;

import com.alzaidan.inventory.dto.InventoryDto;
import com.alzaidan.inventory.dto.ProductDto;
import com.alzaidan.inventory.entity.InventoryLog;
import com.alzaidan.inventory.entity.User;
import com.alzaidan.inventory.service.impl.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Inventory Management", description = "Monitor stock levels, restock products, and view movement logs")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // ── GET ALL INVENTORY ─────────────────────────────────────────────────────
    @Operation(
        summary = "Get full inventory",
        description = "Returns all products with their current stock levels, min/max thresholds, stock status " +
                      "(Active, Low Stock, Out of Stock), supplier info, and last restocked date. " +
                      "Available to all authenticated roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventory list returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised — missing or invalid token")
    })
    @GetMapping
    public ResponseEntity<List<ProductDto.Response>> getAll() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    // ── RESTOCK ───────────────────────────────────────────────────────────────
    @Operation(
        summary = "Restock a product",
        description = "Adds the specified quantity to a product's current stock level. " +
                      "The stock change is recorded in the inventory audit log with the reason provided. " +
                      "Also updates the lastRestocked timestamp on the product. " +
                      "Roles allowed: ADMINISTRATOR, INVENTORY_OFFICER."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product restocked — updated product returned"),
        @ApiResponse(responseCode = "400", description = "Validation error — quantity must be at least 1"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorised"),
        @ApiResponse(responseCode = "403", description = "Forbidden — insufficient role")
    })
    @PostMapping("/{productId}/restock")
    public ResponseEntity<ProductDto.Response> restock(
            @Parameter(description = "ID of the product to restock", required = true)
            @PathVariable Long productId,
            @Valid @RequestBody InventoryDto.RestockRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(inventoryService.restock(productId, request, currentUser));
    }

    // ── LOGS FOR A PRODUCT ────────────────────────────────────────────────────
    @Operation(
        summary = "Get movement log for a product",
        description = "Returns the full inventory movement history for a specific product, ordered by most recent first. " +
                      "Each log entry records the movement type (RESTOCK, SALE, ADJUSTMENT, RETURN), " +
                      "the quantity changed, the stock level before and after, the reason, " +
                      "and who performed the action."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log entries returned"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/{productId}/logs")
    public ResponseEntity<List<InventoryLog>> getLogsForProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getLogsForProduct(productId));
    }

    // ── RECENT LOGS ───────────────────────────────────────────────────────────
    @Operation(
        summary = "Get recent inventory movements",
        description = "Returns the 50 most recent inventory log entries across all products, ordered by most recent first. " +
                      "Useful for an audit dashboard or activity feed."
    )
    @ApiResponse(responseCode = "200", description = "Recent log entries returned")
    @GetMapping("/logs")
    public ResponseEntity<List<InventoryLog>> getRecentLogs() {
        return ResponseEntity.ok(inventoryService.getRecentLogs());
    }
}