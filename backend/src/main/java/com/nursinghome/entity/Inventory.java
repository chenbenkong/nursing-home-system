package com.nursinghome.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Inventory {
    
    private Long id;
    private String itemNo;
    private String itemName;
    private String category;
    private String specification;
    private String unit;
    private Integer stockQuantity;
    private Integer minStock;
    private Integer maxStock;
    private String storageLocation;
    private String supplier;
    private BigDecimal price;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    
    // 前端兼容字段
    public String getName() {
        return itemName;
    }
    
    public void setName(String name) {
        this.itemName = name;
    }
    
    public Integer getStock() {
        return stockQuantity;
    }
    
    public void setStock(Integer stock) {
        this.stockQuantity = stock;
    }
    
    public Integer getStockWarning() {
        return minStock;
    }
    
    public void setStockWarning(Integer stockWarning) {
        this.minStock = stockWarning;
    }
    
    public static final String CATEGORY_MEDICAL = "MEDICAL";
    public static final String CATEGORY_NURSING = "NURSING";
    public static final String CATEGORY_OFFICE = "OFFICE";
    public static final String CATEGORY_FOOD = "FOOD";
    public static final String CATEGORY_DAILY = "DAILY";
    public static final String CATEGORY_OTHER = "OTHER";
    
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DISABLED = "DISABLED";
}
