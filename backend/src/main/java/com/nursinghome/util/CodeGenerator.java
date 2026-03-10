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
     * 生成老人编号 L + 年月日 + 4位随机数
     */
    public static String generateElderNo() {
        return "L" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成申请编号 A + 年月日 + 4位随机数
     */
    public static String generateApplicationNo() {
        return "A" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成护理计划编号 P + 年月日 + 4位随机数
     */
    public static String generatePlanNo() {
        return "P" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成员工编号 E + 年月日 + 4位随机数
     */
    public static String generateEmpNo() {
        return "E" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成费用记录编号 F + 年月日 + 4位随机数
     */
    public static String generateFeeNo() {
        return "F" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成护理记录编号 R + 年月日 + 4位随机数
     */
    public static String generateRecordNo() {
        return "R" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成药品编号 M + 年月日 + 4位随机数
     */
    public static String generateMedicineNo() {
        return "M" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成老人用药记录编号 EM + 年月日 + 4位随机数
     */
    public static String generateElderMedicationNo() {
        return "EM" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成用药执行记录编号 MR + 年月日 + 4位随机数
     */
    public static String generateMedicationRecordNo() {
        return "MR" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成菜单编号 MM + 年月日 + 4位随机数
     */
    public static String generateMealMenuNo() {
        return "MM" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成老人膳食记录编号 EML + 年月日 + 4位随机数
     */
    public static String generateElderMealNo() {
        return "EML" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成健康档案编号 HR + 年月日 + 4位随机数
     */
    public static String generateHealthRecordNo() {
        return "HR" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成体检记录编号 PE + 年月日 + 4位随机数
     */
    public static String generatePhysicalExamNo() {
        return "PE" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成紧急事件编号 EV + 年月日 + 4位随机数
     */
    public static String generateEmergencyEventNo() {
        return "EV" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成库存物品编号 I + 年月日 + 4位随机数
     */
    public static String generateInventoryNo() {
        return "I" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成库存操作记录编号 IR + 年月日 + 4位随机数
     */
    public static String generateInventoryRecordNo() {
        return "IR" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }

    /**
     * 生成活动编号 AC + 年月日 + 4位随机数
     */
    public static String generateActivityNo() {
        return "AC" + DATE_FORMAT.format(new Date()) + String.format("%04d", RANDOM.nextInt(10000));
    }
}
