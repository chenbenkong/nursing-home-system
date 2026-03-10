-- 为入住申请表添加缺失的字段
ALTER TABLE check_in_application
ADD COLUMN room_id BIGINT COMMENT '分配房间ID' AFTER approve_remark,
ADD COLUMN bed_id BIGINT COMMENT '分配床位ID' AFTER room_id,
ADD COLUMN check_in_date DATE COMMENT '实际入住日期' AFTER bed_id;
