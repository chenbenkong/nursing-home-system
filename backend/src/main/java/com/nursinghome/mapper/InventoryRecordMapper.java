package com.nursinghome.mapper;

import com.nursinghome.entity.InventoryRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface InventoryRecordMapper {
    List<InventoryRecord> selectAll();
    List<InventoryRecord> selectByItemId(@Param("itemId") Long itemId);
    List<InventoryRecord> selectByOperationType(@Param("operationType") String operationType);
    List<InventoryRecord> selectByOperatorId(@Param("operatorId") Long operatorId);
    InventoryRecord selectById(@Param("id") Long id);
    InventoryRecord selectByRecordNo(@Param("recordNo") String recordNo);
    int insert(InventoryRecord inventoryRecord);
    int update(InventoryRecord inventoryRecord);
    int deleteById(@Param("id") Long id);
    Long countAll();
    Long countByItemId(@Param("itemId") Long itemId);
    Long countByOperationType(@Param("operationType") String operationType);
}
