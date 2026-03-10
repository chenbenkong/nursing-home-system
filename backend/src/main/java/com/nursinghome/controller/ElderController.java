package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.Elder;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.entity.Room;
import com.nursinghome.mapper.ElderMapper;
import com.nursinghome.mapper.RoomMapper;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老人Controller
 */
@RestController
@RequestMapping("/elder")
public class ElderController {

    @Autowired
    private ElderMapper elderMapper;
    
    @Autowired
    private RoomMapper roomMapper;

    /**
     * 获取老人列表（分页）
     */
    @GetMapping("/list")
    public Result getElderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String idCard,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String careLevel,
            @RequestParam(required = false) String status) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<Elder> list;
        
        // 如果有任何查询条件，使用条件查询
        if ((name != null && !name.isEmpty()) ||
            (idCard != null && !idCard.isEmpty()) ||
            (phone != null && !phone.isEmpty()) ||
            (careLevel != null && !careLevel.isEmpty()) ||
            (status != null && !status.isEmpty())) {
            list = elderMapper.selectByCondition(name, idCard, phone, careLevel, status);
        } else {
            list = elderMapper.selectAll();
        }
        
        PageInfo<Elder> pageInfo = new PageInfo<>(list);
        
        PageResult<Elder> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        
        return Result.success(pageResult);
    }

    /**
     * 获取所有老人
     */
    @GetMapping("/all")
    public Result getAllElders() {
        List<Elder> list = elderMapper.selectAll();
        return Result.success(list);
    }
    
    /**
     * 获取已入住的老人列表
     */
    @GetMapping("/checked-in")
    public Result getCheckedInElders() {
        List<Elder> list = elderMapper.selectByStatus(Elder.STATUS_CHECKED_IN);
        return Result.success(list);
    }

    /**
     * 根据ID获取老人
     */
    @GetMapping("/{id}")
    public Result getElderById(@PathVariable Long id) {
        Elder elder = elderMapper.selectById(id);
        if (elder == null) {
            return Result.error("老人不存在");
        }
        return Result.success(elder);
    }

    /**
     * 添加老人
     */
    @PostMapping
    public Result addElder(@RequestBody Elder elder) {
        // 检查身份证号是否已存在
        if (elder.getIdCard() != null && !elder.getIdCard().isEmpty()) {
            Elder existElder = elderMapper.selectByIdCard(elder.getIdCard());
            if (existElder != null) {
                return Result.error("身份证号已存在");
            }
        }
        
        // 生成老人编号
        elder.setElderNo(CodeGenerator.generateElderNo());
        
        // 计算年龄
        if (elder.getBirthDate() != null) {
            elder.setAge(Period.between(elder.getBirthDate(), LocalDate.now()).getYears());
        }
        
        // 设置默认状态
        if (elder.getStatus() == null) {
            elder.setStatus(Elder.STATUS_PENDING);
        }
        
        elderMapper.insert(elder);
        return Result.success("添加成功", elder);
    }

    /**
     * 更新老人
     */
    @PutMapping
    @Transactional
    public Result updateElder(@RequestBody Elder elder) {
        Elder existElder = elderMapper.selectById(elder.getId());
        if (existElder == null) {
            return Result.error("老人不存在");
        }
        
        // 如果修改了身份证号，检查是否与其他老人重复
        if (elder.getIdCard() != null && !elder.getIdCard().isEmpty() 
                && !elder.getIdCard().equals(existElder.getIdCard())) {
            Elder duplicateElder = elderMapper.selectByIdCard(elder.getIdCard());
            if (duplicateElder != null && !duplicateElder.getId().equals(elder.getId())) {
                return Result.error("身份证号已存在");
            }
        }
        
        // 重新计算年龄
        if (elder.getBirthDate() != null) {
            elder.setAge(Period.between(elder.getBirthDate(), LocalDate.now()).getYears());
        }
        
        // 状态变更校验
        if (elder.getStatus() != null && !elder.getStatus().equals(existElder.getStatus())) {
            String oldStatus = existElder.getStatus();
            String newStatus = elder.getStatus();
            
            // 未入住状态不能直接变为已入住（必须通过办理入住流程）
            if (Elder.STATUS_PENDING.equals(oldStatus) && Elder.STATUS_CHECKED_IN.equals(newStatus)) {
                return Result.error("未入住的老人不能直接改为已入住，请先通过入住申请审批流程");
            }
            
            // 已退住状态不能变回已入住
            if (Elder.STATUS_CHECKED_OUT.equals(oldStatus) && Elder.STATUS_CHECKED_IN.equals(newStatus)) {
                return Result.error("已退住的老人不能再次入住，请重新添加老人信息");
            }
            
            // 已入住状态不能直接变为未入住
            if (Elder.STATUS_CHECKED_IN.equals(oldStatus) && Elder.STATUS_PENDING.equals(newStatus)) {
                return Result.error("已入住的老人不能直接变为未入住状态，请先办理退住");
            }
        }
        
        // 已入住状态必须有房间
        if (Elder.STATUS_CHECKED_IN.equals(elder.getStatus()) && elder.getRoomId() == null) {
            return Result.error("已入住的老人必须分配房间");
        }
        
        // 处理换房逻辑：如果房间变更，更新房间占用情况
        Long oldRoomId = existElder.getRoomId();
        Long newRoomId = elder.getRoomId();
        
        if (newRoomId != null && !newRoomId.equals(oldRoomId)) {
            // 1. 减少旧房间的占用数
            if (oldRoomId != null) {
                Room oldRoom = roomMapper.selectById(oldRoomId);
                if (oldRoom != null && oldRoom.getOccupiedBeds() > 0) {
                    int newOccupiedBeds = oldRoom.getOccupiedBeds() - 1;
                    String newStatus = Room.STATUS_AVAILABLE;
                    roomMapper.updateOccupiedBeds(oldRoomId, newOccupiedBeds, newStatus);
                }
            }
            
            // 2. 增加新房间的占用数
            Room newRoom = roomMapper.selectById(newRoomId);
            if (newRoom != null) {
                if (newRoom.getOccupiedBeds() >= newRoom.getBedCount()) {
                    return Result.error("新房间已满，请选择其他房间");
                }
                int newOccupiedBeds = newRoom.getOccupiedBeds() + 1;
                String newStatus;
                if (newOccupiedBeds >= newRoom.getBedCount()) {
                    newStatus = Room.STATUS_OCCUPIED;
                } else {
                    newStatus = Room.STATUS_AVAILABLE;
                }
                roomMapper.updateOccupiedBeds(newRoomId, newOccupiedBeds, newStatus);
            }
        }
        
        elderMapper.update(elder);
        return Result.success("更新成功", null);
    }

    /**
     * 删除老人
     */
    @DeleteMapping("/{id}")
    @Transactional
    public Result deleteElder(@PathVariable Long id) {
        Elder elder = elderMapper.selectById(id);
        if (elder == null) {
            return Result.error("老人不存在");
        }
        
        // 检查老人状态，只有待入住或已退住的老人才能删除
        if (Elder.STATUS_CHECKED_IN.equals(elder.getStatus())) {
            return Result.error("已入住的老人不能删除，请先办理退住");
        }
        if (Elder.STATUS_SUSPENDED.equals(elder.getStatus())) {
            return Result.error("暂停状态的老人不能删除");
        }
        
        elderMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 办理入住
     */
    @PutMapping("/{id}/checkin")
    @Transactional
    public Result checkIn(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Elder elder = elderMapper.selectById(id);
        if (elder == null) {
            return Result.error("老人不存在");
        }
        
        // 状态校验：只允许待入住状态办理入住
        if (!Elder.STATUS_PENDING.equals(elder.getStatus())) {
            if (Elder.STATUS_CHECKED_IN.equals(elder.getStatus())) {
                return Result.error("该老人已入住，不能重复办理");
            }
            if (Elder.STATUS_CHECKED_OUT.equals(elder.getStatus())) {
                return Result.error("已退住的老人不能再次办理入住，请重新添加老人信息");
            }
            if (Elder.STATUS_SUSPENDED.equals(elder.getStatus())) {
                return Result.error("暂停状态的老人不能办理入住");
            }
        }
        
        Long roomId = null;
        if (params.get("roomId") != null) {
            roomId = Long.valueOf(params.get("roomId").toString());
        }
        
        // 如果选择了房间，更新房间占用情况
        if (roomId != null) {
            Room room = roomMapper.selectById(roomId);
            if (room == null) {
                return Result.error("房间不存在");
            }
            if (room.getOccupiedBeds() >= room.getBedCount()) {
                return Result.error("该房间已满，请选择其他房间");
            }
            
            // 更新房间占用情况
            int newOccupiedBeds = room.getOccupiedBeds() + 1;
            String newStatus;
            if (newOccupiedBeds >= room.getBedCount()) {
                newStatus = Room.STATUS_OCCUPIED;
            } else {
                newStatus = Room.STATUS_AVAILABLE;
            }
            roomMapper.updateOccupiedBeds(roomId, newOccupiedBeds, newStatus);
        }
        
        Elder updateElder = new Elder();
        updateElder.setId(id);
        updateElder.setStatus(Elder.STATUS_CHECKED_IN);
        updateElder.setCheckInDate(LocalDate.now());
        updateElder.setRoomId(roomId);
        
        if (params.get("bedNo") != null) {
            updateElder.setBedNo(params.get("bedNo").toString());
        }
        
        elderMapper.update(updateElder);
        return Result.success("入住办理成功", null);
    }

    /**
     * 办理退住
     */
    @PutMapping("/{id}/checkout")
    @Transactional
    public Result checkOut(@PathVariable Long id) {
        Elder elder = elderMapper.selectById(id);
        if (elder == null) {
            return Result.error("老人不存在");
        }
        
        // 状态校验：只允许已入住状态办理退住
        if (!Elder.STATUS_CHECKED_IN.equals(elder.getStatus())) {
            if (Elder.STATUS_PENDING.equals(elder.getStatus())) {
                return Result.error("未入住的老人不能办理退住");
            }
            if (Elder.STATUS_CHECKED_OUT.equals(elder.getStatus())) {
                return Result.error("该老人已退住，不能重复办理");
            }
            if (Elder.STATUS_SUSPENDED.equals(elder.getStatus())) {
                return Result.error("暂停状态的老人不能办理退住");
            }
        }
        
        // 如果老人有房间，更新房间占用情况
        Long roomId = elder.getRoomId();
        if (roomId != null) {
            Room room = roomMapper.selectById(roomId);
            if (room != null && room.getOccupiedBeds() > 0) {
                int newOccupiedBeds = room.getOccupiedBeds() - 1;
                String newStatus = Room.STATUS_AVAILABLE;
                roomMapper.updateOccupiedBeds(roomId, newOccupiedBeds, newStatus);
            }
        }
        
        Elder updateElder = new Elder();
        updateElder.setId(id);
        updateElder.setStatus(Elder.STATUS_CHECKED_OUT);
        updateElder.setCheckOutDate(LocalDate.now());
        
        elderMapper.update(updateElder);
        return Result.success("退住办理成功", null);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        Long total = elderMapper.countAll();
        Long checkedIn = elderMapper.countByStatus(Elder.STATUS_CHECKED_IN);
        Long pending = elderMapper.countByStatus(Elder.STATUS_PENDING);
        Long checkedOut = elderMapper.countByStatus(Elder.STATUS_CHECKED_OUT);
        
        stats.put("total", total != null ? total : 0);
        stats.put("checkedIn", checkedIn != null ? checkedIn : 0);
        stats.put("pending", pending != null ? pending : 0);
        stats.put("checkedOut", checkedOut != null ? checkedOut : 0);
        
        return Result.success(stats);
    }
}
