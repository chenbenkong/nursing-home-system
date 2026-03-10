package com.nursinghome.mapper;

import com.nursinghome.entity.Elder;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 老人Mapper接口
 */
public interface ElderMapper {
    
    Elder selectById(Long id);

    Elder selectByElderNo(String elderNo);

    Elder selectByIdCard(@Param("idCard") String idCard);

    /**
     * 根据姓名和手机号查询老人
     */
    List<Elder> selectByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

    List<Elder> selectAll();
    
    List<Elder> selectByStatus(@Param("status") String status);
    
    List<Elder> selectByRoomId(@Param("roomId") Long roomId);
    
    /**
     * 根据条件搜索老人
     */
    List<Elder> selectByCondition(@Param("name") String name,
                                   @Param("idCard") String idCard,
                                   @Param("phone") String phone,
                                   @Param("careLevel") String careLevel,
                                   @Param("status") String status);
    
    int insert(Elder elder);
    
    int update(Elder elder);
    
    int deleteById(Long id);
    
    Long countByStatus(@Param("status") String status);
    
    Long countAll();
}
