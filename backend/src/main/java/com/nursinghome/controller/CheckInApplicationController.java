package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.CheckInApplication;
import com.nursinghome.entity.Elder;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.entity.Room;
import com.nursinghome.mapper.CheckInApplicationMapper;
import com.nursinghome.mapper.ElderMapper;
import com.nursinghome.mapper.RoomMapper;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 入住申请Controller
 */
@RestController
@RequestMapping("/application")
public class CheckInApplicationController {

    @Autowired
    private CheckInApplicationMapper applicationMapper;
    
    @Autowired
    private ElderMapper elderMapper;
    
    @Autowired
    private RoomMapper roomMapper;

    /**
     * 获取申请列表（分页）
     */
    @GetMapping("/list")
    public Result getApplicationList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String elderName) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<CheckInApplication> list;
        
        // 如果有查询条件，使用条件查询
        if ((status != null && !status.isEmpty()) || (elderName != null && !elderName.isEmpty())) {
            list = applicationMapper.selectByCondition(status, elderName);
        } else {
            list = applicationMapper.selectAll();
        }
        
        PageInfo<CheckInApplication> pageInfo = new PageInfo<>(list);
        
        PageResult<CheckInApplication> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        
        return Result.success(pageResult);
    }

    /**
     * 获取所有申请
     */
    @GetMapping("/all")
    public Result getAllApplications() {
        List<CheckInApplication> list = applicationMapper.selectAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取申请
     */
    @GetMapping("/{id}")
    public Result getApplicationById(@PathVariable Long id) {
        CheckInApplication application = applicationMapper.selectById(id);
        if (application == null) {
            return Result.error("申请不存在");
        }
        return Result.success(application);
    }

    /**
     * 提交申请
     */
    @PostMapping
    public Result submitApplication(@RequestBody CheckInApplication application, HttpServletRequest request) {
        // 检查身份证号是否已存在
        if (application.getIdCard() != null && !application.getIdCard().isEmpty()) {
            Elder existElder = elderMapper.selectByIdCard(application.getIdCard());
            if (existElder != null) {
                return Result.error("该身份证号已存在，不能重复申请");
            }
        }
        
        // 生成申请编号
        application.setApplicationNo(CodeGenerator.generateApplicationNo());
        
        // 设置默认状态
        if (application.getStatus() == null) {
            application.setStatus(CheckInApplication.STATUS_PENDING);
        }
        
        // 设置申请人信息
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            application.setApplicantId(userId);
        }
        
        applicationMapper.insert(application);
        return Result.success("申请提交成功", application);
    }

    /**
     * 更新申请
     */
    @PutMapping
    public Result updateApplication(@RequestBody CheckInApplication application) {
        CheckInApplication existApp = applicationMapper.selectById(application.getId());
        if (existApp == null) {
            return Result.error("申请不存在");
        }
        
        // 只有待审批的申请才能修改
        if (!CheckInApplication.STATUS_PENDING.equals(existApp.getStatus())) {
            return Result.error("已审批的申请不能修改");
        }
        
        applicationMapper.update(application);
        return Result.success("更新成功", null);
    }

    /**
     * 删除申请
     */
    @DeleteMapping("/{id}")
    @Transactional
    public Result deleteApplication(@PathVariable Long id) {
        CheckInApplication existApp = applicationMapper.selectById(id);
        if (existApp == null) {
            return Result.error("申请不存在");
        }
        
        // 已审批通过的申请也可以删除，但需要先处理关联的老人和房间
        if (CheckInApplication.STATUS_APPROVED.equals(existApp.getStatus())) {
            // 如果申请已通过，需要删除关联的老人记录并释放房间
            if (existApp.getRoomId() != null) {
                // 根据房间ID和申请中的老人信息查找关联的老人
                Elder elder = elderMapper.selectByIdCard(existApp.getIdCard());
                if (elder != null) {
                    // 如果老人已入住，需要更新房间占用情况
                    if (Elder.STATUS_CHECKED_IN.equals(elder.getStatus()) && elder.getRoomId() != null) {
                        Room room = roomMapper.selectById(elder.getRoomId());
                        if (room != null && room.getOccupiedBeds() > 0) {
                            int newOccupiedBeds = room.getOccupiedBeds() - 1;
                            String newStatus = Room.STATUS_AVAILABLE;
                            roomMapper.updateOccupiedBeds(elder.getRoomId(), newOccupiedBeds, newStatus);
                        }
                    }
                    // 删除老人记录
                    elderMapper.deleteById(elder.getId());
                }
            }
        }
        
        applicationMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 审批通过
     */
    @PutMapping("/{id}/approve")
    @Transactional
    public Result approveApplication(@PathVariable Long id, 
                                     @RequestBody CheckInApplication application,
                                     HttpServletRequest request) {
        try {
            CheckInApplication existApp = applicationMapper.selectById(id);
            if (existApp == null) {
                return Result.error("申请不存在");
            }
            
            // 检查申请状态，只有待审批的申请才能审批
            if (!CheckInApplication.STATUS_PENDING.equals(existApp.getStatus())) {
                if (CheckInApplication.STATUS_APPROVED.equals(existApp.getStatus())) {
                    return Result.error("该申请已审批通过，不能重复审批");
                }
                if (CheckInApplication.STATUS_REJECTED.equals(existApp.getStatus())) {
                    return Result.error("该申请已审批拒绝，不能再次审批");
                }
                if (CheckInApplication.STATUS_CANCELLED.equals(existApp.getStatus())) {
                    return Result.error("该申请已取消，不能审批");
                }
            }
            
            Long userId = (Long) request.getAttribute("userId");
            
            // 获取房间床位信息
            Long roomId = application.getRoomId();
            String bedNo = application.getBedId(); // 这里bedId实际上是bedNo
            LocalDate checkInDate = application.getCheckInDate();
            
            // 验证房间和床位
            if (roomId == null || bedNo == null || bedNo.isEmpty()) {
                return Result.error("请选择房间和床位");
            }
            
            Room room = roomMapper.selectById(roomId);
            if (room == null) {
                return Result.error("房间不存在");
            }
            if (room.getOccupiedBeds() >= room.getBedCount()) {
                return Result.error("该房间已满，请选择其他房间");
            }
            
            // 检查老人是否已存在（根据姓名和手机号判断）
            Elder existingElder = null;
            if (existApp.getIdCard() != null && !existApp.getIdCard().trim().isEmpty()) {
                // 如果有身份证号，优先用身份证号查询
                existingElder = elderMapper.selectByIdCard(existApp.getIdCard());
            }
            // 如果没有找到，再用姓名和手机号查询
            if (existingElder == null) {
                List<Elder> eldersByNameAndPhone = elderMapper.selectByNameAndPhone(existApp.getElderName(), existApp.getPhone());
                if (eldersByNameAndPhone != null && !eldersByNameAndPhone.isEmpty()) {
                    existingElder = eldersByNameAndPhone.get(0);
                }
            }
            
            Elder elder;
            if (existingElder != null) {
                // 老人已存在，更新房间床位信息
                elder = existingElder;
                elder.setRoomId(roomId);
                elder.setBedNo(bedNo);
                elder.setCheckInDate(checkInDate != null ? checkInDate : LocalDate.now());
                elder.setStatus(Elder.STATUS_CHECKED_IN);
                elderMapper.update(elder);
            } else {
                // 老人不存在，创建新记录
                elder = new Elder();
                elder.setName(existApp.getElderName());
                elder.setGender(existApp.getGender());
                elder.setBirthDate(existApp.getBirthDate());
                // 只有当身份证号不为空时才设置，避免数据库唯一约束冲突
                if (existApp.getIdCard() != null && !existApp.getIdCard().trim().isEmpty()) {
                    elder.setIdCard(existApp.getIdCard());
                }
                elder.setPhone(existApp.getPhone());
                elder.setAddress(existApp.getAddress());
                elder.setContactName(existApp.getContactName());
                elder.setContactPhone(existApp.getContactPhone());
                elder.setContactRelation(existApp.getContactRelation());
                // 转换健康状况值为数据库ENUM值
                String healthStatus = convertHealthStatus(existApp.getHealthStatus());
                elder.setHealthStatus(healthStatus);
                elder.setCareLevel(existApp.getCareLevel());
                elder.setStatus(Elder.STATUS_CHECKED_IN); // 设置为已入住状态
                elder.setRoomId(roomId);  // 设置房间ID
                elder.setBedNo(bedNo);    // 设置床位号
                elder.setCheckInDate(checkInDate != null ? checkInDate : LocalDate.now()); // 设置入住日期
                
                // 计算年龄
                if (existApp.getBirthDate() != null) {
                    elder.setAge(java.time.Period.between(existApp.getBirthDate(), java.time.LocalDate.now()).getYears());
                }
                
                // 生成老人编号（使用时间戳+随机数确保唯一）
                String elderNo = generateUniqueElderNo();
                elder.setElderNo(elderNo);
                
                elderMapper.insert(elder);
            }
            
            // 更新房间占用情况
            int newOccupiedBeds = room.getOccupiedBeds() + 1;
            String newStatus;
            if (newOccupiedBeds >= room.getBedCount()) {
                newStatus = Room.STATUS_OCCUPIED; // 已满
            } else {
                newStatus = Room.STATUS_AVAILABLE; // 仍可用（部分占用）
            }
            roomMapper.updateOccupiedBeds(roomId, newOccupiedBeds, newStatus);
            
            // 更新申请状态
            CheckInApplication updateApp = new CheckInApplication();
            updateApp.setId(id);
            updateApp.setStatus(CheckInApplication.STATUS_APPROVED);
            updateApp.setApproveTime(LocalDateTime.now());
            updateApp.setApproveBy(userId);
            updateApp.setApproveRemark(application.getApproveRemark());
            
            applicationMapper.update(updateApp);
            
            return Result.success("审批通过，已自动创建老人档案", elder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("审批失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 生成唯一的老人编号
     */
    private String generateUniqueElderNo() {
        String elderNo;
        int maxAttempts = 10;
        int attempts = 0;
        
        do {
            elderNo = CodeGenerator.generateElderNo();
            // 检查编号是否已存在
            Elder existElder = elderMapper.selectByElderNo(elderNo);
            if (existElder == null) {
                return elderNo;
            }
            attempts++;
        } while (attempts < maxAttempts);
        
        // 如果多次尝试都重复，使用时间戳确保唯一
        return "L" + System.currentTimeMillis();
    }
    
    /**
     * 转换健康状况值为数据库ENUM值
     */
    private String convertHealthStatus(String healthStatus) {
        if (healthStatus == null || healthStatus.isEmpty()) {
            return Elder.HEALTH_HEALTHY;
        }
        // 如果是已经是ENUM值，直接返回
        if (healthStatus.equals(Elder.HEALTH_HEALTHY) ||
            healthStatus.equals(Elder.HEALTH_SUBHEALTH) ||
            healthStatus.equals(Elder.HEALTH_CHRONIC) ||
            healthStatus.equals(Elder.HEALTH_DISABLED)) {
            return healthStatus;
        }
        // 转换中文值为ENUM值
        switch (healthStatus) {
            case "健康":
            case "良好":
                return Elder.HEALTH_HEALTHY;
            case "亚健康":
                return Elder.HEALTH_SUBHEALTH;
            case "慢性病":
            case "慢性":
                return Elder.HEALTH_CHRONIC;
            case "残疾":
            case "残障":
                return Elder.HEALTH_DISABLED;
            default:
                return Elder.HEALTH_HEALTHY;
        }
    }

    /**
     * 审批拒绝
     */
    @PutMapping("/{id}/reject")
    @Transactional
    public Result rejectApplication(@PathVariable Long id, 
                                    @RequestBody CheckInApplication application,
                                    HttpServletRequest request) {
        CheckInApplication existApp = applicationMapper.selectById(id);
        if (existApp == null) {
            return Result.error("申请不存在");
        }
        
        // 检查申请状态，只有待审批的申请才能审批
        if (!CheckInApplication.STATUS_PENDING.equals(existApp.getStatus())) {
            if (CheckInApplication.STATUS_APPROVED.equals(existApp.getStatus())) {
                return Result.error("该申请已审批通过，不能再次审批");
            }
            if (CheckInApplication.STATUS_REJECTED.equals(existApp.getStatus())) {
                return Result.error("该申请已审批拒绝，不能重复审批");
            }
            if (CheckInApplication.STATUS_CANCELLED.equals(existApp.getStatus())) {
                return Result.error("该申请已取消，不能审批");
            }
        }
        
        Long userId = (Long) request.getAttribute("userId");
        
        CheckInApplication updateApp = new CheckInApplication();
        updateApp.setId(id);
        updateApp.setStatus(CheckInApplication.STATUS_REJECTED);
        updateApp.setApproveTime(LocalDateTime.now());
        updateApp.setApproveBy(userId);
        updateApp.setApproveRemark(application.getApproveRemark());
        
        applicationMapper.update(updateApp);
        return Result.success("审批拒绝", null);
    }

    /**
     * 获取待审批数量
     */
    @GetMapping("/pending/count")
    public Result getPendingCount() {
        Long count = applicationMapper.countByStatus(CheckInApplication.STATUS_PENDING);
        return Result.success(count);
    }
}
