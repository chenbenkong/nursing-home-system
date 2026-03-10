-- ============================================
-- 养老院管理系统数据库 - 2026年测试数据
-- 配合 nursing_home_schema.sql 使用
-- ============================================

USE nursing_home;

-- ============================================
-- 1. 系统用户表 (sys_user)
-- ============================================
INSERT INTO sys_user (id, username, password, real_name, phone, email, role, status, last_login_time) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '13800138000', 'admin@nursing.com', 'ADMIN', 1, NOW()),
(2, 'manager', 'e10adc3949ba59abbe56e057f20f883e', '王经理', '13800138001', 'manager@nursing.com', 'MANAGER', 1, NOW()),
(3, 'nurse01', 'e10adc3949ba59abbe56e057f20f883e', '张护士', '13800138002', 'nurse01@nursing.com', 'NURSE', 1, NOW()),
(4, 'doctor01', 'e10adc3949ba59abbe56e057f20f883e', '李医生', '13800138003', 'doctor01@nursing.com', 'DOCTOR', 1, NOW()),
(5, 'staff01', 'e10adc3949ba59abbe56e057f20f883e', '赵工', '13800138004', 'staff01@nursing.com', 'STAFF', 1, NOW()),
(6, 'chef01', 'e10adc3949ba59abbe56e057f20f883e', '刘厨师', '13800138005', 'chef01@nursing.com', 'STAFF', 1, NOW()),
(7, 'family01', 'e10adc3949ba59abbe56e057f20f883e', '赵明', '13800138101', 'family01@nursing.com', 'STAFF', 1, NOW()),
(8, 'family02', 'e10adc3949ba59abbe56e057f20f883e', '赵红', '13800138106', 'family02@nursing.com', 'STAFF', 1, NOW()),
(9, 'family03', 'e10adc3949ba59abbe56e057f20f883e', '钱伟', '13800138102', 'family03@nursing.com', 'STAFF', 1, NOW()),
(10, 'family04', 'e10adc3949ba59abbe56e057f20f883e', '孙丽', '13800138103', 'family04@nursing.com', 'STAFF', 1, NOW()),
(11, 'family05', 'e10adc3949ba59abbe56e057f20f883e', '孙强', '13800138107', 'family05@nursing.com', 'STAFF', 1, NOW()),
(12, 'family06', 'e10adc3949ba59abbe56e057f20f883e', '吴芳', '13800138105', 'family06@nursing.com', 'STAFF', 1, NOW());

-- ============================================
-- 2. 房间表 (room)
-- ============================================
INSERT INTO room (id, room_no, room_type, floor, area, bed_count, price, facilities, status, occupied_beds) VALUES
(1, '101', 'SINGLE', 1, 25.00, 1, 3000.00, '空调、电视、独立卫生间', 'OCCUPIED', 1),
(2, '102', 'DOUBLE', 1, 35.00, 2, 2000.00, '空调、电视、独立卫生间', 'OCCUPIED', 2),
(3, '201', 'SINGLE', 2, 25.00, 1, 3000.00, '空调、电视、独立卫生间', 'AVAILABLE', 0),
(4, '202', 'DOUBLE', 2, 35.00, 2, 2000.00, '空调、电视、独立卫生间', 'AVAILABLE', 0),
(5, '301', 'SUITE', 3, 50.00, 2, 5000.00, '空调、电视、独立卫生间、阳台', 'AVAILABLE', 0);

-- ============================================
-- 3. 员工表 (employee)
-- ============================================
INSERT INTO employee (id, emp_no, name, gender, birth_date, id_card, phone, email, department, position, job_title, education, entry_date, salary, status, user_id) VALUES
(1, 'E2026001', '张护士', 'FEMALE', '1990-05-15', '110101199005150011', '13800138002', 'nurse01@nursing.com', 'NURSING', '护士', '护师', '本科', '2026-01-01', 5500.00, 'ACTIVE', 3),
(2, 'E2026002', '李医生', 'MALE', '1985-08-20', '110101198508200022', '13800138003', 'doctor01@nursing.com', 'MEDICAL', '医生', '主治医师', '硕士', '2026-01-01', 12000.00, 'ACTIVE', 4),
(3, 'E2026003', '王管家', 'FEMALE', '1988-03-10', '110101198803100033', '13800138004', 'staff01@nursing.com', 'ADMIN', '行政主管', '中级', '本科', '2026-01-01', 6000.00, 'ACTIVE', 5),
(4, 'E2026004', '刘厨师', 'MALE', '1975-06-20', '110101197506200044', '13800138005', 'chef01@nursing.com', 'CATERING', '厨师', '高级厨师', '中专', '2026-01-01', 5000.00, 'ACTIVE', 6);

