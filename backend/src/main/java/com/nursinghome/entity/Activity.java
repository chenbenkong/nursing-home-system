package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Activity {

    private Long id;
    private String activityNo;
    private String title;
    private String content;
    private String activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Long organizerId;
    private String organizerName;
    private String status;
    private String coverImage;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;

    // 活动类型常量
    public static final String TYPE_ENTERTAINMENT = "ENTERTAINMENT";
    public static final String TYPE_SPORTS = "SPORTS";
    public static final String TYPE_CULTURAL = "CULTURAL";
    public static final String TYPE_EDUCATIONAL = "EDUCATIONAL";
    public static final String TYPE_SOCIAL = "SOCIAL";
    public static final String TYPE_OTHER = "OTHER";

    // 活动状态常量 - 匹配数据库 ENUM('PLANNING', 'ONGOING', 'COMPLETED', 'CANCELLED')
    public static final String STATUS_PLANNING = "PLANNING";
    public static final String STATUS_ONGOING = "ONGOING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    // 兼容旧代码
    public static final String STATUS_PENDING = "PLANNING";
    public static final String STATUS_DRAFT = "PLANNING";
    public static final String STATUS_PUBLISHED = "PLANNING";
}
