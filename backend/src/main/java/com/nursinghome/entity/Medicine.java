package com.nursinghome.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 药品信息实体类
 */
@Data
public class Medicine {

    private Long id;
    private String medicineNo;
    private String name;
    private String category;
    private String specification;
    private String manufacturer;
    private String batchNo;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
    
    private Integer stockQuantity;
    private String unit;
    private BigDecimal price;
    private String storageCondition;
    private String usageMethod;
    private String contraindication;
    private String sideEffects;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private String remark;

    // 药品分类常量
    public static final String CATEGORY_CARDIOVASCULAR = "CARDIOVASCULAR";
    public static final String CATEGORY_DIABETES = "DIABETES";
    public static final String CATEGORY_RESPIRATORY = "RESPIRATORY";
    public static final String CATEGORY_DIGESTIVE = "DIGESTIVE";
    public static final String CATEGORY_NERVOUS = "NERVOUS";
    public static final String CATEGORY_ANTIBIOTIC = "ANTIBIOTIC";
    public static final String CATEGORY_VITAMIN = "VITAMIN";
    public static final String CATEGORY_CHINESE = "CHINESE";
    public static final String CATEGORY_WESTERN = "WESTERN";
    public static final String CATEGORY_HEALTH_PRODUCT = "HEALTH_PRODUCT";
    public static final String CATEGORY_OTHER = "OTHER";

    // 状态常量
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DISABLED = "DISABLED";
    public static final String STATUS_EXPIRED = "EXPIRED";
}
