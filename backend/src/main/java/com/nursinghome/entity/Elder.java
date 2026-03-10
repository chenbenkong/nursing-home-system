package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 老人信息实体类
 */
@Data
public class Elder {
    
    private Long id;
    private String elderNo;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private Integer age;
    private String idCard;
    private String phone;
    private String address;
    private String contactName;
    private String contactPhone;
    private String contactRelation;
    private String healthStatus;
    private String careLevel;
    private String status;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    private String bedNo;
    private String photo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    
    // 非数据库字段，关联房间信息
    private String roomNumber;
    
    // 性别常量
    public static final String GENDER_MALE = "MALE";
    public static final String GENDER_FEMALE = "FEMALE";
    
    // 健康状况常量
    public static final String HEALTH_HEALTHY = "HEALTHY";
    public static final String HEALTH_SUBHEALTH = "SUBHEALTH";
    public static final String HEALTH_CHRONIC = "CHRONIC";
    public static final String HEALTH_DISABLED = "DISABLED";
    
    // 护理等级常量
    public static final String CARE_LEVEL1 = "LEVEL1";
    public static final String CARE_LEVEL2 = "LEVEL2";
    public static final String CARE_LEVEL3 = "LEVEL3";
    public static final String CARE_LEVEL4 = "LEVEL4";
    
    // 状态常量
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CHECKED_IN = "CHECKED_IN";
    public static final String STATUS_CHECKED_OUT = "CHECKED_OUT";
    public static final String STATUS_SUSPENDED = "SUSPENDED";
}
