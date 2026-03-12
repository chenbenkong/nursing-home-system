package com.nursinghome.service;

import com.nursinghome.entity.Fee;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.FeeMapper;
import com.nursinghome.util.CodeGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用管理服务类
 */
@Service
public class FeeService {

    @Autowired
    private FeeMapper feeMapper;

    /**
     * 获取费用记录列表（分页）
     */
    public Result getFeeList(Long elderId, String feeType, String status, String feeMonth, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Fee> list = feeMapper.selectByPage(elderId, feeType, status, feeMonth);
        PageInfo<Fee> pageInfo = new PageInfo<>(list);
        
        PageResult<Fee> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        return Result.success(pageResult);
    }

    /**
     * 根据ID获取费用记录
     */
    public Result getFeeById(Long id) {
        Fee fee = feeMapper.selectById(id);
        if (fee == null) {
            return Result.error("费用记录不存在");
        }
        return Result.success(fee);
    }

    /**
     * 根据老人ID获取费用记录
     */
    public Result getFeesByElderId(Long elderId) {
        List<Fee> list = feeMapper.selectByElderId(elderId);
        return Result.success(list);
    }

    /**
     * 新增费用记录
     */
    @Transactional
    public Result addFee(Fee fee) {
        // 生成记录编号
        fee.setRecordNo(CodeGenerator.generateFeeNo());
        feeMapper.insert(fee);
        return Result.success("添加成功");
    }

