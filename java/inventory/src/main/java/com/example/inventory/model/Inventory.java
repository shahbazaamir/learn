package com.example.inventory.model;


import java.time.LocalDateTime;

public class Inventory {

    private Long id;
    private String productCode;
    private String productName;
    private int quantity;
    private String warehouseLocation;
    private LocalDateTime lastUpdated;

    // Constructors
    public Inventory() {
    }

    public Inventory(Long id, String productCode, String productName, int quantity, String warehouseLocation, LocalDateTime lastUpdated) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.warehouseLocation = warehouseLocation;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

