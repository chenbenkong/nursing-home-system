package com.nursinghome.mapper;

import com.nursinghome.entity.SysConfig;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 系统配置Mapper接口
 */
@Mapper
public interface SysConfigMapper {
    
    @Select("SELECT * FROM sys_config ORDER BY is_system DESC, create_time DESC")
    List<SysConfig> selectAll();
    
    @Select("SELECT * FROM sys_config WHERE is_system = 0 ORDER BY create_time DESC")
    List<SysConfig> selectCustom();
    
    @Select("SELECT * FROM sys_config WHERE id = #{id}")
    SysConfig selectById(Long id);
    
    @Select("SELECT * FROM sys_config WHERE config_key = #{configKey}")
    SysConfig selectByKey(String configKey);
    
    @Insert("INSERT INTO sys_config (config_key, config_value, config_type, description, is_system, create_time, update_time) " +
            "VALUES (#{configKey}, #{configValue}, #{configType}, #{description}, #{isSystem}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysConfig config);
    
    @Update("UPDATE sys_config SET " +
            "config_value = #{configValue}, config_type = #{configType}, " +
            "description = #{description}, update_time = NOW() " +
            "WHERE id = #{id}")
    int update(SysConfig config);
    
    @Delete("DELETE FROM sys_config WHERE id = #{id} AND is_system = 0")
    int deleteById(Long id);
    
    @Select("SELECT COUNT(*) FROM sys_config")
    int countAll();
    
    @Select("SELECT COUNT(*) FROM sys_config WHERE is_system = 1")
    int countSystem();
}
