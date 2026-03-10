package com.nursinghome.mapper;

import com.nursinghome.entity.MealMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MealMenuMapper {
    List<MealMenu> selectAll();
    List<MealMenu> selectByMealType(@Param("mealType") String mealType);
    List<MealMenu> selectByCategory(@Param("category") String category);
    List<MealMenu> selectByStatus(@Param("status") String status);
    List<MealMenu> selectByName(@Param("name") String name);
    MealMenu selectById(@Param("id") Long id);
    int insert(MealMenu mealMenu);
    int update(MealMenu mealMenu);
    int deleteById(@Param("id") Long id);
    Long countAll();
}
