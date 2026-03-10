package com.nursinghome.mapper;

import com.nursinghome.entity.MedicationRecord;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

/**
 * 用药执行记录Mapper接口
 */
public interface MedicationRecordMapper {
    
    List<MedicationRecord> selectAll();
    
    List<MedicationRecord> selectByElderId(@Param("elderId") Long elderId);
    
    List<MedicationRecord> selectByElderMedicationId(@Param("elderMedicationId") Long elderMedicationId);
    
    List<MedicationRecord> selectByStatus(@Param("status") String status);
    
    List<MedicationRecord> selectByDate(@Param("date") LocalDate date);
    
    MedicationRecord selectById(@Param("id") Long id);
    
    int insert(MedicationRecord medicationRecord);
    
    int update(MedicationRecord medicationRecord);
    
    int deleteById(@Param("id") Long id);
    
    Long countAll();
    
    Long countByElderId(@Param("elderId") Long elderId);
    
    Long countByStatus(@Param("status") String status);
    
    Long countByDate(@Param("date") LocalDate date);
    
    Long countByDateAndStatus(@Param("date") LocalDate date, @Param("status") String status);
}
