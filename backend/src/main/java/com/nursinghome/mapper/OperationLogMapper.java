package com.nursinghome.mapper;

import com.nursinghome.entity.OperationLog;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface OperationLogMapper {

    @Insert("INSERT INTO operation_log (user_id, username, operation_type, operation_module, operation_desc, " +
            "request_method, request_url, request_params, response_data, ip_address, user_agent, " +
            "execution_time, status, error_msg, create_time) " +
            "VALUES (#{userId}, #{username}, #{operationType}, #{operationModule}, #{operationDesc}, " +
            "#{requestMethod}, #{requestUrl}, #{requestParams}, #{responseData}, #{ipAddress}, #{userAgent}, " +
            "#{executionTime}, #{status}, #{errorMsg}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OperationLog log);

    @Select("<script>" +
            "SELECT * FROM operation_log WHERE 1=1 " +
            "<if test='username != null'> AND username LIKE CONCAT('%',#{username},'%') </if>" +
            "<if test='operationType != null'> AND operation_type = #{operationType} </if>" +
            "<if test='operationModule != null'> AND operation_module = #{operationModule} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    List<OperationLog> selectList(@Param("username") String username,
                                   @Param("operationType") String operationType,
                                   @Param("operationModule") String operationModule,
                                   @Param("status") String status,
                                   @Param("startTime") String startTime,
                                   @Param("endTime") String endTime);

    @Select("SELECT * FROM operation_log WHERE id = #{id}")
    OperationLog selectById(Long id);

    @Delete("DELETE FROM operation_log WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM operation_log WHERE create_time &lt; DATE_SUB(NOW(), INTERVAL #{days} DAY)")
    int deleteByTime(@Param("days") int days);

    @Select("SELECT COUNT(*) FROM operation_log WHERE DATE(create_time) = CURDATE()")
    int countToday();

    @Select("SELECT COUNT(*) FROM operation_log WHERE status = 'FAIL' AND DATE(create_time) = CURDATE()")
    int countTodayFail();

    @Select("SELECT operation_module as type, COUNT(*) as count, error_msg as reason FROM operation_log " +
            "WHERE status = 'FAIL' GROUP BY operation_module, error_msg ORDER BY count DESC")
    List<Map<String, Object>> countFailByType();

    @Select("SELECT operation_module as type, COUNT(*) as count, error_msg as reason FROM operation_log " +
            "WHERE status = 'FAIL' AND create_time &gt;= #{startTime} AND create_time &lt;= #{endTime} " +
            "GROUP BY operation_module, error_msg ORDER BY count DESC")
    List<Map<String, Object>> countFailByTypeWithTime(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