-- ============================================
-- 4. 老人信息表 (elder)
-- ============================================
INSERT INTO elder (id, elder_no, name, gender, birth_date, age, id_card, phone, address, contact_name, contact_phone, contact_relation, health_status, care_level, status, check_in_date, room_id, bed_no) VALUES
(1, 'L2026001', '赵大爷', 'MALE', '1945-03-15', 81, '11010119450315001X', '13900139001', '北京市朝阳区', '赵明', '13800138101', '儿子', 'CHRONIC', 'LEVEL2', 'CHECKED_IN', '2026-01-15', 1, 'A床'),
(2, 'L2026002', '钱奶奶', 'FEMALE', '1948-07-20', 78, '110101194807200022', '13900139002', '北京市海淀区', '钱伟', '13800138102', '儿子', 'HEALTHY', 'LEVEL1', 'CHECKED_IN', '2026-02-01', 2, 'A床'),
(3, 'L2026003', '孙爷爷', 'MALE', '1940-11-08', 86, '110101194011080033', '13900139003', '北京市西城区', '孙丽', '13800138103', '女儿', 'DISABLED', 'LEVEL3', 'CHECKED_IN', '2026-01-20', 2, 'B床'),
(4, 'L2026004', '周奶奶', 'FEMALE', '1942-05-20', 84, '110101194205200044', '13900139004', '北京市东城区', '周强', '13800138104', '儿子', 'CHRONIC', 'LEVEL2', 'PENDING', NULL, NULL, NULL),
(5, 'L2026005', '吴爷爷', 'MALE', '1938-09-10', 88, '110101193809100055', '13900139005', '北京市丰台区', '吴芳', '13800138105', '女儿', 'CHRONIC', 'LEVEL3', 'CHECKED_IN', '2026-02-10', 3, 'A床');

-- ============================================
-- 5. 护理计划表 (care_plan)
-- ============================================
INSERT INTO care_plan (id, plan_no, elder_id, plan_name, care_level, start_date, end_date, status, nurse_id, nurse_name, diet_plan, medication_plan, daily_care, rehabilitation_plan, special_notes) VALUES
(1, 'CP2026001', 1, '赵大爷高血压护理计划', 'LEVEL2', '2026-01-15', '2026-07-15', 'ACTIVE', 1, '张护士', '低盐低脂饮食，每日三餐定时定量', '按时服用降压药，每日监测血压', '每日晨间护理，协助洗漱、穿衣', '每日散步30分钟，适度运动', '注意血压变化，避免情绪激动'),
(2, 'CP2026002', 2, '钱奶奶日常护理计划', 'LEVEL1', '2026-02-01', '2026-08-01', 'ACTIVE', 1, '张护士', '均衡营养饮食', '按需服药', '日常起居照料，协助活动', '适度运动，保持活力', '定期体检，预防疾病'),
(3, 'CP2026003', 3, '孙爷爷重度护理计划', 'LEVEL3', '2026-01-20', '2026-07-20', 'ACTIVE', 1, '张护士', '软食易消化，少量多餐', '按时服药，密切观察病情', '全面生活照料，协助翻身、擦洗', '被动运动，防止肌肉萎缩', '密切观察病情变化，预防并发症'),
(4, 'CP2026004', 5, '吴爷爷综合护理计划', 'LEVEL3', '2026-02-10', '2026-08-10', 'ACTIVE', 1, '张护士', '低盐低脂低糖饮食', '按时服药，定期复查', '全面生活照料', '适度康复训练', '密切观察病情');

