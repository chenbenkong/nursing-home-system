package com.nursinghome.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 膳食菜单实体类
 */
@Data
public class MealMenu {

    private Long id;
    private LocalDate menuDate;
    private String mealType;
    private String menuName;
    private String dishes;
    private String nutritionInfo;
    private String suitableFor;
    private Integer calories;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 餐次常量
    public static final String MEAL_BREAKFAST = "BREAKFAST";
    public static final String MEAL_LUNCH = "LUNCH";
    public static final String MEAL_DINNER = "DINNER";
    public static final String MEAL_SNACK = "SNACK";
}
