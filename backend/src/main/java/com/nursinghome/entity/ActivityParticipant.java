package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityParticipant {

    private Long id;
    private Long activityId;
    private Long elderId;
    private String elderName;
    private LocalDateTime signUpTime;
    private LocalDateTime signInTime;
    private String status;
    private String feedback;
    private Integer satisfaction;
    private LocalDateTime createTime;

    // 数据库 ENUM: SIGNED_UP, ATTENDED, ABSENT, CANCELLED
    public static final String STATUS_SIGNED_UP = "SIGNED_UP";
    public static final String STATUS_ATTENDED = "ATTENDED";
    public static final String STATUS_ABSENT = "ABSENT";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    // 兼容旧代码
    public static final String STATUS_REGISTERED = "SIGNED_UP";
    public static final String STATUS_CHECKED_IN = "ATTENDED";
    public static final String STATUS_CHECKED_OUT = "ATTENDED";
}
