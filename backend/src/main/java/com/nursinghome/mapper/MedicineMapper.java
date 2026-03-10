package com.nursinghome.mapper;

import com.nursinghome.entity.Medicine;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 药品Mapper接口
 */
public interface MedicineMapper {
    
    List<Medicine> selectAll();
    
    List<Medicine> selectByCategory(@Param("category") String category);
    
    List<Medicine> selectByStatus(@Param("status") String status);
    
    List<Medicine> selectByName(@Param("name") String name);
    
    Medicine selectById(@Param("id") Long id);
    
    Medicine selectByMedicineNo(@Param("medicineNo") String medicineNo);
    
    int insert(Medicine medicine);
    
    int update(Medicine medicine);
    
    int deleteById(@Param("id") Long id);
    
    Long countAll();
    
    Long countByCategory(@Param("category") String category);
    
    Long countByStatus(@Param("status") String status);
    
    List<Medicine> selectLowStock();
    
    List<Medicine> selectExpired();
}
