package com.nursinghome.mapper;

import com.nursinghome.entity.Elder;
import com.nursinghome.entity.Room;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 房间Mapper
 */
@Mapper
public interface RoomMapper {
    
    /**
     * 查询所有房间
     */
    @Select("SELECT * FROM room")
    List<Room> selectAll();
    
    /**
     * 根据ID查询房间
     */
    @Select("SELECT * FROM room WHERE id = #{id}")
    Room selectById(Long id);
    
    /**
     * 查询可用房间（有空闲床位）
     */
    @Select("SELECT * FROM room WHERE status != 'MAINTENANCE' AND occupied_beds < bed_count ORDER BY room_no")
    List<Room> selectAvailableRooms();
    
    /**
     * 更新房间
     */
    @Update("UPDATE room SET " +
            "room_no = #{roomNo}, " +
            "room_type = #{roomType}, " +
            "floor = #{floor}, " +
            "area = #{area}, " +
            "bed_count = #{bedCount}, " +
            "occupied_beds = #{occupiedBeds}, " +
            "price = #{price}, " +
            "facilities = #{facilities}, " +
            "status = #{status}, " +
            "remark = #{remark}, " +
            "update_time = NOW() " +
            "WHERE id = #{id}")
    int update(Room room);
    
    /**
     * 更新房间占用床位数
     */
    @Update("UPDATE room SET occupied_beds = #{occupiedBeds}, status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateOccupiedBeds(@Param("id") Long id, @Param("occupiedBeds") Integer occupiedBeds, @Param("status") String status);
    
    /**
     * 新增房间
     */
    @Insert("INSERT INTO room (room_no, room_type, floor, area, bed_count, occupied_beds, price, facilities, status, remark, create_time, update_time) " +
            "VALUES (#{roomNo}, #{roomType}, #{floor}, #{area}, #{bedCount}, 0, #{price}, #{facilities}, #{status}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Room room);
    
    /**
     * 删除房间
     */
    @Delete("DELETE FROM room WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 查询房间入住老人
     */
    List<Elder> selectEldersByRoomId(Long roomId);
    
    /**
     * 统计房间总数
     */
    @Select("SELECT COUNT(*) FROM room")
    Long countAll();
    
    /**
     * 统计可用房间数
     */
    @Select("SELECT COUNT(*) FROM room WHERE status = 'AVAILABLE'")
    Long countAvailable();
}
