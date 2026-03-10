package com.nursinghome.mapper;

import com.nursinghome.entity.ElderMedication;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 老人用药记录Mapper接口
 */
public interface ElderMedicationMapper {
    
    List<ElderMedication> selectAll();
    
    List<ElderMedication> selectByElderId(@Param("elderId") Long elderId);
    
    List<ElderMedication> selectByMedicineId(@Param("medicineId") Long medicineId);
    
    List<ElderMedication> selectByStatus(@Param("status") String status);
    
    ElderMedication selectById(@Param("id") Long id);
    
    int insert(ElderMedication elderMedication);
    
    int update(ElderMedication elderMedication);
    
    int deleteById(@Param("id") Long id);
    
    Long countAll();
    
    Long countByElderId(@Param("elderId") Long elderId);
    
    Long countByStatus(@Param("status") String status);
    
    Long countToday();
    
    List<ElderMedication> selectActiveByElderId(@Param("elderId") Long elderId);
}
