package com.nursinghome.mapper;

import com.nursinghome.entity.PhysicalExam;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

public interface PhysicalExamMapper {
    List<PhysicalExam> selectAll();
    List<PhysicalExam> selectByElderId(@Param("elderId") Long elderId);
    List<PhysicalExam> selectByExamType(@Param("examType") String examType);
    List<PhysicalExam> selectByExamResult(@Param("examResult") String examResult);
    List<PhysicalExam> selectByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    PhysicalExam selectById(@Param("id") Long id);
    PhysicalExam selectByRecordNo(@Param("recordNo") String recordNo);
    int insert(PhysicalExam physicalExam);
    int update(PhysicalExam physicalExam);
    int deleteById(@Param("id") Long id);
    Long countAll();
    Long countByElderId(@Param("elderId") Long elderId);
    Long countByExamType(@Param("examType") String examType);
    Long countByExamResult(@Param("examResult") String examResult);
    Long countByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