    /**
     * 更新费用记录
     */
    @Transactional
    public Result updateFee(Fee fee) {
        // 自动计算缴费状态
        if (fee.getPaidAmount() != null && fee.getAmount() != null) {
            BigDecimal paid = fee.getPaidAmount();
            BigDecimal total = fee.getAmount();
            
            // 判断是退款还是缴费
            if (total.compareTo(BigDecimal.ZERO) < 0) {
                // 退款记录（金额为负数）
                BigDecimal shouldRefund = total.abs();
                if (paid.compareTo(shouldRefund) >= 0) {
                    fee.setStatus(Fee.FeeStatus.REFUNDED);
                } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
                    fee.setStatus(Fee.FeeStatus.PARTIAL_REFUNDED);
                } else {
                    fee.setStatus(Fee.FeeStatus.UNREFUNDED);
                }
            } else {
                // 正常缴费记录
                if (paid.compareTo(total) >= 0) {
                    fee.setStatus(Fee.FeeStatus.PAID);
                } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
                    fee.setStatus(Fee.FeeStatus.PARTIAL);
                } else {
                    fee.setStatus(Fee.FeeStatus.UNPAID);
                }
            }
        }
        
        feeMapper.update(fee);
        return Result.success("更新成功");
    }

    /**
     * 删除费用记录
     */
    @Transactional
    public Result deleteFee(Long id) {
        feeMapper.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 缴费/退款
     */
    @Transactional
    public Result payFee(Long id, BigDecimal payAmount, String payMethod) {
        if (payAmount == null || payAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("金额必须大于0");
        }
        
        // 获取当前费用记录
        Fee fee = feeMapper.selectById(id);
        if (fee == null) {
            return Result.error("费用记录不存在");
        }
        
        // 判断是退款还是缴费
        boolean isRefund = "REFUND".equals(payMethod) || fee.getAmount().compareTo(BigDecimal.ZERO) < 0;
        
        if (isRefund) {
            // 退款逻辑：处理负数金额
            BigDecimal refundAmount = payAmount;
            BigDecimal currentPaid = fee.getPaidAmount() != null ? fee.getPaidAmount() : BigDecimal.ZERO;
            BigDecimal totalShouldRefund = fee.getAmount().abs(); // 应退总额（取绝对值）
            
            // 计算新的已退金额
            BigDecimal newPaidAmount = currentPaid.add(refundAmount);
            
            // 确定退款状态
            Fee.FeeStatus newStatus;
            if (newPaidAmount.compareTo(totalShouldRefund) >= 0) {
                newStatus = Fee.FeeStatus.REFUNDED;
            } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
                newStatus = Fee.FeeStatus.PARTIAL_REFUNDED;
            } else {
                newStatus = Fee.FeeStatus.UNREFUNDED;
            }
            
            // 更新记录
            fee.setPaidAmount(newPaidAmount);
            fee.setStatus(newStatus);
            fee.setPayTime(java.time.LocalDateTime.now());
            fee.setPayMethod(Fee.PayMethod.REFUND);
            feeMapper.update(fee);
            
            return Result.success("退款成功");
        } else {
            // 正常缴费逻辑
            feeMapper.payFee(id, payAmount, payMethod);
            return Result.success("缴费成功");
        }
    }

    /**
     * 获取费用统计
     */
    public Result getFeeStatistics(String feeMonth) {
        Map<String, Object> statistics = new HashMap<>();

        // 应收总额（只统计正数金额，不包括退款）
        BigDecimal totalAmount = feeMapper.sumAmount(feeMonth, null);
        statistics.put("totalAmount", totalAmount);

        // 已收总额（只统计正常费用的已收）
        BigDecimal paidAmount = feeMapper.sumPaidAmount(feeMonth);
        statistics.put("paidAmount", paidAmount);

        // 未收总额（只统计正常费用的未收）
        BigDecimal unpaidAmount = feeMapper.sumUnpaidAmount(feeMonth);
        statistics.put("unpaidAmount", unpaidAmount);

        // 应退总额（退款记录的金额绝对值）
        BigDecimal refundAmount = feeMapper.sumRefundAmount(feeMonth);
        statistics.put("refundAmount", refundAmount);

        // 已退总额
        BigDecimal refundedAmount = feeMapper.sumRefundedAmount(feeMonth);
        statistics.put("refundedAmount", refundedAmount);

        // 未退总额
        BigDecimal unrefundedAmount = feeMapper.sumUnrefundedAmount(feeMonth);
        statistics.put("unrefundedAmount", unrefundedAmount);

        // 欠费老人数
        Long overdueElders = feeMapper.countOverdueElders(feeMonth);
        statistics.put("overdueElders", overdueElders);

        // 未退款老人数
        Long unrefundedElders = feeMapper.countUnrefundedElders(feeMonth);
        statistics.put("unrefundedElders", unrefundedElders);

        return Result.success(statistics);
    }

    /**
     * 获取本月费用统计
     */
    public Result getCurrentMonthStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 本月应收
        BigDecimal receivable = feeMapper.selectCurrentMonthReceivable();
        statistics.put("receivable", receivable);

        // 本月已收
        BigDecimal received = feeMapper.selectCurrentMonthReceived();
        statistics.put("received", received);

        // 本月未收
        statistics.put("unreceived", receivable.subtract(received));

        return Result.success(statistics);
    }

    /**
     * 自动生成住宿费
     * @param elderId 老人ID
     * @param elderName 老人姓名
     * @param roomPrice 房间价格
     * @param sourceId 来源ID（入住申请ID）
     */
    @Transactional
    public void generateAccommodationFee(Long elderId, String elderName, BigDecimal roomPrice, Long sourceId) {
        if (roomPrice == null || roomPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return; // 房间价格无效，不生成费用
        }

        // 获取当前月份
        String feeMonth = java.time.LocalDate.now().toString().substring(0, 7);

        Fee fee = new Fee();
        fee.setRecordNo(CodeGenerator.generateFeeNo());
        fee.setElderId(elderId);
        fee.setElderName(elderName);
        fee.setFeeType(Fee.FeeType.ACCOMMODATION);
        fee.setFeeMonth(feeMonth);
        fee.setAmount(roomPrice);
        fee.setPaidAmount(BigDecimal.ZERO);
        fee.setStatus(Fee.FeeStatus.UNPAID);
        fee.setDueDate(java.time.LocalDate.now().plusDays(5)); // 5天内缴费
        fee.setSourceId(sourceId);
        fee.setSourceType("CHECK_IN_APPLICATION");
        fee.setRemark("入住自动生成住宿费");

        feeMapper.insert(fee);
    }

    /**
     * 自动生成医疗费
     * @param elderId 老人ID
     * @param elderName 老人姓名
     * @param medicinePrice 药品单价
     * @param quantity 数量
     * @param sourceId 来源ID（用药记录ID）
     */
    @Transactional
    public void generateMedicalFee(Long elderId, String elderName, java.math.BigDecimal medicinePrice, Integer quantity, Long sourceId) {
        if (medicinePrice == null || medicinePrice.compareTo(java.math.BigDecimal.ZERO) <= 0 || quantity == null || quantity <= 0) {
            return; // 价格或数量无效，不生成费用
        }

        // 计算总费用
        java.math.BigDecimal totalAmount = medicinePrice.multiply(new java.math.BigDecimal(quantity));

        // 获取当前月份
        String feeMonth = java.time.LocalDate.now().toString().substring(0, 7);

        Fee fee = new Fee();
        fee.setRecordNo(CodeGenerator.generateFeeNo());
        fee.setElderId(elderId);
        fee.setElderName(elderName);
        fee.setFeeType(Fee.FeeType.MEDICAL);
        fee.setFeeMonth(feeMonth);
        fee.setAmount(totalAmount);
        fee.setPaidAmount(java.math.BigDecimal.ZERO);
        fee.setStatus(Fee.FeeStatus.UNPAID);
        fee.setDueDate(java.time.LocalDate.now().plusDays(5));
        fee.setSourceId(sourceId);
        fee.setSourceType("ELDER_MEDICATION");
        fee.setRemark("用药自动生成医疗费，数量：" + quantity);

        feeMapper.insert(fee);
    }

    /**
     * 自动生成物资费
     * @param elderId 老人ID
     * @param elderName 老人姓名
     * @param itemPrice 物资单价
     * @param quantity 数量
     * @param sourceId 来源ID（出库记录ID）
     */
    @Transactional
    public void generateSupplyFee(Long elderId, String elderName, java.math.BigDecimal itemPrice, Integer quantity, Long sourceId) {
        if (itemPrice == null || itemPrice.compareTo(java.math.BigDecimal.ZERO) <= 0 || quantity == null || quantity <= 0) {
            return; // 价格或数量无效，不生成费用
        }

        // 计算总费用
        java.math.BigDecimal totalAmount = itemPrice.multiply(new java.math.BigDecimal(quantity));

        // 获取当前月份
        String feeMonth = java.time.LocalDate.now().toString().substring(0, 7);

        Fee fee = new Fee();
        fee.setRecordNo(CodeGenerator.generateFeeNo());
        fee.setElderId(elderId);
        fee.setElderName(elderName);
        fee.setFeeType(Fee.FeeType.SUPPLY);
        fee.setFeeMonth(feeMonth);
        fee.setAmount(totalAmount);
        fee.setPaidAmount(java.math.BigDecimal.ZERO);
        fee.setStatus(Fee.FeeStatus.UNPAID);
        fee.setDueDate(java.time.LocalDate.now().plusDays(5));
        fee.setSourceId(sourceId);
        fee.setSourceType("INVENTORY_OUT");
        fee.setRemark("物资出库自动生成物资费，数量：" + quantity);

        feeMapper.insert(fee);
    }

    /**
     * 处理换房费用
     * @param elderId 老人ID
     * @param elderName 老人姓名
     * @param oldRoomPrice 旧房间价格
     * @param newRoomPrice 新房间价格
     * @return 返回费用差异信息：正数表示需要补差价，负数表示可以退款，0表示无差异
     */
    @Transactional
    public java.math.BigDecimal handleRoomChangeFee(Long elderId, String elderName, 
                                                     java.math.BigDecimal oldRoomPrice, 
                                                     java.math.BigDecimal newRoomPrice) {
        if (oldRoomPrice == null || newRoomPrice == null) {
            return java.math.BigDecimal.ZERO;
        }

        java.math.BigDecimal diff = newRoomPrice.subtract(oldRoomPrice);
        
        // 获取当前月份
        String feeMonth = java.time.LocalDate.now().toString().substring(0, 7);

        if (diff.compareTo(java.math.BigDecimal.ZERO) > 0) {
            // 新房更贵，生成补差价费用
            Fee fee = new Fee();
            fee.setRecordNo(CodeGenerator.generateFeeNo());
            fee.setElderId(elderId);
            fee.setElderName(elderName);
            fee.setFeeType(Fee.FeeType.ACCOMMODATION);
            fee.setFeeMonth(feeMonth);
            fee.setAmount(diff);
            fee.setPaidAmount(java.math.BigDecimal.ZERO);
            fee.setStatus(Fee.FeeStatus.UNPAID);
            fee.setDueDate(java.time.LocalDate.now().plusDays(5));
            fee.setSourceType("ROOM_CHANGE");
            fee.setRemark("换房补差价，新房比旧房贵" + diff + "元");
            feeMapper.insert(fee);
        } else if (diff.compareTo(java.math.BigDecimal.ZERO) < 0) {
            // 新房更便宜，生成退款记录（负费用）
            Fee fee = new Fee();
            fee.setRecordNo(CodeGenerator.generateFeeNo());
            fee.setElderId(elderId);
            fee.setElderName(elderName);
            fee.setFeeType(Fee.FeeType.ACCOMMODATION);
            fee.setFeeMonth(feeMonth);
            fee.setAmount(diff); // 存储负数金额（表示退款）
            fee.setPaidAmount(java.math.BigDecimal.ZERO); // 已退金额为0（未退款）
            fee.setStatus(Fee.FeeStatus.UNREFUNDED); // 状态为未退款
            fee.setDueDate(java.time.LocalDate.now().plusDays(5));
            fee.setSourceType("ROOM_CHANGE");
            fee.setRemark("换房退款，新房比旧房便宜" + diff.abs() + "元");
            feeMapper.insert(fee);
        }

        return diff;
    }
}
