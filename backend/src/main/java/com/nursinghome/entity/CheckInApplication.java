package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入住申请实体类
 */
@Data
public class CheckInApplication {
    
    private Long id;
    private String applicationNo;
    private String elderName;
    private String gender;
    private LocalDate birthDate;
    private String idCard;
    private String phone;
    private String address;
    private String contactName;
    private String contactPhone;
    private String contactRelation;
    private String healthStatus;
    private String careLevel;
    private LocalDate expectedDate;
    private String roomType;
    private String status;
    private Long applicantId;
    private String applicantName;
    private String applicantPhone;
    private LocalDateTime applyTime;
    private LocalDateTime approveTime;
    private Long approveBy;
    private String approveRemark;
    private Long roomId;            // 分配的房间ID
    private String bedId;           // 分配的床位号
    private LocalDate checkInDate;  // 入住日期
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 房型常量
    public static final String ROOM_SINGLE = "SINGLE";
    public static final String ROOM_DOUBLE = "DOUBLE";
    public static final String ROOM_TRIPLE = "TRIPLE";
    public static final String ROOM_SUITE = "SUITE";
    
    // 状态常量
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_CANCELLED = "CANCELLED";
}