-- ============================================
-- 6. 护理记录表 (care_record)
-- ============================================
INSERT INTO care_record (id, record_no, elder_id, elder_name, plan_id, record_date, record_time, record_type, content, vital_signs, nurse_id, nurse_name, status) VALUES
(1, 'CR20260001', 1, '赵大爷', 1, '2026-03-07', '08:00:00', 'DAILY_CARE', '晨间护理，协助洗漱、穿衣', '体温36.5°C，血压135/85mmHg', 1, '张护士', 'NORMAL'),
(2, 'CR20260002', 1, '赵大爷', 1, '2026-03-07', '12:00:00', 'MEDICATION', '服用降压药', '血压正常', 1, '张护士', 'NORMAL'),
(3, 'CR20260003', 2, '钱奶奶', 2, '2026-03-07', '08:30:00', 'DAILY_CARE', '晨间护理，血糖监测', '体温36.3°C，血糖6.8mmol/L', 1, '张护士', 'NORMAL'),
(4, 'CR20260004', 3, '孙爷爷', 3, '2026-03-07', '09:00:00', 'DAILY_CARE', '翻身、擦洗，预防褥疮', '体温36.8°C', 1, '张护士', 'NORMAL'),
(5, 'CR20260005', 5, '吴爷爷', 4, '2026-03-07', '08:15:00', 'DAILY_CARE', '晨间护理，协助洗漱', '体温36.4°C，血压140/90mmHg', 1, '张护士', 'NORMAL');

-- ============================================
-- 7. 药品信息表 (medicine)
-- ============================================
INSERT INTO medicine (id, medicine_no, name, category, specification, manufacturer, batch_no, expiry_date, stock_quantity, unit, price, storage_condition, usage_method, status) VALUES
(1, 'M2026001', '硝苯地平缓释片', 'CARDIOVASCULAR', '10mg*30片', '拜耳医药', 'B202601', '2027-12-31', 100, '盒', 28.00, '常温保存', '口服，一次10-20mg，一日2次', 'ACTIVE'),
(2, 'M2026002', '二甲双胍片', 'DIABETES', '0.5g*20片', '中美上海施贵宝', 'B202602', '2027-11-30', 120, '盒', 12.80, '常温保存', '口服，一次0.5g，一日2-3次', 'ACTIVE'),
(3, 'M2026003', '阿司匹林肠溶片', 'CARDIOVASCULAR', '100mg*30片', '拜耳医药', 'B202603', '2027-10-31', 80, '盒', 18.50, '阴凉干燥处', '口服，一日1次，每次1片', 'ACTIVE'),
(4, 'M2026004', '复方丹参片', 'CHINESE', '0.32g*60片', '广州白云山', 'B202604', '2028-03-31', 60, '瓶', 18.00, '密封保存', '口服，一次3片，一日3次', 'ACTIVE'),
(5, 'M2026005', '钙尔奇D', 'VITAMIN', '600mg*30片', '惠氏制药', 'B202605', '2027-06-30', 200, '瓶', 45.00, '常温保存', '口服，一次1片，一日1-2次', 'ACTIVE'),
(6, 'M2026006', '感冒灵颗粒', 'CHINESE', '10g*9袋', '999药业', 'B202606', '2026-12-31', 8, '盒', 12.50, '密封保存', '开水冲服，一日3次，每次1袋', 'ACTIVE'),
(7, 'M2026007', '布洛芬缓释胶囊', 'WESTERN', '0.3g*24粒', '中美史克', 'B202607', '2026-06-30', 5, '盒', 22.00, '常温保存', '口服，一日2次，每次1粒', 'ACTIVE');

