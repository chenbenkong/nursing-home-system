package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.CarePlan;
import com.nursinghome.entity.Employee;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.CarePlanMapper;
import com.nursinghome.mapper.EmployeeMapper;
import com.nursinghome.mapper.UserMapper;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工Controller
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private CarePlanMapper carePlanMapper;
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取员工列表（分页）
     */
    @GetMapping("/list")
    public Result getEmployeeList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list;
        
        // 如果有任何查询条件，使用条件查询
        if ((name != null && !name.isEmpty()) || 
            (department != null && !department.isEmpty()) || 
            (status != null && !status.isEmpty())) {
            list = employeeMapper.selectByCondition(name, department, status);
        } else {
            list = employeeMapper.selectAll();
        }
        
        PageInfo<Employee> pageInfo = new PageInfo<>(list);
        
        PageResult<Employee> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        
        return Result.success(pageResult);
    }

    /**
     * 获取所有员工
     */
    @GetMapping("/all")
    public Result getAllEmployees() {
        List<Employee> list = employeeMapper.selectAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取员工
     */
    @GetMapping("/{id}")
    public Result getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(employee);
    }

    /**
     * 根据部门获取员工
     */
    @GetMapping("/department/{department}")
    public Result getEmployeesByDepartment(@PathVariable String department) {
        List<Employee> list = employeeMapper.selectByDepartment(department);
        return Result.success(list);
    }

    /**
     * 添加员工
     */
    @PostMapping
    public Result addEmployee(@RequestBody Employee employee) {
        // 检查身份证号是否已存在
        if (employee.getIdCard() != null && !employee.getIdCard().isEmpty()) {
            Employee existEmp = employeeMapper.selectByIdCard(employee.getIdCard());
            if (existEmp != null) {
                return Result.error("身份证号已存在");
            }
        }
        
        // 生成员工编号
        employee.setEmpNo(CodeGenerator.generateEmpNo());
        
        // 设置默认状态
        if (employee.getStatus() == null) {
            employee.setStatus(Employee.STATUS_ACTIVE);
        }
        
        employeeMapper.insert(employee);
        return Result.success("添加成功", employee);
    }

    /**
     * 更新员工
     */
    @PutMapping
    public Result updateEmployee(@RequestBody Employee employee) {
        Employee existEmp = employeeMapper.selectById(employee.getId());
        if (existEmp == null) {
            return Result.error("员工不存在");
        }
        
        // 如果修改了身份证号，检查是否与其他员工重复
        if (employee.getIdCard() != null && !employee.getIdCard().isEmpty() 
                && !employee.getIdCard().equals(existEmp.getIdCard())) {
            Employee duplicateEmp = employeeMapper.selectByIdCard(employee.getIdCard());
            if (duplicateEmp != null && !duplicateEmp.getId().equals(employee.getId())) {
                return Result.error("身份证号已存在");
            }
        }
        
        employeeMapper.update(employee);
        return Result.success("更新成功", null);
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    @Transactional
    public Result deleteEmployee(@PathVariable Long id) {
        Employee existEmp = employeeMapper.selectById(id);
        if (existEmp == null) {
            return Result.error("员工不存在");
        }
        
        // 检查是否存在关联的护理计划（作为负责护士）
        List<CarePlan> carePlans = carePlanMapper.selectAll();
        boolean hasCarePlan = carePlans.stream()
                .anyMatch(plan -> id.equals(plan.getNurseId()));
        if (hasCarePlan) {
            return Result.error("该员工存在关联的护理计划，请先处理护理计划");
        }
        
        // 检查是否有关联的系统用户账号
        if (existEmp.getUserId() != null) {
            return Result.error("该员工已绑定系统用户账号，请先解除绑定");
        }
        
        employeeMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 员工离职
     */
    @PutMapping("/{id}/resign")
    @Transactional
    public Result resignEmployee(@PathVariable Long id) {
        Employee existEmp = employeeMapper.selectById(id);
        if (existEmp == null) {
            return Result.error("员工不存在");
        }
        
        // 检查员工状态
        if (Employee.STATUS_RESIGNED.equals(existEmp.getStatus())) {
            return Result.error("该员工已离职，不能重复办理");
        }
        
        // 检查是否存在进行中的护理计划
        List<CarePlan> carePlans = carePlanMapper.selectAll();
        boolean hasActivePlan = carePlans.stream()
                .filter(plan -> id.equals(plan.getNurseId()))
                .anyMatch(plan -> CarePlan.STATUS_ACTIVE.equals(plan.getStatus()));
        if (hasActivePlan) {
            return Result.error("该员工存在进行中的护理计划，请先处理护理计划");
        }
        
        Employee updateEmp = new Employee();
        updateEmp.setId(id);
        updateEmp.setStatus(Employee.STATUS_RESIGNED);
        
        employeeMapper.update(updateEmp);
        return Result.success("已办理离职", null);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        Long total = employeeMapper.countByStatus(null);
        Long active = employeeMapper.countByStatus(Employee.STATUS_ACTIVE);
        Long nursing = employeeMapper.countByDepartment(Employee.DEPT_NURSING);
        Long medical = employeeMapper.countByDepartment(Employee.DEPT_MEDICAL);
        
        stats.put("total", total);
        stats.put("active", active);
        stats.put("nursing", nursing);
        stats.put("medical", medical);
        
        return Result.success(stats);
    }
}
