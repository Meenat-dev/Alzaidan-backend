package com.alzaidan.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReportDto {

    // ── Dashboard Summary ─────────────────────────────────────────────────────
    public static class DashboardSummary {
        private BigDecimal todayRevenue;
        private long todayTransactions;
        private long totalProducts;
        private long lowStockCount;
        private long outOfStockCount;
        private BigDecimal weekRevenue;
        private List<WeekDay> weeklyTrend;
        private List<RecentTransaction> recentTransactions;
        private List<LowStockAlert> lowStockAlerts;

        public BigDecimal getTodayRevenue()                     { return todayRevenue; }
        public long getTodayTransactions()                      { return todayTransactions; }
        public long getTotalProducts()                          { return totalProducts; }
        public long getLowStockCount()                          { return lowStockCount; }
        public long getOutOfStockCount()                        { return outOfStockCount; }
        public BigDecimal getWeekRevenue()                      { return weekRevenue; }
        public List<WeekDay> getWeeklyTrend()                   { return weeklyTrend; }
        public List<RecentTransaction> getRecentTransactions()  { return recentTransactions; }
        public List<LowStockAlert> getLowStockAlerts()          { return lowStockAlerts; }

        public void setTodayRevenue(BigDecimal v)                       { this.todayRevenue = v; }
        public void setTodayTransactions(long v)                        { this.todayTransactions = v; }
        public void setTotalProducts(long v)                            { this.totalProducts = v; }
        public void setLowStockCount(long v)                            { this.lowStockCount = v; }
        public void setOutOfStockCount(long v)                          { this.outOfStockCount = v; }
        public void setWeekRevenue(BigDecimal v)                        { this.weekRevenue = v; }
        public void setWeeklyTrend(List<WeekDay> v)                     { this.weeklyTrend = v; }
        public void setRecentTransactions(List<RecentTransaction> v)    { this.recentTransactions = v; }
        public void setLowStockAlerts(List<LowStockAlert> v)            { this.lowStockAlerts = v; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private BigDecimal todayRevenue; private long todayTransactions;
            private long totalProducts; private long lowStockCount; private long outOfStockCount;
            private BigDecimal weekRevenue; private List<WeekDay> weeklyTrend;
            private List<RecentTransaction> recentTransactions; private List<LowStockAlert> lowStockAlerts;

            public Builder todayRevenue(BigDecimal v)                       { this.todayRevenue = v; return this; }
            public Builder todayTransactions(long v)                        { this.todayTransactions = v; return this; }
            public Builder totalProducts(long v)                            { this.totalProducts = v; return this; }
            public Builder lowStockCount(long v)                            { this.lowStockCount = v; return this; }
            public Builder outOfStockCount(long v)                          { this.outOfStockCount = v; return this; }
            public Builder weekRevenue(BigDecimal v)                        { this.weekRevenue = v; return this; }
            public Builder weeklyTrend(List<WeekDay> v)                     { this.weeklyTrend = v; return this; }
            public Builder recentTransactions(List<RecentTransaction> v)    { this.recentTransactions = v; return this; }
            public Builder lowStockAlerts(List<LowStockAlert> v)            { this.lowStockAlerts = v; return this; }

            public DashboardSummary build() {
                DashboardSummary d = new DashboardSummary();
                d.todayRevenue = todayRevenue; d.todayTransactions = todayTransactions;
                d.totalProducts = totalProducts; d.lowStockCount = lowStockCount;
                d.outOfStockCount = outOfStockCount; d.weekRevenue = weekRevenue;
                d.weeklyTrend = weeklyTrend; d.recentTransactions = recentTransactions;
                d.lowStockAlerts = lowStockAlerts;
                return d;
            }
        }
    }

    // ── WeekDay ───────────────────────────────────────────────────────────────
    public static class WeekDay {
        private String day; private BigDecimal sales; private BigDecimal revenue;
        public String getDay()           { return day; }
        public BigDecimal getSales()     { return sales; }
        public BigDecimal getRevenue()   { return revenue; }
        public void setDay(String day)           { this.day = day; }
        public void setSales(BigDecimal sales)   { this.sales = sales; }
        public void setRevenue(BigDecimal rev)   { this.revenue = rev; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String day; private BigDecimal sales; private BigDecimal revenue;
            public Builder day(String day)           { this.day = day; return this; }
            public Builder sales(BigDecimal sales)   { this.sales = sales; return this; }
            public Builder revenue(BigDecimal rev)   { this.revenue = rev; return this; }
            public WeekDay build() {
                WeekDay w = new WeekDay(); w.day = day; w.sales = sales; w.revenue = revenue; return w;
            }
        }
    }

    // ── RecentTransaction ─────────────────────────────────────────────────────
    public static class RecentTransaction {
        private String id; private String product; private Integer qty;
        private BigDecimal amount; private String time; private String status;
        public String getId()            { return id; }
        public String getProduct()       { return product; }
        public Integer getQty()          { return qty; }
        public BigDecimal getAmount()    { return amount; }
        public String getTime()          { return time; }
        public String getStatus()        { return status; }
        public void setId(String id)             { this.id = id; }
        public void setProduct(String product)   { this.product = product; }
        public void setQty(Integer qty)          { this.qty = qty; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public void setTime(String time)         { this.time = time; }
        public void setStatus(String status)     { this.status = status; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String id; private String product; private Integer qty;
            private BigDecimal amount; private String time; private String status;
            public Builder id(String id)             { this.id = id; return this; }
            public Builder product(String product)   { this.product = product; return this; }
            public Builder qty(Integer qty)          { this.qty = qty; return this; }
            public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
            public Builder time(String time)         { this.time = time; return this; }
            public Builder status(String status)     { this.status = status; return this; }
            public RecentTransaction build() {
                RecentTransaction r = new RecentTransaction();
                r.id = id; r.product = product; r.qty = qty;
                r.amount = amount; r.time = time; r.status = status; return r;
            }
        }
    }

    // ── LowStockAlert ─────────────────────────────────────────────────────────
    public static class LowStockAlert {
        private String product; private int stock; private int min;
        public String getProduct()   { return product; }
        public int getStock()        { return stock; }
        public int getMin()          { return min; }
        public void setProduct(String product) { this.product = product; }
        public void setStock(int stock)        { this.stock = stock; }
        public void setMin(int min)            { this.min = min; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String product; private int stock; private int min;
            public Builder product(String product) { this.product = product; return this; }
            public Builder stock(int stock)        { this.stock = stock; return this; }
            public Builder min(int min)            { this.min = min; return this; }
            public LowStockAlert build() {
                LowStockAlert a = new LowStockAlert();
                a.product = product; a.stock = stock; a.min = min; return a;
            }
        }
    }

    // ── MonthlyData ───────────────────────────────────────────────────────────
    public static class MonthlyData {
        private String month; private BigDecimal revenue; private BigDecimal cost; private BigDecimal profit;
        public String getMonth()         { return month; }
        public BigDecimal getRevenue()   { return revenue; }
        public BigDecimal getCost()      { return cost; }
        public BigDecimal getProfit()    { return profit; }
        public void setMonth(String month)           { this.month = month; }
        public void setRevenue(BigDecimal revenue)   { this.revenue = revenue; }
        public void setCost(BigDecimal cost)         { this.cost = cost; }
        public void setProfit(BigDecimal profit)     { this.profit = profit; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String month; private BigDecimal revenue; private BigDecimal cost; private BigDecimal profit;
            public Builder month(String month)           { this.month = month; return this; }
            public Builder revenue(BigDecimal revenue)   { this.revenue = revenue; return this; }
            public Builder cost(BigDecimal cost)         { this.cost = cost; return this; }
            public Builder profit(BigDecimal profit)     { this.profit = profit; return this; }
            public MonthlyData build() {
                MonthlyData m = new MonthlyData();
                m.month = month; m.revenue = revenue; m.cost = cost; m.profit = profit; return m;
            }
        }
    }

    // ── CategoryRevenue ───────────────────────────────────────────────────────
    public static class CategoryRevenue {
        private String name; private int value; private BigDecimal revenue;
        public String getName()         { return name; }
        public int getValue()           { return value; }
        public BigDecimal getRevenue()  { return revenue; }
        public void setName(String name)           { this.name = name; }
        public void setValue(int value)            { this.value = value; }
        public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String name; private int value; private BigDecimal revenue;
            public Builder name(String name)           { this.name = name; return this; }
            public Builder value(int value)            { this.value = value; return this; }
            public Builder revenue(BigDecimal revenue) { this.revenue = revenue; return this; }
            public CategoryRevenue build() {
                CategoryRevenue c = new CategoryRevenue(); c.name = name; c.value = value; c.revenue = revenue; return c;
            }
        }
    }

    // ── TopProduct ────────────────────────────────────────────────────────────
    public static class TopProduct {
        private String name; private long sold; private BigDecimal revenue; private BigDecimal profit;
        public String getName()         { return name; }
        public long getSold()           { return sold; }
        public BigDecimal getRevenue()  { return revenue; }
        public BigDecimal getProfit()   { return profit; }
        public void setName(String name)           { this.name = name; }
        public void setSold(long sold)             { this.sold = sold; }
        public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
        public void setProfit(BigDecimal profit)   { this.profit = profit; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String name; private long sold; private BigDecimal revenue; private BigDecimal profit;
            public Builder name(String name)           { this.name = name; return this; }
            public Builder sold(long sold)             { this.sold = sold; return this; }
            public Builder revenue(BigDecimal revenue) { this.revenue = revenue; return this; }
            public Builder profit(BigDecimal profit)   { this.profit = profit; return this; }
            public TopProduct build() {
                TopProduct t = new TopProduct(); t.name = name; t.sold = sold; t.revenue = revenue; t.profit = profit; return t;
            }
        }
    }

    // ── SalesReport ───────────────────────────────────────────────────────────
    public static class SalesReport {
        private BigDecimal totalRevenue; private long totalTransactions;
        private BigDecimal avgOrderValue; private String bestMonth;
        private List<MonthlyData> monthlyData; private List<CategoryRevenue> categoryData;
        private List<TopProduct> topProducts;
        public BigDecimal getTotalRevenue()             { return totalRevenue; }
        public long getTotalTransactions()              { return totalTransactions; }
        public BigDecimal getAvgOrderValue()            { return avgOrderValue; }
        public String getBestMonth()                    { return bestMonth; }
        public List<MonthlyData> getMonthlyData()       { return monthlyData; }
        public List<CategoryRevenue> getCategoryData()  { return categoryData; }
        public List<TopProduct> getTopProducts()        { return topProducts; }
        public void setTotalRevenue(BigDecimal v)               { this.totalRevenue = v; }
        public void setTotalTransactions(long v)                { this.totalTransactions = v; }
        public void setAvgOrderValue(BigDecimal v)              { this.avgOrderValue = v; }
        public void setBestMonth(String v)                      { this.bestMonth = v; }
        public void setMonthlyData(List<MonthlyData> v)         { this.monthlyData = v; }
        public void setCategoryData(List<CategoryRevenue> v)    { this.categoryData = v; }
        public void setTopProducts(List<TopProduct> v)          { this.topProducts = v; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private BigDecimal totalRevenue; private long totalTransactions;
            private BigDecimal avgOrderValue; private String bestMonth;
            private List<MonthlyData> monthlyData; private List<CategoryRevenue> categoryData;
            private List<TopProduct> topProducts;
            public Builder totalRevenue(BigDecimal v)               { this.totalRevenue = v; return this; }
            public Builder totalTransactions(long v)                { this.totalTransactions = v; return this; }
            public Builder avgOrderValue(BigDecimal v)              { this.avgOrderValue = v; return this; }
            public Builder bestMonth(String v)                      { this.bestMonth = v; return this; }
            public Builder monthlyData(List<MonthlyData> v)         { this.monthlyData = v; return this; }
            public Builder categoryData(List<CategoryRevenue> v)    { this.categoryData = v; return this; }
            public Builder topProducts(List<TopProduct> v)          { this.topProducts = v; return this; }
            public SalesReport build() {
                SalesReport r = new SalesReport();
                r.totalRevenue = totalRevenue; r.totalTransactions = totalTransactions;
                r.avgOrderValue = avgOrderValue; r.bestMonth = bestMonth;
                r.monthlyData = monthlyData; r.categoryData = categoryData; r.topProducts = topProducts;
                return r;
            }
        }
    }

    // ── InventoryReport ───────────────────────────────────────────────────────
    public static class InventoryReport {
        private long totalSkus; private BigDecimal totalInventoryValue;
        private long lowOrOutOfStockCount; private List<InventoryItem> items;
        public long getTotalSkus()                      { return totalSkus; }
        public BigDecimal getTotalInventoryValue()      { return totalInventoryValue; }
        public long getLowOrOutOfStockCount()           { return lowOrOutOfStockCount; }
        public List<InventoryItem> getItems()           { return items; }
        public void setTotalSkus(long v)                        { this.totalSkus = v; }
        public void setTotalInventoryValue(BigDecimal v)        { this.totalInventoryValue = v; }
        public void setLowOrOutOfStockCount(long v)             { this.lowOrOutOfStockCount = v; }
        public void setItems(List<InventoryItem> v)             { this.items = v; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private long totalSkus; private BigDecimal totalInventoryValue;
            private long lowOrOutOfStockCount; private List<InventoryItem> items;
            public Builder totalSkus(long v)                    { this.totalSkus = v; return this; }
            public Builder totalInventoryValue(BigDecimal v)    { this.totalInventoryValue = v; return this; }
            public Builder lowOrOutOfStockCount(long v)         { this.lowOrOutOfStockCount = v; return this; }
            public Builder items(List<InventoryItem> v)         { this.items = v; return this; }
            public InventoryReport build() {
                InventoryReport r = new InventoryReport();
                r.totalSkus = totalSkus; r.totalInventoryValue = totalInventoryValue;
                r.lowOrOutOfStockCount = lowOrOutOfStockCount; r.items = items; return r;
            }
        }
    }

    // ── InventoryItem ─────────────────────────────────────────────────────────
    public static class InventoryItem {
        private String name; private int stock; private int minStock; private int maxStock;
        private BigDecimal stockValue; private String status; private String supplier;
        public String getName()         { return name; }
        public int getStock()           { return stock; }
        public int getMinStock()        { return minStock; }
        public int getMaxStock()        { return maxStock; }
        public BigDecimal getStockValue(){ return stockValue; }
        public String getStatus()       { return status; }
        public String getSupplier()     { return supplier; }
        public void setName(String name)               { this.name = name; }
        public void setStock(int stock)                { this.stock = stock; }
        public void setMinStock(int minStock)          { this.minStock = minStock; }
        public void setMaxStock(int maxStock)          { this.maxStock = maxStock; }
        public void setStockValue(BigDecimal sv)       { this.stockValue = sv; }
        public void setStatus(String status)           { this.status = status; }
        public void setSupplier(String supplier)       { this.supplier = supplier; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String name; private int stock; private int minStock; private int maxStock;
            private BigDecimal stockValue; private String status; private String supplier;
            public Builder name(String name)               { this.name = name; return this; }
            public Builder stock(int stock)                { this.stock = stock; return this; }
            public Builder minStock(int minStock)          { this.minStock = minStock; return this; }
            public Builder maxStock(int maxStock)          { this.maxStock = maxStock; return this; }
            public Builder stockValue(BigDecimal sv)       { this.stockValue = sv; return this; }
            public Builder status(String status)           { this.status = status; return this; }
            public Builder supplier(String supplier)       { this.supplier = supplier; return this; }
            public InventoryItem build() {
                InventoryItem i = new InventoryItem();
                i.name = name; i.stock = stock; i.minStock = minStock; i.maxStock = maxStock;
                i.stockValue = stockValue; i.status = status; i.supplier = supplier; return i;
            }
        }
    }

    // ── ProfitReport ──────────────────────────────────────────────────────────
    public static class ProfitReport {
        private BigDecimal grossRevenue; private BigDecimal totalCost;
        private BigDecimal grossProfit; private double profitMarginPercent;
        private List<MonthlyData> monthlyData; private List<ProductProfit> productProfits;
        public BigDecimal getGrossRevenue()             { return grossRevenue; }
        public BigDecimal getTotalCost()                { return totalCost; }
        public BigDecimal getGrossProfit()              { return grossProfit; }
        public double getProfitMarginPercent()          { return profitMarginPercent; }
        public List<MonthlyData> getMonthlyData()       { return monthlyData; }
        public List<ProductProfit> getProductProfits()  { return productProfits; }
        public void setGrossRevenue(BigDecimal v)               { this.grossRevenue = v; }
        public void setTotalCost(BigDecimal v)                  { this.totalCost = v; }
        public void setGrossProfit(BigDecimal v)                { this.grossProfit = v; }
        public void setProfitMarginPercent(double v)            { this.profitMarginPercent = v; }
        public void setMonthlyData(List<MonthlyData> v)         { this.monthlyData = v; }
        public void setProductProfits(List<ProductProfit> v)    { this.productProfits = v; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private BigDecimal grossRevenue; private BigDecimal totalCost;
            private BigDecimal grossProfit; private double profitMarginPercent;
            private List<MonthlyData> monthlyData; private List<ProductProfit> productProfits;
            public Builder grossRevenue(BigDecimal v)               { this.grossRevenue = v; return this; }
            public Builder totalCost(BigDecimal v)                  { this.totalCost = v; return this; }
            public Builder grossProfit(BigDecimal v)                { this.grossProfit = v; return this; }
            public Builder profitMarginPercent(double v)            { this.profitMarginPercent = v; return this; }
            public Builder monthlyData(List<MonthlyData> v)         { this.monthlyData = v; return this; }
            public Builder productProfits(List<ProductProfit> v)    { this.productProfits = v; return this; }
            public ProfitReport build() {
                ProfitReport r = new ProfitReport();
                r.grossRevenue = grossRevenue; r.totalCost = totalCost; r.grossProfit = grossProfit;
                r.profitMarginPercent = profitMarginPercent; r.monthlyData = monthlyData;
                r.productProfits = productProfits; return r;
            }
        }
    }

    // ── ProductProfit ─────────────────────────────────────────────────────────
    public static class ProductProfit {
        private String name; private BigDecimal revenue; private BigDecimal profit;
        private double marginPercent; private double contributionPercent;
        public String getName()                 { return name; }
        public BigDecimal getRevenue()          { return revenue; }
        public BigDecimal getProfit()           { return profit; }
        public double getMarginPercent()        { return marginPercent; }
        public double getContributionPercent()  { return contributionPercent; }
        public void setName(String name)                        { this.name = name; }
        public void setRevenue(BigDecimal revenue)              { this.revenue = revenue; }
        public void setProfit(BigDecimal profit)                { this.profit = profit; }
        public void setMarginPercent(double marginPercent)      { this.marginPercent = marginPercent; }
        public void setContributionPercent(double v)            { this.contributionPercent = v; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String name; private BigDecimal revenue; private BigDecimal profit;
            private double marginPercent; private double contributionPercent;
            public Builder name(String name)                        { this.name = name; return this; }
            public Builder revenue(BigDecimal revenue)              { this.revenue = revenue; return this; }
            public Builder profit(BigDecimal profit)                { this.profit = profit; return this; }
            public Builder marginPercent(double v)                  { this.marginPercent = v; return this; }
            public Builder contributionPercent(double v)            { this.contributionPercent = v; return this; }
            public ProductProfit build() {
                ProductProfit p = new ProductProfit();
                p.name = name; p.revenue = revenue; p.profit = profit;
                p.marginPercent = marginPercent; p.contributionPercent = contributionPercent; return p;
            }
        }
    }

    // ── BusinessSummary ───────────────────────────────────────────────────────
    public static class BusinessSummary {
        private BigDecimal totalRevenue; private BigDecimal grossProfit;
        private double profitMarginPercent; private long totalTransactions;
        private String overallHealth; private List<KpiItem> kpis; private List<MonthlyData> monthlyTrend;
        public BigDecimal getTotalRevenue()         { return totalRevenue; }
        public BigDecimal getGrossProfit()          { return grossProfit; }
        public double getProfitMarginPercent()      { return profitMarginPercent; }
        public long getTotalTransactions()          { return totalTransactions; }
        public String getOverallHealth()            { return overallHealth; }
        public List<KpiItem> getKpis()              { return kpis; }
        public List<MonthlyData> getMonthlyTrend()  { return monthlyTrend; }
        public void setTotalRevenue(BigDecimal v)           { this.totalRevenue = v; }
        public void setGrossProfit(BigDecimal v)            { this.grossProfit = v; }
        public void setProfitMarginPercent(double v)        { this.profitMarginPercent = v; }
        public void setTotalTransactions(long v)            { this.totalTransactions = v; }
        public void setOverallHealth(String v)              { this.overallHealth = v; }
        public void setKpis(List<KpiItem> v)                { this.kpis = v; }
        public void setMonthlyTrend(List<MonthlyData> v)    { this.monthlyTrend = v; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private BigDecimal totalRevenue; private BigDecimal grossProfit;
            private double profitMarginPercent; private long totalTransactions;
            private String overallHealth; private List<KpiItem> kpis; private List<MonthlyData> monthlyTrend;
            public Builder totalRevenue(BigDecimal v)           { this.totalRevenue = v; return this; }
            public Builder grossProfit(BigDecimal v)            { this.grossProfit = v; return this; }
            public Builder profitMarginPercent(double v)        { this.profitMarginPercent = v; return this; }
            public Builder totalTransactions(long v)            { this.totalTransactions = v; return this; }
            public Builder overallHealth(String v)              { this.overallHealth = v; return this; }
            public Builder kpis(List<KpiItem> v)                { this.kpis = v; return this; }
            public Builder monthlyTrend(List<MonthlyData> v)    { this.monthlyTrend = v; return this; }
            public BusinessSummary build() {
                BusinessSummary s = new BusinessSummary();
                s.totalRevenue = totalRevenue; s.grossProfit = grossProfit;
                s.profitMarginPercent = profitMarginPercent; s.totalTransactions = totalTransactions;
                s.overallHealth = overallHealth; s.kpis = kpis; s.monthlyTrend = monthlyTrend; return s;
            }
        }
    }

    // ── KpiItem ───────────────────────────────────────────────────────────────
    public static class KpiItem {
        private String label; private String value; private String trend; private String note;
        public String getLabel()  { return label; }
        public String getValue()  { return value; }
        public String getTrend()  { return trend; }
        public String getNote()   { return note; }
        public void setLabel(String label) { this.label = label; }
        public void setValue(String value) { this.value = value; }
        public void setTrend(String trend) { this.trend = trend; }
        public void setNote(String note)   { this.note = note; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private String label; private String value; private String trend; private String note;
            public Builder label(String label) { this.label = label; return this; }
            public Builder value(String value) { this.value = value; return this; }
            public Builder trend(String trend) { this.trend = trend; return this; }
            public Builder note(String note)   { this.note = note; return this; }
            public KpiItem build() {
                KpiItem k = new KpiItem(); k.label = label; k.value = value; k.trend = trend; k.note = note; return k;
            }
        }
    }
}