-- ============================================
-- 8. 老人用药记录表 (elder_medication)
-- ============================================
INSERT INTO elder_medication (id, elder_id, medicine_id, medicine_name, dosage, frequency, administration_time, start_date, end_date, prescribed_by, prescribed_by_name, status) VALUES
(1, 1, 1, '硝苯地平缓释片', '10mg', '每日2次', '早8点、晚8点', '2026-01-15', NULL, 2, '李医生', 'ACTIVE'),
(2, 1, 3, '阿司匹林肠溶片', '100mg', '每日1次', '早8点', '2026-01-15', NULL, 2, '李医生', 'ACTIVE'),
(3, 2, 2, '二甲双胍片', '0.5g', '每日3次', '早中晚餐后', '2026-02-01', NULL, 2, '李医生', 'ACTIVE'),
(4, 3, 4, '复方丹参片', '3片', '每日3次', '早中晚餐后', '2026-01-20', NULL, 2, '李医生', 'ACTIVE'),
(5, 5, 1, '硝苯地平缓释片', '10mg', '每日2次', '早8点、晚8点', '2026-02-10', NULL, 2, '李医生', 'ACTIVE'),
(6, 5, 5, '钙尔奇D', '1片', '每日1次', '早8点', '2026-02-10', NULL, 2, '李医生', 'ACTIVE');

-- ============================================
-- 9. 用药执行记录表 (medication_record)
-- ============================================
INSERT INTO medication_record (id, elder_medication_id, elder_id, medicine_id, medicine_name, dosage, scheduled_time, actual_time, status, executor_id, executor_name, result) VALUES
(1, 1, 1, 1, '硝苯地平缓释片', '10mg', '2026-03-07 08:00:00', '2026-03-07 08:05:00', 'TAKEN', 1, '张护士', 'SUCCESS'),
(2, 2, 1, 3, '阿司匹林肠溶片', '100mg', '2026-03-07 08:00:00', '2026-03-07 08:05:00', 'TAKEN', 1, '张护士', 'SUCCESS'),
(3, 3, 2, 2, '二甲双胍片', '0.5g', '2026-03-07 12:00:00', '2026-03-07 12:10:00', 'TAKEN', 1, '张护士', 'SUCCESS'),
(4, 4, 3, 4, '复方丹参片', '3片', '2026-03-07 18:00:00', NULL, 'PENDING', NULL, NULL, NULL),
(5, 1, 1, 1, '硝苯地平缓释片', '10mg', '2026-03-07 20:00:00', NULL, 'PENDING', NULL, NULL, NULL),
(6, 5, 5, 1, '硝苯地平缓释片', '10mg', '2026-03-07 08:00:00', '2026-03-07 08:10:00', 'TAKEN', 1, '张护士', 'SUCCESS'),
(7, 6, 5, 5, '钙尔奇D', '1片', '2026-03-07 08:00:00', '2026-03-07 08:10:00', 'TAKEN', 1, '张护士', 'SUCCESS');

-- ============================================
-- 10. 费用记录表 (fee_record)
-- ============================================
INSERT INTO fee_record (id, record_no, elder_id, elder_name, fee_type, fee_month, amount, paid_amount, status, due_date) VALUES
(1, 'F2026001', 1, '赵大爷', 'ACCOMMODATION', '2026-03', 3000.00, 3000.00, 'PAID', '2026-03-05'),
(2, 'F2026002', 1, '赵大爷', 'CARE', '2026-03', 2000.00, 2000.00, 'PAID', '2026-03-05'),
(3, 'F2026003', 2, '钱奶奶', 'ACCOMMODATION', '2026-03', 2000.00, 2000.00, 'PAID', '2026-03-05'),
(4, 'F2026004', 2, '钱奶奶', 'CARE', '2026-03', 1500.00, 1500.00, 'PAID', '2026-03-05'),
(5, 'F2026005', 3, '孙爷爷', 'ACCOMMODATION', '2026-03', 2000.00, 2000.00, 'PAID', '2026-03-05'),
(6, 'F2026006', 3, '孙爷爷', 'CARE', '2026-03', 3500.00, 3500.00, 'PAID', '2026-03-05'),
(7, 'F2026007', 5, '吴爷爷', 'ACCOMMODATION', '2026-03', 3000.00, 3000.00, 'PAID', '2026-03-05'),
(8, 'F2026008', 5, '吴爷爷', 'CARE', '2026-03', 3500.00, 3500.00, 'PAID', '2026-03-05');

