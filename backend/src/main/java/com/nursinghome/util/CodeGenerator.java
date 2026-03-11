package com.nursinghome.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 编号生成工具类
 */
public class CodeGenerator {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final Random RANDOM = new Random();

    /**
     * 生成编号
     * @param prefix 前缀
     * @return 编号
     */
    private static String generate(String prefix) {
        return prefix + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成老人编号
     */
    public static String generateElderNo() {
        return generate("L");
    }

    /**
     * 生成申请编号
     */
    public static String generateApplicationNo() {
        return generate("A");
    }

    /**
     * 生成护理计划编号
     */
    public static String generatePlanNo() {
        return generate("P");
    }

    /**
     * 生成员工编号
     */
    public static String generateEmpNo() {
        return generate("E");
    }

    /**
     * 生成费用记录编号
     */
    public static String generateFeeNo() {
        return generate("F");
    }

    /**
     * 生成护理记录编号
     */
    public static String generateRecordNo() {
        return generate("R");
    }

    /**
     * 生成药品编号
     */
    public static String generateMedicineNo() {
        return generate("M");
    }

    /**
     * 生成老人用药记录编号
     */
    public static String generateElderMedicationNo() {
        return generate("EM");
    }

    /**
     * 生成用药执行记录编号
     */
    public static String generateMedicationRecordNo() {
        return generate("MR");
    }

    /**
     * 生成菜单编号
     */
    public static String generateMealMenuNo() {
        return generate("MM");
    }

    /**
     * 生成老人膳食记录编号
     */
    public static String generateElderMealNo() {
        return generate("EML");
    }

    /**
     * 生成健康档案编号
     */
    public static String generateHealthRecordNo() {
        return generate("HR");
    }

    /**
     * 生成体检记录编号
     */
    public static String generatePhysicalExamNo() {
        return generate("PE");
    }

    /**
     * 生成紧急事件编号
     */
    public static String generateEmergencyEventNo() {
        return generate("EV");
    }

    /**
     * 生成库存物品编号
     */
    public static String generateInventoryNo() {
        return generate("I");
    }

    /**
     * 生成库存操作记录编号
     */
    public static String generateInventoryRecordNo() {
        return generate("IR");
    }

    /**
     * 生成活动编号
     */
    public static String generateActivityNo() {
        return generate("AC");
    }
}
