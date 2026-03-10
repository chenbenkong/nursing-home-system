-- 添加入住申请表的房间床位相关字段

USE nursing_home;

-- 添加 room_id 字段
ALTER TABLE check_in_application 
ADD COLUMN room_id BIGINT COMMENT '分配的房间ID' AFTER approve_remark;

-- 添加 bed_id 字段
ALTER TABLE check_in_application 
ADD COLUMN bed_id VARCHAR(10) COMMENT '分配的床位号' AFTER room_id;

-- 添加 check_in_date 字段
ALTER TABLE check_in_application 
ADD COLUMN check_in_date DATE COMMENT '实际入住日期' AFTER bed_id;
