package com.alzaidan.inventory.repository;

import com.alzaidan.inventory.entity.Sale;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByTransactionRef(String transactionRef);

    @Query("""
           SELECT s
           FROM Sale s
           WHERE LOWER(s.transactionRef) LIKE LOWER(CONCAT('%', :search, '%'))
              OR LOWER(s.customerName) LIKE LOWER(CONCAT('%', :search, '%'))
           ORDER BY s.createdAt DESC
           """)
    List<Sale> search(@Param("search") String search);

    List<Sale> findAllByOrderByCreatedAtDesc();

    List<Sale> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
           SELECT COALESCE(SUM(s.totalAmount),0)
           FROM Sale s
           WHERE s.createdAt BETWEEN :start AND :end
           """)
    BigDecimal sumRevenueBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
           SELECT COUNT(s)
           FROM Sale s
           WHERE s.createdAt BETWEEN :start AND :end
           """)
    Long countBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT
                DATE(created_at),
                COUNT(*),
                COALESCE(SUM(total_amount),0)
            FROM sales
            WHERE created_at BETWEEN :start AND :end
            GROUP BY DATE(created_at)
            ORDER BY DATE(created_at)
            """,
            nativeQuery = true)
    List<Object[]> weeklySummary(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT
                EXTRACT(MONTH FROM s.created_at) month,
                COUNT(*) transactions,
                COALESCE(SUM(s.total_amount),0) revenue,
                0 cost,
                COALESCE(SUM(s.total_amount),0) profit
            FROM sales s
            WHERE s.created_at BETWEEN :start AND :end
            GROUP BY EXTRACT(MONTH FROM s.created_at)
            ORDER BY month
            """,
            nativeQuery = true)
    List<Object[]> monthlySummary(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT
                p.category,
                COALESCE(SUM(si.quantity * si.unit_price),0)
            FROM sale_items si
            JOIN products p ON p.id = si.product_id
            JOIN sales s ON s.id = si.sale_id
            WHERE s.created_at BETWEEN :start AND :end
            GROUP BY p.category
            """,
            nativeQuery = true)
    List<Object[]> revenueByCategory(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT
                p.name,
                SUM(si.quantity) sold,
                SUM(si.quantity * si.unit_price) revenue,
                SUM((si.unit_price - p.cost) * si.quantity) profit
            FROM sale_items si
            JOIN products p ON p.id = si.product_id
            JOIN sales s ON s.id = si.sale_id
            WHERE s.created_at BETWEEN :start AND :end
            GROUP BY p.name
            ORDER BY sold DESC
            """,
            nativeQuery = true)
    List<Object[]> topProducts(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    default List<Object[]> topProducts(
            LocalDateTime start,
            LocalDateTime end,
            int limit) {

        return topProducts(start, end, Pageable.ofSize(limit));
    }
}