package com.alzaidan.inventory.controller;

import com.alzaidan.inventory.dto.SaleDto;
import com.alzaidan.inventory.entity.SaleItem;
import com.alzaidan.inventory.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.alzaidan.inventory.repository.SaleItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sale-items")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Sale Items", description = "Retrieve individual line items belonging to a sale transaction")
public class SaleItemController {

    private final SaleItemRepository saleItemRepository;

    public SaleItemController(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    // ── GET ITEMS BY SALE ID ──────────────────────────────────────────────────
    @Operation(
        summary = "Get all line items for a sale",
        description = "Returns every line item (product, quantity, unit price, and line total) " +
                      "that belongs to the specified sale transaction ID. " +
                      "Useful when the frontend needs to re-render a receipt or drill into a transaction's detail. " +
                      "Available to all authenticated roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Line items returned"),
        @ApiResponse(responseCode = "404", description = "No items found for the given sale ID"),
        @ApiResponse(responseCode = "401", description = "Unauthorised — missing or invalid token")
    })
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<SaleDto.ItemResponse>> getBySaleId(
            @Parameter(description = "Sale (transaction) ID", required = true)
            @PathVariable Long saleId) {

        List<SaleItem> items = saleItemRepository.findBySaleId(saleId);

        if (items.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No sale items found for sale ID: " + saleId);
        }

        List<SaleDto.ItemResponse> response = new ArrayList<>();
        for (SaleItem item : items) {
            response.add(SaleDto.ItemResponse.builder()
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .sku(item.getProduct().getSku())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .lineTotal(item.getLineTotal())
                    .build());
        }

        return ResponseEntity.ok(response);
    }

    // ── GET SINGLE ITEM BY ID ─────────────────────────────────────────────────
    @Operation(
        summary = "Get a single sale line item by ID",
        description = "Returns one specific sale item record by its own database ID. " +
                      "Each sale item holds the product reference, quantity sold, " +
                      "the unit selling price and cost at the time of sale, and the calculated line total. " +
                      "Available to all authenticated roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sale item returned"),
        @ApiResponse(responseCode = "404", description = "Sale item not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorised — missing or invalid token")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleDto.ItemResponse> getById(
            @Parameter(description = "Sale item ID", required = true)
            @PathVariable Long id) {

        SaleItem item = saleItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale item not found with id: " + id));

        SaleDto.ItemResponse response = SaleDto.ItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .sku(item.getProduct().getSku())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();

        return ResponseEntity.ok(response);
    }

    // ── GET ALL SALE ITEMS ────────────────────────────────────────────────────
    @Operation(
        summary = "Get all sale items across all transactions",
        description = "Returns every sale item record in the system across all transactions. " +
                      "Primarily intended for administrative reporting or audit purposes. " +
                      "For a specific transaction's items, use GET /sale-items/sale/{saleId} instead. " +
                      "Available to all authenticated roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "All sale items returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised — missing or invalid token")
    })
    @GetMapping
    public ResponseEntity<List<SaleDto.ItemResponse>> getAll() {

        List<SaleItem> items = saleItemRepository.findAll();

        List<SaleDto.ItemResponse> response = new ArrayList<>();
        for (SaleItem item : items) {
            response.add(SaleDto.ItemResponse.builder()
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .sku(item.getProduct().getSku())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .lineTotal(item.getLineTotal())
                    .build());
        }

        return ResponseEntity.ok(response);
    }
}
