package com.nursinghome.mapper;

import com.nursinghome.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户Mapper接口
 */
public interface UserMapper {
    
    User selectById(Long id);
    
    User selectByUsername(String username);
    
    List<User> selectAll();
    
    List<User> selectByCondition(@Param("username") String username,
                                  @Param("realName") String realName,
                                  @Param("phone") String phone,
                                  @Param("role") String role,
                                  @Param("status") Integer status);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(Long id);
    
    int updateLastLoginTime(@Param("id") Long id);
}
