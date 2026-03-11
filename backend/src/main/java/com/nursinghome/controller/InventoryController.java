package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.Inventory;
import com.nursinghome.entity.InventoryRecord;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.ElderMapper;
import com.nursinghome.mapper.InventoryMapper;
import com.nursinghome.mapper.InventoryRecordMapper;
import com.nursinghome.service.FeeService;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private InventoryRecordMapper inventoryRecordMapper;
    
    @Autowired
    private ElderMapper elderMapper;
    
    @Autowired
    private FeeService feeService;

    @GetMapping("/list")
    public Result getInventoryList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String itemName) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<Inventory> list;
        
        if (itemName != null && !itemName.isEmpty()) {
            list = inventoryMapper.selectByItemName(itemName);
        } else if (category != null && !category.isEmpty()) {
            list = inventoryMapper.selectByCategory(category);
        } else if (status != null && !status.isEmpty()) {
            list = inventoryMapper.selectByStatus(status);
        } else {
            list = inventoryMapper.selectAll();
        }
        
        PageInfo<Inventory> pageInfo = new PageInfo<>(list);
        PageResult<Inventory> pageResult = new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/all")
    public Result getAllInventories() {
        return Result.success(inventoryMapper.selectAll());
    }

    @GetMapping("/{id}")
    public Result getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryMapper.selectById(id);
        if (inventory == null) return Result.error("库存物品不存在");
        return Result.success(inventory);
    }

    @PostMapping
    public Result addInventory(@RequestBody Inventory inventory) {
        inventory.setItemNo(CodeGenerator.generateInventoryNo());
        // 数据库status字段为ENUM('NORMAL', 'LOW_STOCK', 'OUT_OF_STOCK', 'DISCONTINUED')
        if (inventory.getStatus() == null || inventory.getStatus().isEmpty()) {
            inventory.setStatus("NORMAL");
        }
        if (inventory.getStockQuantity() == null) inventory.setStockQuantity(0);
        inventoryMapper.insert(inventory);
        return Result.success("添加成功", inventory);
    }

    @PutMapping
    public Result updateInventory(@RequestBody Inventory inventory) {
        if (inventoryMapper.selectById(inventory.getId()) == null) return Result.error("库存物品不存在");
        inventoryMapper.update(inventory);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result deleteInventory(@PathVariable Long id) {
        if (inventoryMapper.selectById(id) == null) return Result.error("库存物品不存在");
        inventoryMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/in")
    public Result stockIn(@RequestBody Map<String, Object> params) {
        Long itemId = Long.valueOf(params.get("itemId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        
        Inventory inventory = inventoryMapper.selectById(itemId);
        if (inventory == null) return Result.error("库存物品不存在");
        
        Integer beforeStock = inventory.getStockQuantity();
        Integer afterStock = beforeStock + quantity;
        inventory.setStockQuantity(afterStock);
        inventoryMapper.update(inventory);
        
        InventoryRecord record = new InventoryRecord();
        record.setItemId(itemId);
        record.setItemName(inventory.getItemName());
        record.setRecordType(InventoryRecord.RECORD_TYPE_IN);
        record.setQuantity(quantity);
        record.setBeforeStock(beforeStock);
        record.setAfterStock(afterStock);
        record.setReason((String) params.get("reason"));
        if (params.get("operatorId") != null) record.setOperatorId(Long.valueOf(params.get("operatorId").toString()));
        record.setOperatorName((String) params.get("operatorName"));
        record.setOperateTime(LocalDateTime.now());
        inventoryRecordMapper.insert(record);
        
        return Result.success("入库成功", record);
    }

    @PostMapping("/out")
    public Result stockOut(@RequestBody Map<String, Object> params) {
        Long itemId = Long.valueOf(params.get("itemId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        
        Inventory inventory = inventoryMapper.selectById(itemId);
        if (inventory == null) return Result.error("库存物品不存在");
        
        Integer beforeStock = inventory.getStockQuantity();
        if (beforeStock < quantity) return Result.error("库存不足");
        
        Integer afterStock = beforeStock - quantity;
        inventory.setStockQuantity(afterStock);
        inventoryMapper.update(inventory);
        
        InventoryRecord record = new InventoryRecord();
        record.setItemId(itemId);
        record.setItemName(inventory.getItemName());
        record.setRecordType(InventoryRecord.RECORD_TYPE_OUT);
        record.setQuantity(quantity);
        record.setBeforeStock(beforeStock);
        record.setAfterStock(afterStock);
        record.setReason((String) params.get("reason"));
        if (params.get("operatorId") != null) record.setOperatorId(Long.valueOf(params.get("operatorId").toString()));
        record.setOperatorName((String) params.get("operatorName"));
        record.setOperateTime(LocalDateTime.now());
        
        // 关联老人（如果指定了老人ID）
        Long elderId = null;
        String elderName = null;
        if (params.get("elderId") != null) {
            elderId = Long.valueOf(params.get("elderId").toString());
            com.nursinghome.entity.Elder elder = elderMapper.selectById(elderId);
            if (elder != null) {
                elderName = elder.getName();
                record.setRelatedId(elderId);
                record.setRelatedName(elderName);
            }
        }
        
        inventoryRecordMapper.insert(record);
        
        // 自动生成物资费（如果关联了老人且有价格）
        if (elderId != null && inventory.getPrice() != null) {
            feeService.generateSupplyFee(
                elderId,
                elderName,
                inventory.getPrice(),
                quantity,
                record.getId()
            );
        }
        
        return Result.success("出库成功" + (elderId != null && inventory.getPrice() != null ? "，已自动生成物资费" : ""), record);
    }

    @GetMapping("/low-stock")
    public Result getLowStockInventories() {
        return Result.success(inventoryMapper.selectLowStock());
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", inventoryMapper.countAll());
        stats.put("lowStockCount", inventoryMapper.selectLowStock().size());
        return Result.success(stats);
    }
}
