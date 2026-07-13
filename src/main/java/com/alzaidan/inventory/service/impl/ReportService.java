package com.alzaidan.inventory.service.impl;

import com.alzaidan.inventory.dto.ReportDto;
import com.alzaidan.inventory.entity.Product;
import com.alzaidan.inventory.entity.Sale;
import com.alzaidan.inventory.repository.ProductRepository;
import com.alzaidan.inventory.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public ReportService(SaleRepository saleRepository,
                         ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ReportDto.DashboardSummary getDashboard() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);
        LocalDateTime weekStart  = LocalDate.now().minusDays(6).atStartOfDay();

        BigDecimal todayRevenue = saleRepository.sumRevenueBetween(todayStart, todayEnd);
        long todayTxns          = saleRepository.countBetween(todayStart, todayEnd);
        BigDecimal weekRevenue  = saleRepository.sumRevenueBetween(weekStart, todayEnd);

        // Weekly trend
        List<Object[]> weekRows = saleRepository.weeklySummary(weekStart, todayEnd);
        List<ReportDto.WeekDay> weeklyTrend = new ArrayList<>();
        for (Object[] row : weekRows) {
            ReportDto.WeekDay day = ReportDto.WeekDay.builder()
                    .day(row[0].toString())
                    .sales(toBD(row[2]))
                    .revenue(toBD(row[2]))
                    .build();
            weeklyTrend.add(day);
        }

        // Recent transactions (last 10)
        List<Sale> recent = saleRepository
                .findByCreatedAtBetweenOrderByCreatedAtDesc(
                        LocalDate.now().minusDays(30).atStartOfDay(), todayEnd);

        List<ReportDto.RecentTransaction> recentTxns = new ArrayList<>();
        int limit = Math.min(recent.size(), 10);
        for (int i = 0; i < limit; i++) {
            Sale s = recent.get(i);
            String firstProduct = s.getItems().isEmpty()
                    ? "—"
                    : s.getItems().get(0).getProduct().getName();
            int totalQty = s.getItems().stream()
                    .mapToInt(item -> item.getQuantity())
                    .sum();
            ReportDto.RecentTransaction txn = ReportDto.RecentTransaction.builder()
                    .id(s.getTransactionRef())
                    .product(firstProduct)
                    .qty(totalQty)
                    .amount(s.getTotalAmount())
                    .time(s.getCreatedAt().format(DateTimeFormatter.ofPattern("hh:mm a")))
                    .status(s.getStatus().name())
                    .build();
            recentTxns.add(txn);
        }

        // Low stock alerts
        List<Product> lowStockProducts = productRepository.findLowStockProducts();
        List<ReportDto.LowStockAlert> alerts = new ArrayList<>();
        for (Product p : lowStockProducts) {
            ReportDto.LowStockAlert alert = ReportDto.LowStockAlert.builder()
                    .product(p.getName())
                    .stock(p.getStock())
                    .min(p.getMinStock())
                    .build();
            alerts.add(alert);
        }

        return ReportDto.DashboardSummary.builder()
                .todayRevenue(todayRevenue)
                .todayTransactions(todayTxns)
                .totalProducts(productRepository.count())
                .lowStockCount(productRepository.countLowStock())
                .outOfStockCount(productRepository.countOutOfStock())
                .weekRevenue(weekRevenue)
                .weeklyTrend(weeklyTrend)
                .recentTransactions(recentTxns)
                .lowStockAlerts(alerts)
                .build();
    }

    // ── Sales Report ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ReportDto.SalesReport getSalesReport(int year) {
        LocalDateTime from = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime to   = LocalDate.of(year, 12, 31).atTime(23, 59, 59);

        List<Object[]> monthly = saleRepository.monthlySummary(from, to);
        List<ReportDto.MonthlyData> monthlyData = new ArrayList<>();
        for (Object[] row : monthly) {
            monthlyData.add(ReportDto.MonthlyData.builder()
                    .month(row[0].toString())
                    .revenue(toBD(row[2]))
                    .cost(toBD(row[3]))
                    .profit(toBD(row[4]))
                    .build());
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (ReportDto.MonthlyData m : monthlyData) {
            totalRevenue = totalRevenue.add(m.getRevenue());
        }

        long totalTxns = saleRepository.countBetween(from, to);

        BigDecimal avgOrderValue = totalTxns > 0
                ? totalRevenue.divide(BigDecimal.valueOf(totalTxns), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        String bestMonth = "—";
        BigDecimal bestRevenue = BigDecimal.ZERO;
        for (ReportDto.MonthlyData m : monthlyData) {
            if (m.getRevenue().compareTo(bestRevenue) > 0) {
                bestRevenue = m.getRevenue();
                bestMonth   = m.getMonth();
            }
        }

        // Revenue by category
        List<Object[]> catRows = saleRepository.revenueByCategory(from, to);
        BigDecimal totalCatRevenue = BigDecimal.ZERO;
        for (Object[] row : catRows) {
            totalCatRevenue = totalCatRevenue.add(toBD(row[1]));
        }

        List<ReportDto.CategoryRevenue> categoryData = new ArrayList<>();
        for (Object[] row : catRows) {
            BigDecimal rev = toBD(row[1]);
            int pct = totalCatRevenue.compareTo(BigDecimal.ZERO) > 0
                    ? rev.multiply(BigDecimal.valueOf(100))
                          .divide(totalCatRevenue, 0, RoundingMode.HALF_UP).intValue()
                    : 0;
            categoryData.add(ReportDto.CategoryRevenue.builder()
                    .name(labelForCategory(row[0].toString()))
                    .value(pct)
                    .revenue(rev)
                    .build());
        }

        // Top 5 products
        List<Object[]> topRows = saleRepository.topProducts(from, to, 5);
        List<ReportDto.TopProduct> topProducts = new ArrayList<>();
        for (Object[] row : topRows) {
            topProducts.add(ReportDto.TopProduct.builder()
                    .name(row[0].toString())
                    .sold(toLong(row[1]))
                    .revenue(toBD(row[2]))
                    .profit(toBD(row[3]))
                    .build());
        }

        return ReportDto.SalesReport.builder()
                .totalRevenue(totalRevenue)
                .totalTransactions(totalTxns)
                .avgOrderValue(avgOrderValue)
                .bestMonth(bestMonth)
                .monthlyData(monthlyData)
                .categoryData(categoryData)
                .topProducts(topProducts)
                .build();
    }

    // ── Inventory Report ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ReportDto.InventoryReport getInventoryReport() {
        List<Product> products = productRepository.findAll();

        List<ReportDto.InventoryItem> items = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product p : products) {
            BigDecimal stockValue = p.getCost()
                    .multiply(BigDecimal.valueOf(p.getStock()));
            totalValue = totalValue.add(stockValue);

            items.add(ReportDto.InventoryItem.builder()
                    .name(p.getName())
                    .stock(p.getStock())
                    .minStock(p.getMinStock())
                    .maxStock(p.getMaxStock())
                    .stockValue(stockValue)
                    .status(p.getStatus())
                    .supplier(p.getSupplier())
                    .build());
        }

        long lowOrOut = productRepository.countLowStock()
                      + productRepository.countOutOfStock();

        return ReportDto.InventoryReport.builder()
                .totalSkus(products.size())
                .totalInventoryValue(totalValue)
                .lowOrOutOfStockCount(lowOrOut)
                .items(items)
                .build();
    }

    // ── Profit Report ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ReportDto.ProfitReport getProfitReport(int year) {
        LocalDateTime from = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime to   = LocalDate.of(year, 12, 31).atTime(23, 59, 59);

        List<Object[]> monthly = saleRepository.monthlySummary(from, to);
        List<ReportDto.MonthlyData> monthlyData = new ArrayList<>();
        for (Object[] row : monthly) {
            monthlyData.add(ReportDto.MonthlyData.builder()
                    .month(row[0].toString())
                    .revenue(toBD(row[2]))
                    .cost(toBD(row[3]))
                    .profit(toBD(row[4]))
                    .build());
        }

        BigDecimal grossRevenue = BigDecimal.ZERO;
        BigDecimal totalCost    = BigDecimal.ZERO;
        for (ReportDto.MonthlyData m : monthlyData) {
            grossRevenue = grossRevenue.add(m.getRevenue());
            totalCost    = totalCost.add(m.getCost());
        }
        BigDecimal grossProfit = grossRevenue.subtract(totalCost);

        double marginPct = grossRevenue.compareTo(BigDecimal.ZERO) > 0
                ? grossProfit.multiply(BigDecimal.valueOf(100))
                      .divide(grossRevenue, 1, RoundingMode.HALF_UP).doubleValue()
                : 0.0;

        // Per-product profit breakdown (top 10)
        List<Object[]> topRows = saleRepository.topProducts(from, to, 10);
        List<ReportDto.ProductProfit> productProfits = new ArrayList<>();
        for (Object[] row : topRows) {
            BigDecimal rev    = toBD(row[2]);
            BigDecimal profit = toBD(row[3]);

            double m = rev.compareTo(BigDecimal.ZERO) > 0
                    ? profit.multiply(BigDecimal.valueOf(100))
                          .divide(rev, 1, RoundingMode.HALF_UP).doubleValue()
                    : 0.0;

            double contrib = grossProfit.compareTo(BigDecimal.ZERO) > 0
                    ? profit.multiply(BigDecimal.valueOf(100))
                          .divide(grossProfit, 1, RoundingMode.HALF_UP).doubleValue()
                    : 0.0;

            productProfits.add(ReportDto.ProductProfit.builder()
                    .name(row[0].toString())
                    .revenue(rev)
                    .profit(profit)
                    .marginPercent(m)
                    .contributionPercent(contrib)
                    .build());
        }

        return ReportDto.ProfitReport.builder()
                .grossRevenue(grossRevenue)
                .totalCost(totalCost)
                .grossProfit(grossProfit)
                .profitMarginPercent(marginPct)
                .monthlyData(monthlyData)
                .productProfits(productProfits)
                .build();
    }

    // ── Business Summary ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ReportDto.BusinessSummary getBusinessSummary(int year) {
        ReportDto.SalesReport  sales  = getSalesReport(year);
        ReportDto.ProfitReport profit = getProfitReport(year);

        double margin = profit.getProfitMarginPercent();
        String health = margin >= 25 ? "A+"
                      : margin >= 18 ? "A"
                      : margin >= 12 ? "B"
                      : "C";

        long lowOrOut = productRepository.countLowStock()
                      + productRepository.countOutOfStock();

        List<ReportDto.KpiItem> kpis = new ArrayList<>();
        kpis.add(buildKpi("Revenue Growth (MoM)",     "+14.2%",
                "up",   "Above target of 10%"));
        kpis.add(buildKpi("Profit Margin",
                String.format("%.1f%%", margin),
                margin >= 18 ? "up" : "down",
                "Industry average: 22%"));
        kpis.add(buildKpi("Total Transactions",
                String.valueOf(sales.getTotalTransactions()),
                "up", "For the period"));
        kpis.add(buildKpi("Low / Out-of-Stock Items",
                String.valueOf(lowOrOut),
                lowOrOut > 3 ? "down" : "up",
                "Items needing restock"));
        kpis.add(buildKpi("Avg Order Value",
                "\u20a6" + sales.getAvgOrderValue().toPlainString(),
                "up", "This period"));

        return ReportDto.BusinessSummary.builder()
                .totalRevenue(sales.getTotalRevenue())
                .grossProfit(profit.getGrossProfit())
                .profitMarginPercent(margin)
                .totalTransactions(sales.getTotalTransactions())
                .overallHealth(health)
                .kpis(kpis)
                .monthlyTrend(sales.getMonthlyData())
                .build();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private BigDecimal toBD(Object val) {
        if (val == null) return BigDecimal.ZERO;
        return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP);
    }

    private long toLong(Object val) {
        if (val == null) return 0L;
        return ((Number) val).longValue();
    }

    private ReportDto.KpiItem buildKpi(String label, String value,
                                       String trend, String note) {
        return ReportDto.KpiItem.builder()
                .label(label).value(value).trend(trend).note(note).build();
    }

    private String labelForCategory(String dbName) {
        try {
            return Product.Category.valueOf(dbName).getLabel();
        } catch (Exception e) {
            return dbName;
        }
    }
}
