package com.nursinghome.mapper;

import com.nursinghome.entity.HealthRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface HealthRecordMapper {
    List<HealthRecord> selectAll();
    List<HealthRecord> selectByElderId(@Param("elderId") Long elderId);
    List<HealthRecord> selectByStatus(@Param("status") String status);
    List<HealthRecord> selectByBloodType(@Param("bloodType") String bloodType);
    HealthRecord selectById(@Param("id") Long id);
    HealthRecord selectByRecordNo(@Param("recordNo") String recordNo);
    HealthRecord selectByElderIdActive(@Param("elderId") Long elderId);
    int insert(HealthRecord healthRecord);
    int update(HealthRecord healthRecord);
    int deleteById(@Param("id") Long id);
    Long countAll();
    Long countByElderId(@Param("elderId") Long elderId);
    Long countByStatus(@Param("status") String status);
    Long countByBloodType(@Param("bloodType") String bloodType);
}
