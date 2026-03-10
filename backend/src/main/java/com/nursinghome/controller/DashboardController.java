package com.nursinghome.controller;

import com.nursinghome.entity.Elder;
import com.nursinghome.entity.Employee;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.CheckInApplicationMapper;
import com.nursinghome.mapper.ElderMapper;
import com.nursinghome.mapper.EmployeeMapper;
import com.nursinghome.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 仪表盘Controller
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ElderMapper elderMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CheckInApplicationMapper applicationMapper;

    @Autowired
    private RoomMapper roomMapper;

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 老人统计
        Long elderTotal = elderMapper.countAll();
        Long elderCheckedIn = elderMapper.countByStatus(Elder.STATUS_CHECKED_IN);
        Long elderPending = elderMapper.countByStatus(Elder.STATUS_PENDING);
        
        // 员工统计
        Long employeeTotal = employeeMapper.countByStatus(null);
        Long employeeActive = employeeMapper.countByStatus(Employee.STATUS_ACTIVE);
        Long nursingStaff = employeeMapper.countByDepartment(Employee.DEPT_NURSING);
        
        // 房间统计
        Long roomTotal = roomMapper.countAll();
        Long roomAvailable = roomMapper.countAvailable();
        
        // 待审批申请数
        Long pendingApplications = applicationMapper.countByStatus("PENDING");
        
        stats.put("elderTotal", elderTotal != null ? elderTotal : 0);
        stats.put("elderCheckedIn", elderCheckedIn != null ? elderCheckedIn : 0);
        stats.put("elderPending", elderPending != null ? elderPending : 0);
        stats.put("employeeTotal", employeeTotal != null ? employeeTotal : 0);
        stats.put("employeeActive", employeeActive != null ? employeeActive : 0);
        stats.put("nursingStaff", nursingStaff != null ? nursingStaff : 0);
        stats.put("roomTotal", roomTotal != null ? roomTotal : 0);
        stats.put("roomAvailable", roomAvailable != null ? roomAvailable : 0);
        stats.put("pendingApplications", pendingApplications != null ? pendingApplications : 0);
        
        return Result.success(stats);
    }
}
