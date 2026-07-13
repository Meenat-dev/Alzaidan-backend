package com.alzaidan.inventory.controller;

import com.alzaidan.inventory.dto.ReportDto;
import com.alzaidan.inventory.service.impl.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reports & Analytics", description = "Dashboard KPIs, sales reports, inventory reports, profit analysis, and business summary")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ── DASHBOARD ─────────────────────────────────────────────────────────────
    @Operation(
        summary = "Dashboard summary",
        description = "Returns real-time KPI data for the Dashboard module:\n" +
                      "- Today's total revenue and transaction count\n" +
                      "- Total product count, low-stock count, out-of-stock count\n" +
                      "- This week's total revenue\n" +
                      "- Day-by-day sales trend for the current week\n" +
                      "- The 10 most recent transactions\n" +
                      "- All products currently below their minimum stock threshold (low-stock alerts)\n\n" +
                      "Available to all authenticated roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dashboard data returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<ReportDto.DashboardSummary> dashboard() {
        return ResponseEntity.ok(reportService.getDashboard());
    }

    // ── SALES REPORT ──────────────────────────────────────────────────────────
    @Operation(
        summary = "Sales report",
        description = "Returns a full sales analysis for the specified year (defaults to the current year):\n" +
                      "- Total revenue, total transaction count, average order value, and best-performing month\n" +
                      "- Month-by-month breakdown of revenue, cost of goods sold, and gross profit\n" +
                      "- Revenue breakdown by product category with percentage share\n" +
                      "- Top 5 best-selling products by revenue with units sold and profit\n\n" +
                      "This data powers the Sales Report tab in the frontend Reports module."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sales report returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/sales")
    public ResponseEntity<ReportDto.SalesReport> sales(
            @Parameter(description = "Year to report on (e.g. 2025). Defaults to current year.")
            @RequestParam(required = false) Integer year) {
        int y = (year != null) ? year : LocalDate.now().getYear();
        return ResponseEntity.ok(reportService.getSalesReport(y));
    }

    // ── INVENTORY REPORT ──────────────────────────────────────────────────────
    @Operation(
        summary = "Inventory report",
        description = "Returns a snapshot of the current inventory position:\n" +
                      "- Total number of SKUs, total inventory value at cost price\n" +
                      "- Combined count of low-stock and out-of-stock items\n" +
                      "- Per-product breakdown: stock quantity, min/max thresholds, stock value, status, and supplier\n\n" +
                      "This data powers the Inventory Report tab in the frontend Reports module."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventory report returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/inventory")
    public ResponseEntity<ReportDto.InventoryReport> inventory() {
        return ResponseEntity.ok(reportService.getInventoryReport());
    }

    // ── PROFIT REPORT ─────────────────────────────────────────────────────────
    @Operation(
        summary = "Profit report",
        description = "Returns a profitability analysis for the specified year (defaults to the current year):\n" +
                      "- Gross revenue, total cost of goods sold, gross profit, and overall profit margin percentage\n" +
                      "- Month-by-month profit trend data for charting\n" +
                      "- Per-product profit breakdown with margin percentage and contribution to total profit\n\n" +
                      "This data powers the Profit Report tab in the frontend Reports module."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profit report returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/profit")
    public ResponseEntity<ReportDto.ProfitReport> profit(
            @Parameter(description = "Year to report on (e.g. 2025). Defaults to current year.")
            @RequestParam(required = false) Integer year) {
        int y = (year != null) ? year : LocalDate.now().getYear();
        return ResponseEntity.ok(reportService.getProfitReport(y));
    }

    // ── BUSINESS SUMMARY ──────────────────────────────────────────────────────
    @Operation(
        summary = "Business summary",
        description = "Returns a high-level business performance summary for the specified year (defaults to current year):\n" +
                      "- Overall revenue, gross profit, profit margin, and transaction count\n" +
                      "- An overall health rating (A+, A, B, C) derived from the profit margin\n" +
                      "- A set of KPI items each with a label, formatted value, trend direction (up/down), and a note\n" +
                      "- Monthly revenue and profit trend data for the summary chart\n\n" +
                      "This data powers the Business Summary tab in the frontend Reports module."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Business summary returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorised")
    })
    @GetMapping("/summary")
    public ResponseEntity<ReportDto.BusinessSummary> summary(
            @Parameter(description = "Year to report on (e.g. 2025). Defaults to current year.")
            @RequestParam(required = false) Integer year) {
        int y = (year != null) ? year : LocalDate.now().getYear();
        return ResponseEntity.ok(reportService.getBusinessSummary(y));
    }
}