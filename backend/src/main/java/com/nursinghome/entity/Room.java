package com.nursinghome.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 房间信息实体类（匹配数据库表结构）
 */
@Data
public class Room {
    
    private Long id;
    private String roomNo;          // 房间号
    private String roomType;        // 房间类型
    private Integer floor;          // 楼层
    private BigDecimal area;        // 面积
    private Integer bedCount;       // 床位数
    private BigDecimal price;       // 价格
    private String facilities;      // 设施配置
    private String status;          // 状态
    private Integer occupiedBeds;   // 已占用床位数
    private String remark;          // 备注
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 非数据库字段，用于存储房间入住老人
    private List<Elder> elders;
    
    // 房间类型常量
    public static final String TYPE_SINGLE = "SINGLE";      // 单人间
    public static final String TYPE_DOUBLE = "DOUBLE";      // 双人间
    public static final String TYPE_TRIPLE = "TRIPLE";      // 三人间
    public static final String TYPE_MULTI = "MULTI";        // 多人间
    public static final String TYPE_SUITE = "SUITE";        // 套房
    
    // 状态常量
    public static final String STATUS_AVAILABLE = "AVAILABLE";      // 空闲
    public static final String STATUS_OCCUPIED = "OCCUPIED";        // 已占用
    public static final String STATUS_MAINTENANCE = "MAINTENANCE";  // 维修中
    public static final String STATUS_RESERVED = "RESERVED";        // 预留
}
