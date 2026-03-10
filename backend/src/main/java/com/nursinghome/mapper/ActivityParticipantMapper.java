package com.nursinghome.mapper;

import com.nursinghome.entity.ActivityParticipant;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ActivityParticipantMapper {
    List<ActivityParticipant> selectAll();
    List<ActivityParticipant> selectByActivityId(@Param("activityId") Long activityId);
    List<ActivityParticipant> selectByElderId(@Param("elderId") Long elderId);
    List<ActivityParticipant> selectByStatus(@Param("status") String status);
    ActivityParticipant selectById(@Param("id") Long id);
    ActivityParticipant selectByActivityIdAndElderId(@Param("activityId") Long activityId, @Param("elderId") Long elderId);
    int insert(ActivityParticipant activityParticipant);
    int update(ActivityParticipant activityParticipant);
    int deleteById(@Param("id") Long id);
    int deleteByActivityId(@Param("activityId") Long activityId);
    Long countAll();
    Long countByActivityId(@Param("activityId") Long activityId);
    Long countByElderId(@Param("elderId") Long elderId);
}
