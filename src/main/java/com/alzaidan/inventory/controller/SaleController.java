package com.alzaidan.inventory.controller;

import com.alzaidan.inventory.dto.SaleDto;
import com.alzaidan.inventory.entity.User;
import com.alzaidan.inventory.service.impl.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Sales Management", description = "Record sales transactions, complete checkouts, and view transaction history")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // ── GET ALL / SEARCH ──────────────────────────────────────────────────────
    @Operation(
        summary = "List or search transactions",
        description = "Returns all recorded sales transactions ordered by most recent first. " +
                      "Optionally pass a search term to filter by transaction reference (e.g. TXN-001) " +
                      "or customer name. Available to all authenticated roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction list returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised — missing or invalid token")
    })
    @GetMapping
    public ResponseEntity<List<SaleDto.Response>> getAll(
            @Parameter(description = "Search term matched against transaction ref or customer name")
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(saleService.getAll(search));
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────
    @Operation(
        summary = "Get transaction by ID",
        description = "Returns a single sale transaction with its full item breakdown, payment details, " +
                      "total amount, amount paid, change due, and status."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction found and returned"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleDto.Response> getById(
            @Parameter(description = "Sale ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(saleService.getById(id));
    }

    // ── CHECKOUT ──────────────────────────────────────────────────────────────
    @Operation(
        summary = "Complete a sale — checkout",
        description = "Records a new sale transaction. The request must include the customer name, " +
                      "payment method (CASH, BANK_TRANSFER, POS, MOBILE_MONEY), optional amount paid (for cash change calculation), " +
                      "and a non-empty list of cart items each containing a productId and quantity.\n\n" +
                      "The endpoint performs the following atomically in a single database transaction:\n" +
                      "1. Validates that all requested products exist.\n" +
                      "2. Verifies sufficient stock is available for each item.\n" +
                      "3. Creates the sale record and all line items.\n" +
                      "4. Deducts the sold quantity from each product's stock.\n" +
                      "5. Records an inventory movement log entry for each item.\n" +
                      "6. Calculates and stores the change due for cash payments.\n\n" +
                      "Returns the complete receipt — transaction ref, itemised list, totals, and change.\n" +
                      "Available to all authenticated roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Sale completed — receipt returned"),
        @ApiResponse(responseCode = "400", description = "Validation error — e.g. empty cart or missing fields"),
        @ApiResponse(responseCode = "404", description = "One or more products not found"),
        @ApiResponse(responseCode = "422", description = "Insufficient stock for one or more items"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @PostMapping("/checkout")
    public ResponseEntity<SaleDto.Response> checkout(
            @Valid @RequestBody SaleDto.CheckoutRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saleService.checkout(request, currentUser));
    }
}