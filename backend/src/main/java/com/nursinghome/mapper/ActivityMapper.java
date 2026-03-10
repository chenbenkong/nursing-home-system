package com.nursinghome.mapper;

import com.nursinghome.entity.Activity;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ActivityMapper {
    List<Activity> selectAll();
    List<Activity> selectByStatus(@Param("status") String status);
    List<Activity> selectByActivityType(@Param("activityType") String activityType);
    List<Activity> selectByOrganizerId(@Param("organizerId") Long organizerId);
    Activity selectById(@Param("id") Long id);
    Activity selectByActivityNo(@Param("activityNo") String activityNo);
    int insert(Activity activity);
    int update(Activity activity);
    int deleteById(@Param("id") Long id);
    int incrementCurrentParticipants(@Param("id") Long id);
    int decrementCurrentParticipants(@Param("id") Long id);
    Long countAll();
    Long countByStatus(@Param("status") String status);
}
