-- ============================================
-- 修复费用表退款相关问题
-- ============================================

USE nursing_home;

-- 1. 修改 fee_record 表的 status 字段，添加退款相关状态
ALTER TABLE fee_record 
MODIFY COLUMN status ENUM('UNPAID', 'PARTIAL', 'PAID', 'OVERDUE', 'UNREFUNDED', 'REFUNDED', 'PARTIAL_REFUNDED') 
DEFAULT 'UNPAID' 
COMMENT '状态：UNPAID-未缴费, PARTIAL-部分缴费, PAID-已缴清, OVERDUE-已逾期, UNREFUNDED-未退款, REFUNDED-已退款, PARTIAL_REFUNDED-部分退款';

-- 2. 修改 fee_record 表的 pay_method 字段，确保包含 REFUND
ALTER TABLE fee_record 
MODIFY COLUMN pay_method ENUM('CASH', 'BANK_TRANSFER', 'ALIPAY', 'WECHAT', 'REFUND', 'OTHER') 
COMMENT '支付方式：CASH-现金, BANK_TRANSFER-银行转账, ALIPAY-支付宝, WECHAT-微信支付, REFUND-退款, OTHER-其他';

-- 3. 更新现有退款记录的状态（金额为负数的记录）
-- 已完全退款的（paid_amount >= ABS(amount)）
UPDATE fee_record 
SET status = 'REFUNDED' 
WHERE amount < 0 
AND paid_amount >= ABS(amount);

-- 部分退款的（0 < paid_amount < ABS(amount)）
UPDATE fee_record 
SET status = 'PARTIAL_REFUNDED' 
WHERE amount < 0 
AND paid_amount > 0 
AND paid_amount < ABS(amount);

-- 未退款的（paid_amount = 0）
UPDATE fee_record 
SET status = 'UNREFUNDED' 
WHERE amount < 0 
AND (paid_amount = 0 OR paid_amount IS NULL);

-- 4. 修复老人姓名为空的记录（通过 elder_id 关联 elder 表）
UPDATE fee_record f
JOIN elder e ON f.elder_id = e.id
SET f.elder_name = e.name
WHERE f.elder_name IS NULL 
OR f.elder_name = '';

-- 5. 查看修复结果
SELECT 
    '修复后的统计' AS description,
    COUNT(*) AS total_records,
    SUM(CASE WHEN amount < 0 THEN 1 ELSE 0 END) AS refund_records,
    SUM(CASE WHEN amount < 0 AND elder_name IS NULL THEN 1 ELSE 0 END) AS refund_without_name,
    SUM(CASE WHEN status IN ('UNREFUNDED', 'REFUNDED', 'PARTIAL_REFUNDED') THEN 1 ELSE 0 END) AS refund_status_count
FROM fee_record;

-- 6. 查看退款记录详情
SELECT 
    id,
    record_no,
    elder_id,
    elder_name,
    fee_type,
    fee_month,
    amount,
    paid_amount,
    status,
    remark
FROM fee_record 
WHERE amount < 0;
