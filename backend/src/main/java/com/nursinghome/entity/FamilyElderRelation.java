package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 家属账户关联实体类
 */
@Data
public class FamilyElderRelation {
    
    private Long id;
    private Long familyUserId;
    private Long elderId;
    private String relation;
    private Integer isPrimary;
    private Integer canViewHealth;
    private Integer canViewFee;
    private Integer canViewLocation;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联信息
    private String familyUsername;
    private String familyRealName;
    private String familyPhone;
    private String elderName;
    private String elderBedNo;
}
