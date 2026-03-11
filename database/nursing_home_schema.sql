-- ============================================
-- 养老院管理系统数据库 - 表结构脚本
-- 仅包含CREATE TABLE语句，不包含数据
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS nursing_home 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE nursing_home;

-- ============================================
-- 1. 用户表 (sys_user)
-- ============================================
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(200) COMMENT '头像URL',
    role ENUM('ADMIN', 'MANAGER', 'NURSE', 'DOCTOR', 'STAFF') DEFAULT 'STAFF' COMMENT '角色',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ============================================
-- 2. 老人信息表 (elder)
-- ============================================
CREATE TABLE elder (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '老人ID',
    elder_no VARCHAR(20) NOT NULL UNIQUE COMMENT '老人编号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender ENUM('MALE', 'FEMALE') NOT NULL COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    age INT COMMENT '年龄',
    id_card VARCHAR(18) UNIQUE COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(200) COMMENT '家庭住址',
    contact_name VARCHAR(50) COMMENT '紧急联系人姓名',
    contact_phone VARCHAR(20) COMMENT '紧急联系人电话',
    contact_relation VARCHAR(20) COMMENT '与老人关系',
    health_status ENUM('HEALTHY', 'SUBHEALTH', 'CHRONIC', 'DISABLED') DEFAULT 'HEALTHY' COMMENT '健康状况',
    care_level ENUM('LEVEL1', 'LEVEL2', 'LEVEL3', 'LEVEL4') DEFAULT 'LEVEL1' COMMENT '护理等级',
    status ENUM('PENDING', 'CHECKED_IN', 'CHECKED_OUT', 'SUSPENDED') DEFAULT 'PENDING' COMMENT '状态',
    check_in_date DATE COMMENT '入住日期',
    check_out_date DATE COMMENT '退住日期',
    room_id BIGINT COMMENT '房间ID',
    bed_no VARCHAR(20) COMMENT '床位号',
    photo VARCHAR(200) COMMENT '照片URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人信息表';

-- ============================================
-- 3. 入住申请表 (check_in_application)
-- ============================================
CREATE TABLE check_in_application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    application_no VARCHAR(20) NOT NULL UNIQUE COMMENT '申请编号',
    elder_name VARCHAR(50) NOT NULL COMMENT '老人姓名',
    gender ENUM('MALE', 'FEMALE') NOT NULL COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(200) COMMENT '家庭住址',
    contact_name VARCHAR(50) COMMENT '紧急联系人姓名',
    contact_phone VARCHAR(20) COMMENT '紧急联系人电话',
    contact_relation VARCHAR(20) COMMENT '与老人关系',
    health_status VARCHAR(200) COMMENT '健康状况描述',
    care_level ENUM('LEVEL1', 'LEVEL2', 'LEVEL3', 'LEVEL4') DEFAULT 'LEVEL1' COMMENT '期望护理等级',
    expected_date DATE COMMENT '期望入住日期',
    room_type ENUM('SINGLE', 'DOUBLE', 'TRIPLE', 'MULTI', 'SUITE') COMMENT '期望房型',
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') DEFAULT 'PENDING' COMMENT '申请状态',
    applicant_id BIGINT COMMENT '申请人ID',
    applicant_name VARCHAR(50) COMMENT '申请人姓名',
    applicant_phone VARCHAR(20) COMMENT '申请人电话',
    apply_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    approve_time DATETIME COMMENT '审批时间',
    approve_by BIGINT COMMENT '审批人ID',
    approve_remark VARCHAR(500) COMMENT '审批备注',
    room_id BIGINT COMMENT '分配房间ID',
    bed_id BIGINT COMMENT '分配床位ID',
    check_in_date DATE COMMENT '实际入住日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入住申请表';

-- ============================================
-- 4. 护理计划表 (care_plan)
-- ============================================
CREATE TABLE care_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    plan_no VARCHAR(20) NOT NULL UNIQUE COMMENT '计划编号',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    plan_name VARCHAR(100) NOT NULL COMMENT '计划名称',
    care_level ENUM('LEVEL1', 'LEVEL2', 'LEVEL3', 'LEVEL4') NOT NULL COMMENT '护理等级',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status ENUM('ACTIVE', 'PAUSED', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE' COMMENT '状态',
    nurse_id BIGINT COMMENT '负责护士ID',
    nurse_name VARCHAR(50) COMMENT '负责护士姓名',
    diet_plan TEXT COMMENT '饮食计划',
    medication_plan TEXT COMMENT '用药计划',
    daily_care TEXT COMMENT '日常护理计划',
    rehabilitation_plan TEXT COMMENT '康复计划',
    special_notes TEXT COMMENT '特殊注意事项',
    create_by BIGINT COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理计划表';

-- ============================================
-- 5. 护理记录表 (care_record)
-- ============================================
CREATE TABLE care_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    record_no VARCHAR(20) NOT NULL UNIQUE COMMENT '记录编号',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    plan_id BIGINT COMMENT '护理计划ID',
    record_date DATE NOT NULL COMMENT '记录日期',
    record_time TIME NOT NULL COMMENT '记录时间',
    record_type ENUM('DAILY_CARE', 'MEDICATION', 'DIET', 'VITAL_SIGNS', 'REHABILITATION', 'OTHER') NOT NULL COMMENT '记录类型',
    content TEXT NOT NULL COMMENT '护理内容',
    vital_signs VARCHAR(200) COMMENT '生命体征(体温、血压、脉搏等)',
    nurse_id BIGINT NOT NULL COMMENT '护理人员ID',
    nurse_name VARCHAR(50) COMMENT '护理人员姓名',
    status ENUM('NORMAL', 'ABNORMAL') DEFAULT 'NORMAL' COMMENT '状态',
    abnormal_desc VARCHAR(500) COMMENT '异常情况描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理记录表';

-- ============================================
-- 6. 员工表 (employee)
-- ============================================
CREATE TABLE employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '员工ID',
    emp_no VARCHAR(20) NOT NULL UNIQUE COMMENT '员工编号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender ENUM('MALE', 'FEMALE') NOT NULL COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) UNIQUE COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '家庭住址',
    department ENUM('ADMIN', 'NURSING', 'MEDICAL', 'CATERING', 'HOUSEKEEPING', 'SECURITY', 'OTHER') NOT NULL COMMENT '部门',
    position VARCHAR(50) COMMENT '职位',
    job_title VARCHAR(50) COMMENT '职称',
    education VARCHAR(20) COMMENT '学历',
    entry_date DATE COMMENT '入职日期',
    leave_date DATE COMMENT '离职日期',
    status ENUM('ACTIVE', 'LEAVE', 'RESIGNED') DEFAULT 'ACTIVE' COMMENT '状态',
    salary DECIMAL(10,2) COMMENT '薪资',
    photo VARCHAR(200) COMMENT '照片URL',
    user_id BIGINT COMMENT '关联用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- ============================================
-- 7. 房间表 (room)
-- ============================================
CREATE TABLE room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '房间ID',
    room_no VARCHAR(20) NOT NULL UNIQUE COMMENT '房间号',
    room_type ENUM('SINGLE', 'DOUBLE', 'TRIPLE', 'MULTI', 'SUITE') NOT NULL COMMENT '房间类型',
    floor INT COMMENT '楼层',
    area DECIMAL(8,2) COMMENT '面积(平方米)',
    bed_count INT DEFAULT 1 COMMENT '床位数',
    price DECIMAL(10,2) COMMENT '价格(元/月)',
    facilities VARCHAR(500) COMMENT '设施配置',
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'RESERVED') DEFAULT 'AVAILABLE' COMMENT '状态',
    occupied_beds INT DEFAULT 0 COMMENT '已占用床位数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间表';

-- ============================================
-- 8. 费用表 (fee_record)
-- ============================================
CREATE TABLE fee_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    record_no VARCHAR(20) NOT NULL UNIQUE COMMENT '记录编号',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    fee_type ENUM('ACCOMMODATION', 'MEDICAL', 'OTHER') NOT NULL COMMENT '费用类型：ACCOMMODATION-住宿费(房间表支持), MEDICAL-医疗费(药品表支持), OTHER-其他费用',
    fee_month VARCHAR(7) NOT NULL COMMENT '费用月份(yyyy-MM)',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已付金额',
    status ENUM('UNPAID', 'PARTIAL', 'PAID', 'OVERDUE') DEFAULT 'UNPAID' COMMENT '状态',
    due_date DATE COMMENT '应缴日期',
    pay_time DATETIME COMMENT '支付时间',
    pay_method ENUM('CASH', 'BANK_TRANSFER', 'ALIPAY', 'WECHAT', 'OTHER') COMMENT '支付方式',
    create_by BIGINT COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费用记录表';

-- ============================================
-- 9. 访客记录表 (visitor_record)
-- ============================================
CREATE TABLE visitor_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    visitor_name VARCHAR(50) NOT NULL COMMENT '访客姓名',
    visitor_phone VARCHAR(20) COMMENT '访客电话',
    visitor_id_card VARCHAR(18) COMMENT '访客身份证号',
    elder_id BIGINT NOT NULL COMMENT '探访老人ID',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    relation VARCHAR(20) COMMENT '与老人关系',
    visit_date DATE NOT NULL COMMENT '探访日期',
    visit_time TIME NOT NULL COMMENT '到访时间',
    leave_time TIME COMMENT '离开时间',
    status ENUM('VISITING', 'LEFT', 'CANCELLED') DEFAULT 'VISITING' COMMENT '状态',
    purpose VARCHAR(200) COMMENT '探访目的',
    staff_id BIGINT COMMENT '登记员工ID',
    staff_name VARCHAR(50) COMMENT '登记员工姓名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客记录表';

-- ============================================
-- 10. 公告表 (announcement)
-- ============================================
CREATE TABLE announcement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    type ENUM('NOTICE', 'ACTIVITY', 'POLICY', 'OTHER') DEFAULT 'NOTICE' COMMENT '类型',
    priority ENUM('HIGH', 'NORMAL', 'LOW') DEFAULT 'NORMAL' COMMENT '优先级',
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT' COMMENT '状态',
    publish_time DATETIME COMMENT '发布时间',
    publisher_id BIGINT COMMENT '发布人ID',
    publisher_name VARCHAR(50) COMMENT '发布人姓名',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ============================================
-- 11. 药品信息表 (medicine)
-- ============================================
CREATE TABLE medicine (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '药品ID',
    medicine_no VARCHAR(20) NOT NULL UNIQUE COMMENT '药品编号',
    name VARCHAR(100) NOT NULL COMMENT '药品名称',
    category VARCHAR(50) NOT NULL COMMENT '类别：心血管类/糖尿病类/呼吸系统/消化系统/神经系统/抗生素类/维生素类/中药类/西药类/保健品/其他',
    specification VARCHAR(100) COMMENT '规格',
    manufacturer VARCHAR(100) COMMENT '生产厂家',
    batch_no VARCHAR(50) COMMENT '批号',
    expiry_date DATE COMMENT '有效期',
    stock_quantity INT DEFAULT 0 COMMENT '库存数量',
    unit VARCHAR(20) COMMENT '单位（片/粒/瓶/盒）',
    price DECIMAL(10,2) COMMENT '单价',
    storage_condition VARCHAR(50) COMMENT '储存条件',
    usage_method TEXT COMMENT '用法用量',
    contraindication TEXT COMMENT '禁忌',
    side_effects TEXT COMMENT '不良反应',
    status ENUM('ACTIVE', 'DISABLED', 'EXPIRED') DEFAULT 'ACTIVE' COMMENT '状态：正常/禁用/过期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品信息表';

-- ============================================
-- 12. 老人用药记录表 (elder_medication)
-- ============================================
CREATE TABLE elder_medication (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    medicine_id BIGINT NOT NULL COMMENT '药品ID',
    medicine_name VARCHAR(100) COMMENT '药品名称',
    dosage VARCHAR(50) COMMENT '剂量',
    frequency VARCHAR(50) COMMENT '服用频率（如：每日3次）',
    administration_time VARCHAR(100) COMMENT '服用时间（如：早8点、午12点、晚6点）',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    prescribed_by BIGINT COMMENT '开药医生ID',
    prescribed_by_name VARCHAR(50) COMMENT '开药医生姓名',
    status ENUM('ACTIVE', 'COMPLETED', 'STOPPED') DEFAULT 'ACTIVE' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_elder_medicine (elder_id, medicine_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人用药记录表';

-- ============================================
-- 13. 用药执行记录表 (medication_record)
-- ============================================
CREATE TABLE medication_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    elder_medication_id BIGINT NOT NULL COMMENT '用药记录ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    medicine_id BIGINT NOT NULL COMMENT '药品ID',
    medicine_name VARCHAR(100) COMMENT '药品名称',
    dosage VARCHAR(50) COMMENT '剂量',
    scheduled_time DATETIME NOT NULL COMMENT '计划服用时间',
    actual_time DATETIME COMMENT '实际服用时间',
    status ENUM('PENDING', 'TAKEN', 'MISSED', 'SKIPPED') DEFAULT 'PENDING' COMMENT '状态',
    executor_id BIGINT COMMENT '执行人ID',
    executor_name VARCHAR(50) COMMENT '执行人姓名',
    result ENUM('SUCCESS', 'REFUSED', 'VOMITED', 'OTHER') COMMENT '服用结果',
    notes VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_elder_date (elder_id, scheduled_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用药执行记录表';

-- ============================================
-- 14. 膳食菜单表 (meal_menu)
-- ============================================
CREATE TABLE meal_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    menu_date DATE NOT NULL COMMENT '菜单日期',
    meal_type ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK') NOT NULL COMMENT '餐次：早餐/午餐/晚餐/加餐',
    menu_name VARCHAR(100) COMMENT '菜单名称',
    dishes TEXT COMMENT '菜品列表（JSON格式）',
    nutrition_info TEXT COMMENT '营养信息',
    suitable_for VARCHAR(200) COMMENT '适用人群（如：糖尿病、高血压等）',
    calories INT COMMENT '卡路里',
    create_by BIGINT COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_date_type (menu_date, meal_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='膳食菜单表';

-- ============================================
-- 15. 老人膳食记录表 (elder_meal)
-- ============================================
CREATE TABLE elder_meal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    meal_date DATE NOT NULL COMMENT '用餐日期',
    meal_type ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK') NOT NULL COMMENT '餐次',
    menu_id BIGINT COMMENT '菜单ID',
    food_items TEXT COMMENT '实际食物',
    intake_amount ENUM('FULL', 'HALF', 'LITTLE', 'NONE') DEFAULT 'FULL' COMMENT '摄入量',
    appetite ENUM('GOOD', 'NORMAL', 'POOR', 'NONE') DEFAULT 'NORMAL' COMMENT '食欲',
    special_needs VARCHAR(200) COMMENT '特殊需求（如：软食、流食）',
    allergies VARCHAR(200) COMMENT '过敏食物',
    record_by BIGINT COMMENT '记录人ID',
    record_by_name VARCHAR(50) COMMENT '记录人姓名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_elder_date (elder_id, meal_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人膳食记录表';

-- ============================================
-- 16. 健康档案表 (health_record)
-- ============================================
CREATE TABLE health_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    record_date DATE NOT NULL COMMENT '记录日期',
    record_type ENUM('ROUTINE', 'EMERGENCY', 'CHECKUP', 'FOLLOWUP') DEFAULT 'ROUTINE' COMMENT '记录类型',
    temperature DECIMAL(4,1) COMMENT '体温(°C)',
    blood_pressure_high INT COMMENT '收缩压(mmHg)',
    blood_pressure_low INT COMMENT '舒张压(mmHg)',
    heart_rate INT COMMENT '心率(次/分)',
    respiratory_rate INT COMMENT '呼吸频率(次/分)',
    blood_sugar DECIMAL(4,2) COMMENT '血糖(mmol/L)',
    weight DECIMAL(5,2) COMMENT '体重(kg)',
    height DECIMAL(5,2) COMMENT '身高(cm)',
    chief_complaint VARCHAR(500) COMMENT '主诉',
    present_illness TEXT COMMENT '现病史',
    physical_exam TEXT COMMENT '体格检查',
    diagnosis VARCHAR(500) COMMENT '诊断结果',
    treatment_plan TEXT COMMENT '治疗方案',
    doctor_id BIGINT COMMENT '医生ID',
    doctor_name VARCHAR(50) COMMENT '医生姓名',
    attachment_urls TEXT COMMENT '附件URL（检查报告等）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_elder_date (elder_id, record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康档案表';

-- ============================================
-- 17. 体检记录表 (physical_exam)
-- ============================================
CREATE TABLE physical_exam (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '体检ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    exam_date DATE NOT NULL COMMENT '体检日期',
    exam_org VARCHAR(100) COMMENT '体检机构',
    exam_type ENUM('ROUTINE', 'COMPREHENSIVE', 'SPECIAL') DEFAULT 'ROUTINE' COMMENT '体检类型',
    vision_left VARCHAR(10) COMMENT '左眼视力',
    vision_right VARCHAR(10) COMMENT '右眼视力',
    hearing ENUM('NORMAL', 'MILD', 'MODERATE', 'SEVERE') COMMENT '听力',
    ecg_result VARCHAR(200) COMMENT '心电图结果',
    blood_routine VARCHAR(500) COMMENT '血常规',
    urine_routine VARCHAR(500) COMMENT '尿常规',
    liver_function VARCHAR(500) COMMENT '肝功能',
    kidney_function VARCHAR(500) COMMENT '肾功能',
    lipid_profile VARCHAR(500) COMMENT '血脂',
    blood_sugar_fasting DECIMAL(4,2) COMMENT '空腹血糖',
    blood_sugar_postprandial DECIMAL(4,2) COMMENT '餐后血糖',
    ultrasound VARCHAR(500) COMMENT 'B超结果',
    x_ray VARCHAR(500) COMMENT 'X光结果',
    ct_mri VARCHAR(500) COMMENT 'CT/MRI结果',
    overall_assessment TEXT COMMENT '综合评估',
    health_suggestions TEXT COMMENT '健康建议',
    report_url VARCHAR(500) COMMENT '体检报告URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elder_date (elder_id, exam_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体检记录表';

-- ============================================
-- 18. 物资库存表 (inventory)
-- ============================================
CREATE TABLE inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物资ID',
    item_no VARCHAR(20) NOT NULL UNIQUE COMMENT '物资编号',
    item_name VARCHAR(100) NOT NULL COMMENT '物资名称',
    category ENUM('DAILY_SUPPLIES', 'MEDICAL', 'FOOD', 'CLEANING', 'OFFICE', 'OTHER') NOT NULL COMMENT '类别',
    specification VARCHAR(100) COMMENT '规格',
    unit VARCHAR(20) COMMENT '单位',
    stock_quantity INT DEFAULT 0 COMMENT '库存数量',
    min_stock INT DEFAULT 10 COMMENT '最低库存',
    max_stock INT DEFAULT 100 COMMENT '最高库存',
    supplier VARCHAR(100) COMMENT '供应商',
    price DECIMAL(10,2) COMMENT '单价',
    storage_location VARCHAR(100) COMMENT '存放位置',
    status ENUM('NORMAL', 'LOW_STOCK', 'OUT_OF_STOCK', 'DISCONTINUED') DEFAULT 'NORMAL' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物资库存表';

-- ============================================
-- 19. 物资出入库记录表 (inventory_record)
-- ============================================
CREATE TABLE inventory_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    item_id BIGINT NOT NULL COMMENT '物资ID',
    item_name VARCHAR(100) COMMENT '物资名称',
    record_type ENUM('IN', 'OUT') NOT NULL COMMENT '类型：入库/出库',
    quantity INT NOT NULL COMMENT '数量',
    before_stock INT COMMENT '操作前库存',
    after_stock INT COMMENT '操作后库存',
    reason VARCHAR(200) COMMENT '原因/用途',
    related_id BIGINT COMMENT '关联ID（如老人ID）',
    related_name VARCHAR(50) COMMENT '关联名称',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_item_time (item_id, operate_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物资出入库记录表';

-- ============================================
-- 20. 活动表 (activity)
-- ============================================
CREATE TABLE activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    activity_no VARCHAR(20) NOT NULL UNIQUE COMMENT '活动编号',
    title VARCHAR(200) NOT NULL COMMENT '活动标题',
    content TEXT COMMENT '活动内容',
    activity_type ENUM('ENTERTAINMENT', 'SPORTS', 'CULTURAL', 'EDUCATIONAL', 'SOCIAL', 'OTHER') NOT NULL COMMENT '活动类型',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    location VARCHAR(200) COMMENT '活动地点',
    organizer_id BIGINT COMMENT '组织者ID',
    organizer_name VARCHAR(50) COMMENT '组织者姓名',
    max_participants INT COMMENT '最大参与人数',
    current_participants INT DEFAULT 0 COMMENT '当前参与人数',
    status ENUM('PLANNING', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'PLANNING' COMMENT '状态',
    cover_image VARCHAR(500) COMMENT '封面图片',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- ============================================
-- 21. 活动参与记录表 (activity_participant)
-- ============================================
CREATE TABLE activity_participant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    sign_up_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    sign_in_time DATETIME COMMENT '签到时间',
    status ENUM('SIGNED_UP', 'ATTENDED', 'ABSENT', 'CANCELLED') DEFAULT 'SIGNED_UP' COMMENT '状态',
    feedback TEXT COMMENT '反馈/评价',
    satisfaction INT COMMENT '满意度评分(1-5)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_activity_elder (activity_id, elder_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与记录表';

-- ============================================
-- 22. 家属账户关联表 (family_elder_relation)
-- ============================================
CREATE TABLE family_elder_relation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    family_user_id BIGINT NOT NULL COMMENT '家属用户ID',
    elder_id BIGINT NOT NULL COMMENT '老人ID',
    relation VARCHAR(20) NOT NULL COMMENT '关系（儿子/女儿/配偶等）',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主要联系人',
    can_view_health TINYINT DEFAULT 1 COMMENT '是否可查看健康信息',
    can_view_fee TINYINT DEFAULT 1 COMMENT '是否可查看费用信息',
    can_view_location TINYINT DEFAULT 1 COMMENT '是否可查看位置信息',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_family_elder (family_user_id, elder_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属账户关联表';

-- ============================================
-- 23. 紧急事件记录表 (emergency_event)
-- ============================================
CREATE TABLE emergency_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '事件ID',
    event_no VARCHAR(20) NOT NULL UNIQUE COMMENT '事件编号',
    event_type ENUM('FALL', 'ILLNESS', 'INJURY', 'MISSING', 'FIRE', 'OTHER') NOT NULL COMMENT '事件类型',
    elder_id BIGINT COMMENT '涉及老人ID',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    event_time DATETIME NOT NULL COMMENT '发生时间',
    location VARCHAR(200) COMMENT '发生地点',
    description TEXT COMMENT '事件描述',
    severity ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') DEFAULT 'MEDIUM' COMMENT '严重程度',
    response_measures TEXT COMMENT '处理措施',
    handler_id BIGINT COMMENT '处理人ID',
    handler_name VARCHAR(50) COMMENT '处理人姓名',
    handle_time DATETIME COMMENT '处理时间',
    result VARCHAR(500) COMMENT '处理结果',
    status ENUM('PENDING', 'HANDLING', 'RESOLVED', 'CLOSED') DEFAULT 'PENDING' COMMENT '状态',
    notify_family TINYINT DEFAULT 0 COMMENT '是否已通知家属',
    notify_time DATETIME COMMENT '通知时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_event_time (event_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='紧急事件记录表';

-- ============================================
-- 24. 系统操作日志表 (operation_log)
-- ============================================
CREATE TABLE operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '用户名',
    operation_type ENUM('LOGIN', 'LOGOUT', 'CREATE', 'UPDATE', 'DELETE', 'QUERY', 'EXPORT', 'IMPORT', 'OTHER') NOT NULL COMMENT '操作类型',
    operation_module VARCHAR(50) COMMENT '操作模块',
    operation_desc VARCHAR(500) COMMENT '操作描述',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    response_data TEXT COMMENT '响应数据',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    execution_time INT COMMENT '执行时间(ms)',
    status ENUM('SUCCESS', 'FAIL') DEFAULT 'SUCCESS' COMMENT '状态',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_time (user_id, create_time),
    INDEX idx_operation_type (operation_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

-- ============================================
-- 25. 系统配置表 (sys_config)
-- ============================================
CREATE TABLE sys_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') DEFAULT 'STRING' COMMENT '配置类型',
    description VARCHAR(500) COMMENT '配置描述',
    is_system TINYINT DEFAULT 0 COMMENT '是否系统配置',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ============================================
-- 创建索引
-- ============================================
CREATE INDEX idx_elder_status ON elder(status);
CREATE INDEX idx_elder_room ON elder(room_id);
CREATE INDEX idx_application_status ON check_in_application(status);
CREATE INDEX idx_care_plan_elder ON care_plan(elder_id);
CREATE INDEX idx_care_record_elder ON care_record(elder_id);
CREATE INDEX idx_employee_dept ON employee(department);
CREATE INDEX idx_employee_status ON employee(status);
CREATE INDEX idx_room_status ON room(status);
CREATE INDEX idx_fee_elder ON fee_record(elder_id);
CREATE INDEX idx_visitor_elder ON visitor_record(elder_id);
CREATE INDEX idx_medicine_category ON medicine(category);
CREATE INDEX idx_medicine_status ON medicine(status);
CREATE INDEX idx_elder_medication_elder ON elder_medication(elder_id);
CREATE INDEX idx_medication_record_elder ON medication_record(elder_id);
CREATE INDEX idx_meal_menu_date ON meal_menu(menu_date);
CREATE INDEX idx_elder_meal_elder ON elder_meal(elder_id);
CREATE INDEX idx_health_record_elder ON health_record(elder_id);
CREATE INDEX idx_inventory_category ON inventory(category);
CREATE INDEX idx_inventory_status ON inventory(status);
CREATE INDEX idx_activity_status ON activity(status);
CREATE INDEX idx_emergency_event_status ON emergency_event(status);
CREATE INDEX idx_operation_log_time ON operation_log(create_time);

-- ============================================
-- 表结构创建完成
-- ============================================
SELECT '所有表结构创建完成！' AS message;
