package com.nursinghome.mapper;

import com.nursinghome.entity.EmergencyEvent;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EmergencyEventMapper {
    List<EmergencyEvent> selectAll();
    List<EmergencyEvent> selectByStatus(@Param("status") String status);
    List<EmergencyEvent> selectByEventType(@Param("eventType") String eventType);
    List<EmergencyEvent> selectBySeverity(@Param("severity") String severity);
    List<EmergencyEvent> selectByElderId(@Param("elderId") Long elderId);
    EmergencyEvent selectById(@Param("id") Long id);
    EmergencyEvent selectByEventNo(@Param("eventNo") String eventNo);
    int insert(EmergencyEvent emergencyEvent);
    int update(EmergencyEvent emergencyEvent);
    int deleteById(@Param("id") Long id);
    Long countAll();
    Long countByStatus(@Param("status") String status);
}
