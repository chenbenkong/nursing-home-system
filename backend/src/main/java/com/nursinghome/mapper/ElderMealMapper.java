package com.nursinghome.mapper;

import com.nursinghome.entity.ElderMeal;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

public interface ElderMealMapper {
    List<ElderMeal> selectAll();
    List<ElderMeal> selectByElderId(@Param("elderId") Long elderId);
    List<ElderMeal> selectByDate(@Param("date") LocalDate date);
    List<ElderMeal> selectByElderIdAndDate(@Param("elderId") Long elderId, @Param("date") LocalDate date);
    List<ElderMeal> selectByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    ElderMeal selectById(@Param("id") Long id);
    int insert(ElderMeal elderMeal);
    int update(ElderMeal elderMeal);
    int deleteById(@Param("id") Long id);
    Long countAll();
    Long countByElderId(@Param("elderId") Long elderId);
    Long countByDate(@Param("date") LocalDate date);
}