-- ============================================
-- 11. 健康档案表 (health_record)
-- ============================================
INSERT INTO health_record (id, elder_id, record_date, record_type, temperature, blood_pressure_high, blood_pressure_low, heart_rate, blood_sugar, weight, height, chief_complaint, diagnosis, treatment_plan, doctor_id, doctor_name) VALUES
(1, 1, '2026-03-07', 'ROUTINE', 36.5, 140, 85, 72, 5.8, 65.5, 170.0, '血压偏高', '高血压', '继续服用降压药，定期监测', 2, '李医生'),
(2, 2, '2026-03-07', 'ROUTINE', 36.3, 125, 78, 68, 7.2, 58.0, 160.0, '血糖控制良好', '2型糖尿病', '继续服用降糖药，控制饮食', 2, '李医生'),
(3, 3, '2026-03-07', 'ROUTINE', 36.4, 135, 82, 75, 6.5, 70.2, 165.0, '胸闷气短', '冠心病', '按时服药，注意休息', 2, '李医生'),
(4, 5, '2026-03-07', 'ROUTINE', 36.6, 145, 88, 70, 6.8, 72.0, 168.0, '头晕', '高血压', '调整用药剂量', 2, '李医生');

-- ============================================
-- 12. 体检记录表 (physical_exam)
-- ============================================
INSERT INTO physical_exam (id, elder_id, exam_date, exam_type, vision_left, vision_right, hearing, ecg_result, blood_routine, urine_routine, liver_function, kidney_function, blood_sugar_fasting, blood_sugar_postprandial, ultrasound, x_ray, overall_assessment, health_suggestions) VALUES
(1, 1, '2026-02-15', 'ROUTINE', '0.8', '0.8', 'NORMAL', '窦性心律，正常', '正常', '正常', '正常', '正常', 5.2, 7.8, '正常', '正常', '健康状况良好，血压控制尚可', '继续按时服药，定期复查血压'),
(2, 2, '2026-02-15', 'ROUTINE', '0.6', '0.7', 'NORMAL', '窦性心律，正常', '正常', '正常', '正常', '正常', 6.8, 9.5, '正常', '正常', '血糖控制尚可', '继续控制饮食，定期监测血糖'),
(3, 3, '2026-02-15', 'ROUTINE', '0.5', '0.5', 'MILD', '窦性心律，ST段改变', '正常', '正常', '轻度脂肪肝', '正常', 5.5, 8.2, '正常', '正常', '冠心病稳定，需继续服药', '按时服药，避免剧烈运动，定期复查');

-- ============================================
-- 13. 公告表 (announcement)
-- ============================================
INSERT INTO announcement (id, title, content, type, priority, status, publish_time, publisher_id, publisher_name, view_count) VALUES
(1, '春节期间探视安排通知', '尊敬的各位家属：春节期间探视时间为上午9:00-11:00，下午14:00-16:00，请提前预约。', 'NOTICE', 'HIGH', 'PUBLISHED', NOW(), 1, '系统管理员', 156),
(2, '三月份健康讲座活动', '本月将举办老年人健康知识讲座，主题为"春季养生"，欢迎各位老人参加。', 'ACTIVITY', 'NORMAL', 'PUBLISHED', NOW(), 1, '系统管理员', 89),
(3, '新入住老人欢迎会', '欢迎新入住的吴爷爷，将于本周五下午举办欢迎会。', 'NOTICE', 'NORMAL', 'PUBLISHED', NOW(), 1, '系统管理员', 45);

-- ============================================
-- 14. 物资库存表 (inventory)
-- ============================================
INSERT INTO inventory (id, item_no, item_name, category, specification, unit, stock_quantity, min_stock, max_stock, supplier, price, storage_location, status) VALUES
(1, 'I2026001', '成人纸尿裤', 'DAILY_SUPPLIES', 'L号', '包', 50, 20, 100, '恒安集团', 35.00, '仓库A区-货架1', 'NORMAL'),
(2, 'I2026002', '医用口罩', 'MEDICAL', '一次性医用外科', '盒', 200, 50, 500, '稳健医疗', 12.00, '仓库B区-货架2', 'NORMAL'),
(3, 'I2026003', '消毒酒精', 'MEDICAL', '75% 500ml', '瓶', 30, 10, 50, '山东利尔康', 8.50, '仓库B区-货架3', 'NORMAL'),
(4, 'I2026004', '护理垫', 'DAILY_SUPPLIES', '60*90cm', '包', 15, 20, 100, '可靠护理', 28.00, '仓库A区-货架2', 'LOW_STOCK');

