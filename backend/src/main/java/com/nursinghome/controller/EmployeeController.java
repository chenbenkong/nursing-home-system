package com.nursinghome.controller;

import com.nursinghome.common.BaseController;
import com.nursinghome.entity.Employee;
import com.nursinghome.entity.Result;
import com.nursinghome.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工Controller
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseController<Employee, EmployeeService> {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 根据部门获取员工
     */
    @GetMapping("/department/{department}")
    public Result getByDepartment(@PathVariable String department) {
        List<Employee> list = employeeService.findByDepartment(department);
        return Result.success(list);
    }

    /**
     * 员工离职
     */
    @PutMapping("/{id}/resign")
    public Result resign(@PathVariable Long id) {
        employeeService.resign(id);
        return Result.success("已办理离职", null);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        return Result.success(employeeService.getStatistics());
    }
}
