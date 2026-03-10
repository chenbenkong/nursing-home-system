package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 老人用药记录实体类
 */
@Data
public class ElderMedication {
    
    private Long id;
    private Long elderId;
    private String elderName;
    private Long medicineId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String administrationTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long prescribedBy;
    private String prescribedByName;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    
    // 频率常量
    public static final String FREQUENCY_ONCE_DAILY = "ONCE_DAILY";
    public static final String FREQUENCY_TWICE_DAILY = "TWICE_DAILY";
    public static final String FREQUENCY_THREE_TIMES_DAILY = "THREE_TIMES_DAILY";
    public static final String FREQUENCY_FOUR_TIMES_DAILY = "FOUR_TIMES_DAILY";
    public static final String FREQUENCY_EVERY_MORNING = "EVERY_MORNING";
    public static final String FREQUENCY_EVERY_EVENING = "EVERY_EVENING";
    public static final String FREQUENCY_BEFORE_BED = "BEFORE_BED";
    public static final String FREQUENCY_AS_NEEDED = "AS_NEEDED";
    
    // 状态常量
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_STOPPED = "STOPPED";
}
