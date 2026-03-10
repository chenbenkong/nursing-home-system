package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventoryRecord {
    
    private Long id;
    private Long itemId;
    private String itemName;
    private String recordType;
    private Integer quantity;
    private Integer beforeStock;
    private Integer afterStock;
    private String reason;
    private Long relatedId;
    private String relatedName;
    private Long operatorId;
    private String operatorName;
    private LocalDateTime operateTime;
    private String remark;
    
    public static final String RECORD_TYPE_IN = "IN";
    public static final String RECORD_TYPE_OUT = "OUT";
}
