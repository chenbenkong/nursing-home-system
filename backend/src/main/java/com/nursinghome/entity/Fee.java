package com.nursinghome.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 费用记录实体类
 */
@Data
public class Fee {
    
    private Long id;
    
    private String recordNo;
    
    private Long elderId;
    
    private String elderName;
    
    private FeeType feeType;
    
    private String feeMonth;
    
    private BigDecimal amount;
    
    private BigDecimal paidAmount;
    
    private FeeStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
    
    private PayMethod payMethod;
    
    private Long createBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private String remark;
    
    private Long sourceId;
    
    private String sourceType;
    
    /**
     * 费用类型枚举 - 仅包含有数据库支持的类型
     * ACCOMMODATION: 房间表有 price 字段
     * MEDICAL: 药品表有 price 字段
     * SUPPLY: 物资库存表有 price 字段
     * OTHER: 其他临时费用（手动录入）
     */
    public enum FeeType {
        ACCOMMODATION("住宿费"),
        MEDICAL("医疗费"),
        SUPPLY("物资费"),
        OTHER("其他费用");
        
        private final String label;
        
        FeeType(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    /**
     * 费用状态枚举
     */
    public enum FeeStatus {
        UNPAID("未缴费"),
        PARTIAL("部分缴费"),
        PAID("已缴清"),
        OVERDUE("已逾期"),
        UNREFUNDED("未退款"),
        REFUNDED("已退款"),
        PARTIAL_REFUNDED("部分退款");
        
        private final String label;
        
        FeeStatus(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    /**
     * 支付方式枚举
     */
    public enum PayMethod {
        CASH("现金"),
        BANK_TRANSFER("银行转账"),
        ALIPAY("支付宝"),
        WECHAT("微信支付"),
        REFUND("退款"),
        OTHER("其他");
        
        private final String label;
        
        PayMethod(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
}
