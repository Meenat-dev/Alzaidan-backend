package com.alzaidan.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InventoryDto {

    public static class RestockRequest {
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
        @Size(max = 200)
        private String reason;

        public Integer getQuantity()  { return quantity; }
        public String getReason()     { return reason; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public void setReason(String reason)      { this.reason = reason; }
    }
}
