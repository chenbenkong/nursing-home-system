package com.nursinghome.mapper;

import com.nursinghome.entity.Inventory;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface InventoryMapper {
    List<Inventory> selectAll();
    List<Inventory> selectByCategory(@Param("category") String category);
    List<Inventory> selectByStatus(@Param("status") String status);
    List<Inventory> selectByItemName(@Param("itemName") String itemName);
    Inventory selectById(@Param("id") Long id);
    Inventory selectByItemNo(@Param("itemNo") String itemNo);
    int insert(Inventory inventory);
    int update(Inventory inventory);
    int deleteById(@Param("id") Long id);
    Long countAll();
    Long countByCategory(@Param("category") String category);
    Long countByStatus(@Param("status") String status);
    List<Inventory> selectLowStock();
}
