package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmergencyEvent {
    
    private Long id;
    private String eventNo;
    private String eventType;
    private Long elderId;
    private String elderName;
    private LocalDateTime eventTime;
    private String location;
    private String description;
    private String severity;
    private String responseMeasures;
    private Long handlerId;
    private String handlerName;
    private LocalDateTime handleTime;
    private String result;
    private String status;
    private Boolean notifyFamily;
    private LocalDateTime notifyTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public static final String EVENT_TYPE_MEDICAL = "MEDICAL";
    public static final String EVENT_TYPE_FALL = "FALL";
    public static final String EVENT_TYPE_FIRE = "FIRE";
    public static final String EVENT_TYPE_SECURITY = "SECURITY";
    public static final String EVENT_TYPE_NATURAL = "NATURAL";
    public static final String EVENT_TYPE_OTHER = "OTHER";
    
    public static final String SEVERITY_LOW = "LOW";
    public static final String SEVERITY_MEDIUM = "MEDIUM";
    public static final String SEVERITY_HIGH = "HIGH";
    public static final String SEVERITY_CRITICAL = "CRITICAL";
    
    // 数据库 ENUM: PENDING, HANDLING, RESOLVED, CLOSED
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_HANDLING = "HANDLING";
    public static final String STATUS_RESOLVED = "RESOLVED";
    public static final String STATUS_CLOSED = "CLOSED";
    
    // 兼容旧代码
    public static final String STATUS_PROCESSING = "HANDLING";
}
