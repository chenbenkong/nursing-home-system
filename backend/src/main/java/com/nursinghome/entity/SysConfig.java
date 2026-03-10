package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统配置实体类
 */
@Data
public class SysConfig {
    
    private Long id;
    private String configKey;
    private String configValue;
    private String configType;
    private String description;
    private Integer isSystem;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
