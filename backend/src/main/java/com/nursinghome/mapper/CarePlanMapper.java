package com.nursinghome.mapper;

import com.nursinghome.entity.CarePlan;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 护理计划Mapper接口
 */
public interface CarePlanMapper {
    
    CarePlan selectById(Long id);
    
    CarePlan selectByPlanNo(String planNo);
    
    List<CarePlan> selectAll();
    
    List<CarePlan> selectByElderId(@Param("elderId") Long elderId);
    
    List<CarePlan> selectByStatus(@Param("status") String status);
    
    List<CarePlan> selectByCondition(@Param("planName") String planName,
                                     @Param("elderName") String elderName,
                                     @Param("careLevel") String careLevel,
                                     @Param("status") String status,
                                     @Param("elderId") Long elderId);
    
    int insert(CarePlan carePlan);
    
    int update(CarePlan carePlan);
    
    int deleteById(Long id);
}
