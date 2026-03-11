package com.nursinghome.service;

import com.nursinghome.common.BaseService;
import com.nursinghome.entity.Employee;

import java.util.List;
import java.util.Map;

/**
 * 员工Service接口
 */
public interface EmployeeService extends BaseService<Employee> {
    
    /**
     * 根据部门查询
     */
    List<Employee> findByDepartment(String department);
    
    /**
     * 根据身份证号查询
     */
    Employee findByIdCard(String idCard);
    
    /**
     * 员工离职
     */
    void resign(Long id);
    
    /**
     * 获取统计数据
     */
    Map<String, Object> getStatistics();
}
