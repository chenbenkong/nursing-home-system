package com.nursinghome.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 健康档案实体类
 */
@Data
public class HealthRecord {

    private Long id;
    private Long elderId;
    private String elderName;
    private LocalDate recordDate;
    private String recordType;
    private BigDecimal temperature;
    private Integer bloodPressureHigh;
    private Integer bloodPressureLow;
    private Integer heartRate;
    private Integer respiratoryRate;
    private BigDecimal bloodSugar;
    private BigDecimal weight;
    private BigDecimal height;
    private String chiefComplaint;
    private String presentIllness;
    private String physicalExam;
    private String diagnosis;
    private String treatmentPlan;
    private Long doctorId;
    private String doctorName;
    private String attachmentUrls;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;

    // 记录类型常量
    public static final String RECORD_TYPE_ROUTINE = "ROUTINE";
    public static final String RECORD_TYPE_EMERGENCY = "EMERGENCY";
    public static final String RECORD_TYPE_CHECKUP = "CHECKUP";
    public static final String RECORD_TYPE_FOLLOWUP = "FOLLOWUP";
}