-- ============================================
-- 15. 活动表 (activity)
-- ============================================
INSERT INTO activity (id, activity_no, title, content, activity_type, start_time, end_time, location, organizer_id, organizer_name, max_participants, current_participants, status) VALUES
(1, 'A2026001', '晨练活动', '每天早上7:00-8:00的晨练活动，包括太极、广场舞等', 'SPORTS', '2026-03-07 07:00:00', '2026-03-07 08:00:00', '院内广场', 3, '王管家', 30, 15, 'ONGOING'),
(2, 'A2026002', '健康讲座', '邀请专家讲解春季养生知识', 'EDUCATIONAL', '2026-03-10 14:00:00', '2026-03-10 16:00:00', '多功能厅', 3, '王管家', 50, 20, 'PLANNING'),
(3, 'A2026003', '生日会', '为本月过生日的老人举办集体生日会', 'ENTERTAINMENT', '2026-03-15 15:00:00', '2026-03-15 17:00:00', '活动室', 3, '王管家', 40, 10, 'PLANNING');

-- ============================================
-- 16. 活动参与记录表 (activity_participant)
-- ============================================
INSERT INTO activity_participant (id, activity_id, elder_id, elder_name, sign_up_time, sign_in_time, status, feedback, satisfaction) VALUES
(1, 1, 1, '赵大爷', '2026-03-07 06:30:00', '2026-03-07 06:55:00', 'ATTENDED', '活动很好，锻炼了身体', 5),
(2, 1, 2, '钱奶奶', '2026-03-07 06:45:00', '2026-03-07 06:58:00', 'ATTENDED', '不错，老师教得很好', 5),
(3, 2, 1, '赵大爷', '2026-03-05 10:00:00', NULL, 'SIGNED_UP', NULL, NULL),
(4, 2, 2, '钱奶奶', '2026-03-05 10:30:00', NULL, 'SIGNED_UP', NULL, NULL),
(5, 3, 3, '孙爷爷', '2026-03-10 09:00:00', NULL, 'SIGNED_UP', NULL, NULL);

-- ============================================
-- 17. 访客记录表 (visitor_record)
-- ============================================
INSERT INTO visitor_record (id, visitor_name, visitor_phone, visitor_id_card, elder_id, elder_name, relation, visit_date, visit_time, leave_time, status, purpose, staff_id, staff_name) VALUES
(1, '赵明', '13800138101', '110101197001010011', 1, '赵大爷', '儿子', '2026-03-07', '14:00:00', '16:30:00', 'LEFT', '探望父亲', 3, '王管家'),
(2, '钱伟', '13800138102', '110101197502020022', 2, '钱奶奶', '儿子', '2026-03-06', '10:00:00', '12:00:00', 'LEFT', '探望母亲', 3, '王管家'),
(3, '孙丽', '13800138103', '110101198003030033', 3, '孙爷爷', '女儿', '2026-03-05', '15:00:00', '17:00:00', 'LEFT', '探望父亲', 3, '王管家');

-- ============================================
-- 18. 家属账户关联表 (family_elder_relation)
-- ============================================
INSERT INTO family_elder_relation (id, family_user_id, elder_id, relation, is_primary, can_view_health, can_view_fee, can_view_location, status) VALUES
(1, 7, 1, '儿子', 1, 1, 1, 1, 1),
(2, 8, 1, '女儿', 0, 1, 1, 1, 1),
(3, 9, 2, '儿子', 1, 1, 1, 1, 1),
(4, 10, 3, '女儿', 1, 1, 1, 1, 1),
(5, 11, 3, '儿子', 0, 1, 1, 1, 1),
(6, 12, 5, '女儿', 1, 1, 1, 1, 1);

