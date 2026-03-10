package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统用户实体类
 */
@Data
public class User {
    
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private String role;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    
    // 角色常量
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_NURSE = "NURSE";
    public static final String ROLE_DOCTOR = "DOCTOR";
    public static final String ROLE_STAFF = "STAFF";
    
    // 状态常量
    public static final Integer STATUS_DISABLED = 0;
    public static final Integer STATUS_ENABLED = 1;
}
