package com.nursinghome.mapper;

import com.nursinghome.entity.CheckInApplication;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 入住申请Mapper接口
 */
public interface CheckInApplicationMapper {
    
    CheckInApplication selectById(Long id);
    
    CheckInApplication selectByApplicationNo(String applicationNo);
    
    List<CheckInApplication> selectAll();
    
    List<CheckInApplication> selectByStatus(@Param("status") String status);
    
    List<CheckInApplication> selectByCondition(@Param("status") String status, @Param("elderName") String elderName);
    
    int insert(CheckInApplication application);
    
    int update(CheckInApplication application);
    
    int deleteById(Long id);
    
    Long countByStatus(@Param("status") String status);
}
