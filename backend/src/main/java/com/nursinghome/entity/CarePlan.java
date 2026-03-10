package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 护理计划实体类
 */
@Data
public class CarePlan {
    
    private Long id;
    private String planNo;
    private Long elderId;
    private String elderName;
    private String planName;
    private String careLevel;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long nurseId;
    private String nurseName;
    private String dietPlan;
    private String medicationPlan;
    private String dailyCare;
    private String rehabilitationPlan;
    private String specialNotes;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 状态常量
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_PAUSED = "PAUSED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
}
