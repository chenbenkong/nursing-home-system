package com.nursinghome.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用药执行记录实体类
 */
@Data
public class MedicationRecord {
    
    private Long id;
    private Long elderMedicationId;
    private Long elderId;
    private String elderName;
    private Long medicineId;
    private String medicineName;
    private String dosage;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualTime;
    
    private String status;
    private Long executorId;
    private String executorName;
    private String result;
    private String notes;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    // 状态常量
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_TAKEN = "TAKEN";
    public static final String STATUS_MISSED = "MISSED";
    public static final String STATUS_SKIPPED = "SKIPPED";
}
