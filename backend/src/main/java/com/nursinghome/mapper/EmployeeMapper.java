package com.nursinghome.mapper;

import com.nursinghome.entity.Employee;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 员工Mapper接口
 */
public interface EmployeeMapper {
    
    Employee selectById(Long id);
    
    Employee selectByEmpNo(String empNo);
    
    Employee selectByIdCard(@Param("idCard") String idCard);
    
    List<Employee> selectAll();
    
    List<Employee> selectByDepartment(@Param("department") String department);
    
    List<Employee> selectByStatus(@Param("status") String status);
    
    List<Employee> selectByCondition(@Param("name") String name,
                                     @Param("department") String department,
                                     @Param("status") String status);
    
    int insert(Employee employee);
    
    int update(Employee employee);
    
    int deleteById(Long id);
    
    Long countByDepartment(@Param("department") String department);
    
    Long countByStatus(@Param("status") String status);
}
