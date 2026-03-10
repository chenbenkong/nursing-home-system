package com.nursinghome.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类
 */
@Data
public class Employee {
    
    private Long id;
    private String empNo;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String idCard;
    private String phone;
    private String email;
    private String address;
    private String department;
    private String position;
    private String jobTitle;
    private String education;
    private LocalDate entryDate;
    private LocalDate leaveDate;
    private String status;
    private BigDecimal salary;
    private String photo;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    
    // 部门常量
    public static final String DEPT_ADMIN = "ADMIN";
    public static final String DEPT_NURSING = "NURSING";
    public static final String DEPT_MEDICAL = "MEDICAL";
    public static final String DEPT_CATERING = "CATERING";
    public static final String DEPT_HOUSEKEEPING = "HOUSEKEEPING";
    public static final String DEPT_SECURITY = "SECURITY";
    public static final String DEPT_OTHER = "OTHER";
    
    // 状态常量
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_LEAVE = "LEAVE";
    public static final String STATUS_RESIGNED = "RESIGNED";
}
