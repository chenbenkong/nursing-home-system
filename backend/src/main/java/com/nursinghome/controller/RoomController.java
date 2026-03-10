package com.nursinghome.controller;

import com.nursinghome.entity.Elder;
import com.nursinghome.entity.Result;
import com.nursinghome.entity.Room;
import com.nursinghome.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 房间管理控制器
 */
@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomMapper roomMapper;

    /**
     * 获取房间列表（支持分页和搜索）
     */
    @GetMapping("/list")
    public Result getRoomList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) String status) {
        // 获取所有房间
        List<Room> rooms = roomMapper.selectAll();
        
        // 根据搜索条件过滤
        if (roomNumber != null && !roomNumber.isEmpty()) {
            String keyword = roomNumber.toLowerCase();
            rooms = rooms.stream()
                .filter(r -> r.getRoomNo() != null && r.getRoomNo().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        }
        
        if (floor != null) {
            rooms = rooms.stream()
                .filter(r -> r.getFloor() != null && r.getFloor().equals(floor))
                .collect(Collectors.toList());
        }
        
        if (roomType != null && !roomType.isEmpty()) {
            rooms = rooms.stream()
                .filter(r -> r.getRoomType() != null && r.getRoomType().equals(roomType))
                .collect(Collectors.toList());
        }
        
        if (status != null && !status.isEmpty()) {
            rooms = rooms.stream()
                .filter(r -> r.getStatus() != null && r.getStatus().equals(status))
                .collect(Collectors.toList());
        }
        
        // 为每个房间添加入住老人信息
        for (Room room : rooms) {
            List<Elder> elders = roomMapper.selectEldersByRoomId(room.getId());
            room.setElders(elders);
        }
        
        return Result.success(rooms);
    }

    /**
     * 获取所有房间
     */
    @GetMapping("/all")
    public Result getAllRooms() {
        List<Room> rooms = roomMapper.selectAll();
        return Result.success(rooms);
    }

    /**
     * 获取可用房间（有空闲床位）
     */
    @GetMapping("/available")
    public Result getAvailableRooms() {
        List<Room> rooms = roomMapper.selectAvailableRooms();
        return Result.success(rooms);
    }

    /**
     * 根据ID获取房间（包含入住老人信息）
     */
    @GetMapping("/{id}")
    public Result getRoomById(@PathVariable Long id) {
        Room room = roomMapper.selectById(id);
        if (room == null) {
            return Result.error("房间不存在");
        }
        
        // 查询房间入住老人
        List<Elder> elders = roomMapper.selectEldersByRoomId(id);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("id", room.getId());
        result.put("roomNo", room.getRoomNo());
        result.put("roomType", room.getRoomType());
        result.put("floor", room.getFloor());
        result.put("area", room.getArea());
        result.put("bedCount", room.getBedCount());
        result.put("occupiedBeds", room.getOccupiedBeds());
        result.put("price", room.getPrice());
        result.put("facilities", room.getFacilities());
        result.put("status", room.getStatus());
        result.put("remark", room.getRemark());
        result.put("elders", elders);
        
        return Result.success(result);
    }

    /**
     * 新增房间
     */
    @PostMapping
    public Result addRoom(@RequestBody Room room) {
        roomMapper.insert(room);
        return Result.success(room);
    }

    /**
     * 更新房间
     */
    @PutMapping
    public Result updateRoom(@RequestBody Room room) {
        Room existRoom = roomMapper.selectById(room.getId());
        if (existRoom == null) {
            return Result.error("房间不存在");
        }
        
        // 状态变更校验
        if (room.getStatus() != null && !room.getStatus().equals(existRoom.getStatus())) {
            String oldStatus = existRoom.getStatus();
            String newStatus = room.getStatus();
            int occupiedBeds = existRoom.getOccupiedBeds();
            
            // 如果有老人入住，不能改为空闲或维修中
            if (occupiedBeds > 0) {
                if (Room.STATUS_AVAILABLE.equals(newStatus)) {
                    return Result.error("该房间有" + occupiedBeds + "人入住，不能改为空闲状态");
                }
                if (Room.STATUS_MAINTENANCE.equals(newStatus)) {
                    return Result.error("该房间有" + occupiedBeds + "人入住，不能改为维修中状态");
                }
            }
            
            // 已入住状态不能改为空闲
            if (Room.STATUS_OCCUPIED.equals(oldStatus) && Room.STATUS_AVAILABLE.equals(newStatus)) {
                return Result.error("房间已满，不能改为空闲状态");
            }
        }
        
        roomMapper.update(room);
        return Result.success(room);
    }

    /**
     * 删除房间
     */
    @DeleteMapping("/{id}")
    public Result deleteRoom(@PathVariable Long id) {
        roomMapper.deleteById(id);
        return Result.success();
    }
}
