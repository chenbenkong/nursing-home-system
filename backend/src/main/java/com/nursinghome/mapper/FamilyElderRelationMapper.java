package com.nursinghome.mapper;

import com.nursinghome.entity.FamilyElderRelation;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 家属账户关联Mapper接口
 */
@Mapper
public interface FamilyElderRelationMapper {
    
    @Select("<script>" +
            "SELECT r.*, u.username as familyUsername, u.real_name as familyRealName, u.phone as familyPhone, " +
            "e.name as elderName, e.bed_no as elderBedNo " +
            "FROM family_elder_relation r " +
            "LEFT JOIN sys_user u ON r.family_user_id = u.id " +
            "LEFT JOIN elder e ON r.elder_id = e.id " +
            "WHERE 1=1 " +
            "<if test='elderName != null'> AND e.name LIKE CONCAT('%',#{elderName},'%') </if>" +
            "<if test='familyName != null'> AND (u.real_name LIKE CONCAT('%',#{familyName},'%') OR u.username LIKE CONCAT('%',#{familyName},'%')) </if>" +
            "<if test='relation != null'> AND r.relation = #{relation} </if>" +
            "<if test='status != null'> AND r.status = #{status} </if>" +
            "ORDER BY r.create_time DESC" +
            "</script>")
    List<FamilyElderRelation> selectList(@Param("elderName") String elderName, 
                                          @Param("familyName") String familyName,
                                          @Param("relation") String relation,
                                          @Param("status") Integer status);
    
    @Select("SELECT r.*, u.username as familyUsername, u.real_name as familyRealName, u.phone as familyPhone, " +
            "e.name as elderName, e.bed_no as elderBedNo " +
            "FROM family_elder_relation r " +
            "LEFT JOIN sys_user u ON r.family_user_id = u.id " +
            "LEFT JOIN elder e ON r.elder_id = e.id " +
            "WHERE r.id = #{id}")
    FamilyElderRelation selectById(Long id);
    
    @Select("SELECT r.*, u.username as familyUsername, u.real_name as familyRealName, u.phone as familyPhone, " +
            "e.name as elderName, e.bed_no as elderBedNo " +
            "FROM family_elder_relation r " +
            "LEFT JOIN sys_user u ON r.family_user_id = u.id " +
            "LEFT JOIN elder e ON r.elder_id = e.id " +
            "WHERE r.elder_id = #{elderId} AND r.status = 1")
    List<FamilyElderRelation> selectByElderId(Long elderId);
    
    @Insert("INSERT INTO family_elder_relation (family_user_id, elder_id, relation, is_primary, " +
            "can_view_health, can_view_fee, can_view_location, status, create_time, update_time) " +
            "VALUES (#{familyUserId}, #{elderId}, #{relation}, #{isPrimary}, " +
            "#{canViewHealth}, #{canViewFee}, #{canViewLocation}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FamilyElderRelation relation);
    
    @Update("UPDATE family_elder_relation SET " +
            "relation = #{relation}, is_primary = #{isPrimary}, " +
            "can_view_health = #{canViewHealth}, can_view_fee = #{canViewFee}, " +
            "can_view_location = #{canViewLocation}, status = #{status}, update_time = NOW() " +
            "WHERE id = #{id}")
    int update(FamilyElderRelation relation);
    
    @Delete("DELETE FROM family_elder_relation WHERE id = #{id}")
    int deleteById(Long id);
    
    @Select("SELECT COUNT(*) FROM family_elder_relation WHERE status = 1")
    int countActive();
    
    @Select("SELECT COUNT(*) FROM family_elder_relation WHERE elder_id = #{elderId} AND r.status = 1")
    int countByElderId(Long elderId);
}
