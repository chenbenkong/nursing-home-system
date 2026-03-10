-- 更新房间表，添加 MULTI 类型
ALTER TABLE room MODIFY COLUMN room_type ENUM('SINGLE', 'DOUBLE', 'TRIPLE', 'MULTI', 'SUITE') NOT NULL COMMENT '房间类型';

-- 更新入住申请表，添加 MULTI 类型
ALTER TABLE check_in_application MODIFY COLUMN room_type ENUM('SINGLE', 'DOUBLE', 'TRIPLE', 'MULTI', 'SUITE') COMMENT '期望房型';
