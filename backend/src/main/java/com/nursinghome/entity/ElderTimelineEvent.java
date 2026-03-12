package com.nursinghome.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 老人时间轴事件实体类
 * 用于展示老人的入住、换房、护理等历史记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElderTimelineEvent {
    
    /**
     * 事件类型枚举
     */
    public enum EventType {
        CHECK_IN("入住", "#67C23A", "CircleCheck"),
        CHECK_OUT("退住", "#F56C6C", "CircleClose"),
        ROOM_CHANGE("换房", "#E6A23C", "Switch"),
        CARE_PLAN("护理计划", "#409EFF", "FirstAidKit"),
        FEE_PAYMENT("缴费", "#909399", "Money"),
        FEE_REFUND("退款", "#FF6B6B", "Refund");
        
        private final String label;
        private final String color;
        private final String icon;
        
        EventType(String label, String color, String icon) {
            this.label = label;
            this.color = color;
            this.icon = icon;
        }
        
        public String getLabel() {
            return label;
        }
        
        public String getColor() {
            return color;
        }
        
        public String getIcon() {
            return icon;
        }
    }
    
    /**
     * 事件ID
     */
    private Long id;
    
    /**
     * 老人ID
     */
    private Long elderId;
    
    /**
     * 事件类型
     */
    private EventType eventType;
    
    /**
     * 事件标题
     */
    private String title;
    
    /**
     * 事件描述
     */
    private String description;
    
    /**
     * 事件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;
    
    /**
     * 关联数据ID（如费用记录ID、护理计划ID等）
     */
    private Long relatedId;
    
    /**
     * 关联数据类型
     */
    private String relatedType;
    
    /**
     * 额外数据（JSON格式，存储扩展信息）
     */
    private String extraData;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
