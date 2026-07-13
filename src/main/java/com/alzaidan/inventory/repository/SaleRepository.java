package com.alzaidan.inventory.repository;

import com.alzaidan.inventory.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
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

    @Query("""
           SELECT COALESCE(SUM(s.totalAmount), 0)
           FROM Sale s
           WHERE s.createdAt BETWEEN :start AND :end
           """)
    BigDecimal sumRevenueBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}