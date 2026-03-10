package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 床位信息实体类
 */
@Data
public class Bed {
    
    private Long id;
    private Long roomId;
    private String bedNumber;
    private String status;
    private Long elderId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 非数据库字段
    private String elderName;
    private String roomNumber;
    
    // 状态常量
    public static final String STATUS_AVAILABLE = "AVAILABLE";      // 空闲
    public static final String STATUS_OCCUPIED = "OCCUPIED";        // 已占用
    public static final String STATUS_MAINTENANCE = "MAINTENANCE";  // 维修中
}