-- ============================================
-- 19. 膳食菜单表 (meal_menu)
-- ============================================
INSERT INTO meal_menu (id, menu_date, meal_type, menu_name, dishes, nutrition_info, suitable_for, calories, create_by, create_time) VALUES
(101, '2026-03-07', 'BREAKFAST', '营养早餐A', '[{"name": "燕麦粥", "amount": "1碗"}, {"name": "鸡蛋", "amount": "1个"}, {"name": "牛奶", "amount": "1杯"}, {"name": "全麦面包", "amount": "2片"}]', '热量：350卡，蛋白质：15g，碳水：55g', '普通老人', 350, 1, NOW()),
(102, '2026-03-07', 'LUNCH', '健康午餐A', '[{"name": "清蒸鱼", "amount": "1份"}, {"name": "炒青菜", "amount": "1份"}, {"name": "米饭", "amount": "1碗"}, {"name": "紫菜蛋花汤", "amount": "1碗"}]', '热量：650卡，蛋白质：35g，碳水：85g', '普通老人', 650, 1, NOW()),
(103, '2026-03-07', 'DINNER', '清淡晚餐A', '[{"name": "白粥", "amount": "1碗"}, {"name": "蒸蛋", "amount": "1份"}, {"name": "炒青菜", "amount": "1份"}, {"name": "馒头", "amount": "1个"}]', '热量：450卡，蛋白质：22g，碳水：65g', '普通老人', 450, 1, NOW()),
(104, '2026-03-08', 'BREAKFAST', '营养早餐B', '[{"name": "小米粥", "amount": "1碗"}, {"name": "豆浆", "amount": "1杯"}, {"name": "包子", "amount": "2个"}, {"name": "咸菜", "amount": "适量"}]', '热量：320卡，蛋白质：12g，碳水：58g', '普通老人', 320, 1, NOW()),
(105, '2026-03-08', 'LUNCH', '健康午餐B', '[{"name": "红烧肉", "amount": "1份"}, {"name": "炒时蔬", "amount": "1份"}, {"name": "米饭", "amount": "1碗"}, {"name": "番茄蛋汤", "amount": "1碗"}]', '热量：680卡，蛋白质：38g，碳水：82g', '普通老人', 680, 1, NOW()),
(106, '2026-03-08', 'DINNER', '清淡晚餐B', '[{"name": "面条", "amount": "1碗"}, {"name": "肉丝炒青菜", "amount": "1份"}, {"name": "豆腐汤", "amount": "1碗"}]', '热量：480卡，蛋白质：25g，碳水：62g', '普通老人', 480, 1, NOW());

-- ============================================
-- 20. 老人膳食记录表 (elder_meal)
-- ============================================
INSERT INTO elder_meal (id, elder_id, meal_date, meal_type, menu_id, food_items, intake_amount, appetite, special_needs, allergies, record_by, record_by_name, remark) VALUES
(201, 1, '2026-03-07', 'BREAKFAST', 101, '燕麦粥、鸡蛋、牛奶、全麦面包', 'FULL', 'GOOD', NULL, NULL, 1, '张护士', '老人很喜欢'),
(202, 1, '2026-03-07', 'LUNCH', 102, '清蒸鱼、炒青菜、米饭、紫菜蛋花汤', 'FULL', 'GOOD', NULL, NULL, 1, '张护士', '吃得很香'),
(203, 1, '2026-03-07', 'DINNER', 103, '白粥、蒸蛋、炒青菜、馒头', 'FULL', 'NORMAL', NULL, NULL, 1, '张护士', NULL),
(204, 2, '2026-03-07', 'BREAKFAST', 101, '燕麦粥、鸡蛋、牛奶、全麦面包', 'FULL', 'NORMAL', NULL, NULL, 1, '张护士', NULL),
(205, 2, '2026-03-07', 'LUNCH', 102, '清蒸鱼、炒青菜、米饭、紫菜蛋花汤', 'FULL', 'GOOD', NULL, NULL, 1, '张护士', NULL),
(206, 2, '2026-03-07', 'DINNER', 103, '白粥、蒸蛋、炒青菜、馒头', 'FULL', 'NORMAL', NULL, NULL, 1, '张护士', NULL),
(207, 3, '2026-03-07', 'BREAKFAST', 101, '燕麦粥、鸡蛋、牛奶、全麦面包', 'HALF', 'NORMAL', '软食', NULL, 1, '张护士', '需要软食'),
(208, 3, '2026-03-07', 'LUNCH', 102, '清蒸鱼、炒青菜、米饭、紫菜蛋花汤', 'HALF', 'POOR', '低盐', NULL, 1, '张护士', '低盐饮食'),
(209, 5, '2026-03-08', 'BREAKFAST', 104, '小米粥、豆浆、包子、咸菜', 'FULL', 'GOOD', NULL, NULL, 1, '张护士', NULL),
(210, 5, '2026-03-08', 'LUNCH', 105, '红烧肉、炒时蔬、米饭、番茄蛋汤', 'FULL', 'GOOD', NULL, NULL, 1, '张护士', NULL);

