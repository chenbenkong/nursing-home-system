package com.nursinghome.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体检记录实体类
 */
@Data
public class PhysicalExam {

    private Long id;
    private Long elderId;
    private String elderName;
    private LocalDate examDate;
    private String examOrg;
    private String examType;
    private String visionLeft;
    private String visionRight;
    private String hearing;
    private String ecgResult;
    private String bloodRoutine;
    private String urineRoutine;
    private String liverFunction;
    private String kidneyFunction;
    private String lipidProfile;
    private BigDecimal bloodSugarFasting;
    private BigDecimal bloodSugarPostprandial;
    private String ultrasound;
    private String xRay;
    private String ctMri;
    private String overallAssessment;
    private String healthSuggestions;
    private String reportUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 体检类型常量
    public static final String EXAM_TYPE_ROUTINE = "ROUTINE";
    public static final String EXAM_TYPE_COMPREHENSIVE = "COMPREHENSIVE";
    public static final String EXAM_TYPE_SPECIAL = "SPECIAL";

    // 听力常量
    public static final String HEARING_NORMAL = "NORMAL";
    public static final String HEARING_MILD = "MILD";
    public static final String HEARING_MODERATE = "MODERATE";
    public static final String HEARING_SEVERE = "SEVERE";
}
