package com.alzaidan.inventory.repository;

import com.alzaidan.inventory.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {

    List<InventoryLog> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<InventoryLog> findTop50ByOrderByCreatedAtDesc();
}