-- ============================================
-- 21. 入住申请表 (check_in_application)
-- ============================================
INSERT INTO check_in_application (id, application_no, elder_name, gender, birth_date, id_card, phone, address, contact_name, contact_phone, contact_relation, health_status, care_level, expected_date, room_type, status, applicant_id, applicant_name, applicant_phone, apply_time) VALUES
(1, 'A2026001', '周奶奶', 'FEMALE', '1942-05-20', '110101194205200044', '13900139004', '北京市东城区', '周强', '13800138104', '儿子', '高血压、糖尿病', 'LEVEL2', '2026-04-01', 'SINGLE', 'PENDING', 7, '周强', '13800138104', NOW()),
(2, 'A2026002', '郑爷爷', 'MALE', '1935-08-15', '110101193508150055', '13900139006', '北京市石景山区', '郑华', '13800138108', '儿子', '心脏病、高血压', 'LEVEL3', '2026-03-20', 'DOUBLE', 'PENDING', 9, '郑华', '13800138108', NOW());

-- ============================================
-- 22. 系统配置表 (sys_config)
-- ============================================
INSERT INTO sys_config (id, config_key, config_value, config_type, description, is_system) VALUES
(1, 'nursing_home_name', '阳光养老院', 'STRING', '养老院名称', 0),
(2, 'nursing_home_phone', '010-12345678', 'STRING', '养老院联系电话', 0),
(3, 'nursing_home_address', '北京市朝阳区阳光路123号', 'STRING', '养老院地址', 0),
(4, 'check_in_time', '09:00-17:00', 'STRING', '入住办理时间', 0),
(5, 'visit_time', '09:00-11:00,14:00-16:00', 'STRING', '探视时间', 0),
(6, 'meal_breakfast_time', '07:00-08:00', 'STRING', '早餐时间', 0),
(7, 'meal_lunch_time', '12:00-13:00', 'STRING', '午餐时间', 0),
(8, 'meal_dinner_time', '18:00-19:00', 'STRING', '晚餐时间', 0);

-- ============================================
-- 23. 操作日志表 (operation_log)
-- ============================================
INSERT INTO operation_log (id, user_id, username, operation_type, operation_module, operation_desc, request_method, request_url, ip_address, execution_time, status) VALUES
(1, 1, 'admin', 'LOGIN', '系统登录', '用户登录系统', 'POST', '/api/auth/login', '192.168.1.100', 100, 'SUCCESS'),
(2, 3, 'nurse01', 'CREATE', '老人管理', '新增老人信息：赵大爷', 'POST', '/api/elder', '192.168.1.101', 200, 'SUCCESS'),
(3, 3, 'nurse01', 'CREATE', '老人管理', '新增老人信息：钱奶奶', 'POST', '/api/elder', '192.168.1.101', 200, 'SUCCESS'),
(4, 3, 'nurse01', 'CREATE', '老人管理', '新增老人信息：孙爷爷', 'POST', '/api/elder', '192.168.1.101', 200, 'SUCCESS'),
(5, 3, 'nurse01', 'UPDATE', '护理计划', '修改护理计划', 'PUT', '/api/care-plan', '192.168.1.101', 150, 'SUCCESS'),
(6, 3, 'nurse01', 'UPDATE', '用药记录', '记录服药：赵大爷服用降压药', 'PUT', '/api/medication-record', '192.168.1.101', 120, 'SUCCESS');

-- ============================================
-- 完成提示
-- ============================================
SELECT '2026年测试数据插入完成！' AS message;
