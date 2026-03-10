package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 老人膳食记录实体类
 */
@Data
public class ElderMeal {

    private Long id;
    private Long elderId;
    private String elderName;
    private LocalDate mealDate;
    private String mealType;
    private Long menuId;
    private String foodItems;
    private String intakeAmount;
    private String appetite;
    private String specialNeeds;
    private String allergies;
    private Long recordBy;
    private String recordByName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;

    // 餐次常量
    public static final String MEAL_BREAKFAST = "BREAKFAST";
    public static final String MEAL_LUNCH = "LUNCH";
    public static final String MEAL_DINNER = "DINNER";
    public static final String MEAL_SNACK = "SNACK";

    // 摄入量常量
    public static final String INTAKE_FULL = "FULL";
    public static final String INTAKE_HALF = "HALF";
    public static final String INTAKE_LITTLE = "LITTLE";
    public static final String INTAKE_NONE = "NONE";

    // 食欲常量
    public static final String APPETITE_GOOD = "GOOD";
    public static final String APPETITE_NORMAL = "NORMAL";
    public static final String APPETITE_POOR = "POOR";
    public static final String APPETITE_NONE = "NONE";
}
