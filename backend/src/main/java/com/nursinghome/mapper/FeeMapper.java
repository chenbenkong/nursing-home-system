package com.nursinghome.mapper;

import com.nursinghome.entity.Fee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用记录Mapper接口
 */
@Mapper
public interface FeeMapper {
    
    /**
     * 根据ID查询费用记录
     */
    Fee selectById(Long id);
    
    /**
     * 查询所有费用记录
     */
    List<Fee> selectAll();
    
    /**
     * 分页查询费用记录
     */
    List<Fee> selectByPage(@Param("elderId") Long elderId,
                           @Param("feeType") String feeType,
                           @Param("status") String status,
                           @Param("feeMonth") String feeMonth);
    
    /**
     * 根据老人ID查询费用记录
     */
    List<Fee> selectByElderId(Long elderId);
    
    /**
     * 插入费用记录
     */
    int insert(Fee fee);
    
    /**
     * 更新费用记录
     */
    int update(Fee fee);
    
    /**
     * 删除费用记录
     */
    int deleteById(Long id);
    
    /**
     * 缴费
     */
    int payFee(@Param("id") Long id,
               @Param("payAmount") BigDecimal payAmount,
               @Param("payMethod") String payMethod);
    
    /**
     * 统计费用总额
     */
    BigDecimal sumAmount(@Param("feeMonth") String feeMonth,
                         @Param("status") String status);
    
    /**
     * 统计已缴费用总额
     */
    BigDecimal sumPaidAmount(@Param("feeMonth") String feeMonth);
    
    /**
     * 统计未缴费用总额
     */
    BigDecimal sumUnpaidAmount(@Param("feeMonth") String feeMonth);

    /**
     * 统计应退总额（退款记录的金额绝对值）
     */
    BigDecimal sumRefundAmount(@Param("feeMonth") String feeMonth);

    /**
     * 统计已退总额
     */
    BigDecimal sumRefundedAmount(@Param("feeMonth") String feeMonth);

    /**
     * 统计未退总额
     */
    BigDecimal sumUnrefundedAmount(@Param("feeMonth") String feeMonth);

    /**
     * 统计欠费老人数量
     */
    Long countOverdueElders(@Param("feeMonth") String feeMonth);

    /**
     * 统计未退款老人数量
     */
    Long countUnrefundedElders(@Param("feeMonth") String feeMonth);
    
    /**
     * 查询本月应收费用
     */
    BigDecimal selectCurrentMonthReceivable();
    
    /**
     * 查询本月已收费用
     */
    BigDecimal selectCurrentMonthReceived();
}
