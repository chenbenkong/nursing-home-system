package com.nursinghome.service.impl;

import com.nursinghome.entity.CarePlan;
import com.nursinghome.entity.Employee;
import com.nursinghome.mapper.CarePlanMapper;
import com.nursinghome.mapper.EmployeeMapper;
import com.nursinghome.service.EmployeeService;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工Service实现类
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CarePlanMapper carePlanMapper;

    @Override
    public Employee findById(Long id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeMapper.selectAll();
    }

    @Override
    public List<Employee> findByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return employeeMapper.selectAll();
        }
        return employeeMapper.selectByCondition(keyword, null, null);
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        return employeeMapper.selectByDepartment(department);
    }

    @Override
    public Employee findByIdCard(String idCard) {
        return employeeMapper.selectByIdCard(idCard);
    }

    @Override
    @Transactional
    public void save(Employee employee) {
        // 检查身份证号
        if (StringUtils.hasText(employee.getIdCard())) {
            Employee exist = employeeMapper.selectByIdCard(employee.getIdCard());
            if (exist != null) {
                throw new RuntimeException("身份证号已存在");
            }
        }

        // 生成编号
        employee.setEmpNo(CodeGenerator.generateEmpNo());

        // 设置默认状态
        if (employee.getStatus() == null) {
            employee.setStatus(Employee.STATUS_ACTIVE);
        }

        employeeMapper.insert(employee);
    }

    @Override
    @Transactional
    public void update(Employee employee) {
        Employee existEmp = employeeMapper.selectById(employee.getId());
        if (existEmp == null) {
            throw new RuntimeException("员工不存在");
        }

        // 检查身份证号重复
        if (StringUtils.hasText(employee.getIdCard())
                && !employee.getIdCard().equals(existEmp.getIdCard())) {
            Employee duplicate = employeeMapper.selectByIdCard(employee.getIdCard());
            if (duplicate != null && !duplicate.getId().equals(employee.getId())) {
                throw new RuntimeException("身份证号已存在");
            }
        }

        employeeMapper.update(employee);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee existEmp = employeeMapper.selectById(id);
        if (existEmp == null) {
            throw new RuntimeException("员工不存在");
        }

        // 检查护理计划关联
        if (hasCarePlan(id, false)) {
            throw new RuntimeException("该员工存在关联的护理计划，请先处理护理计划");
        }

        // 检查用户账号绑定
        if (existEmp.getUserId() != null) {
            throw new RuntimeException("该员工已绑定系统用户账号，请先解除绑定");
        }

        employeeMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void resign(Long id) {
        Employee existEmp = employeeMapper.selectById(id);
        if (existEmp == null) {
            throw new RuntimeException("员工不存在");
        }

        if (Employee.STATUS_RESIGNED.equals(existEmp.getStatus())) {
            throw new RuntimeException("该员工已离职，不能重复办理");
        }

        // 检查进行中的护理计划
        if (hasCarePlan(id, true)) {
            throw new RuntimeException("该员工存在进行中的护理计划，请先处理护理计划");
        }

        Employee updateEmp = new Employee();
        updateEmp.setId(id);
        updateEmp.setStatus(Employee.STATUS_RESIGNED);
        employeeMapper.update(updateEmp);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", employeeMapper.countByStatus(null));
        stats.put("active", employeeMapper.countByStatus(Employee.STATUS_ACTIVE));
        stats.put("nursing", employeeMapper.countByDepartment(Employee.DEPT_NURSING));
        stats.put("medical", employeeMapper.countByDepartment(Employee.DEPT_MEDICAL));
        return stats;
    }

    /**
     * 检查是否有护理计划
     * @param employeeId 员工ID
     * @param activeOnly 是否只检查进行中的计划
     */
    private boolean hasCarePlan(Long employeeId, boolean activeOnly) {
        List<CarePlan> carePlans = carePlanMapper.selectAll();
        return carePlans.stream()
                .filter(plan -> employeeId.equals(plan.getNurseId()))
                .anyMatch(plan -> !activeOnly || CarePlan.STATUS_ACTIVE.equals(plan.getStatus()));
    }
}
