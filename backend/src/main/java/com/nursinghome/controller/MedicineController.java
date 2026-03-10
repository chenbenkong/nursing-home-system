package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.Medicine;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.MedicineMapper;
import com.nursinghome.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品Controller
 */
@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineMapper medicineMapper;

    /**
     * 获取药品列表（分页）
     */
    @GetMapping("/list")
    public Result getMedicineList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<Medicine> list;
        
        if (name != null && !name.isEmpty()) {
            list = medicineMapper.selectByName(name);
        } else if (category != null && !category.isEmpty()) {
            list = medicineMapper.selectByCategory(category);
        } else if (status != null && !status.isEmpty()) {
            list = medicineMapper.selectByStatus(status);
        } else {
            list = medicineMapper.selectAll();
        }
        
        PageInfo<Medicine> pageInfo = new PageInfo<>(list);
        
        PageResult<Medicine> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        
        return Result.success(pageResult);
    }

    /**
     * 获取所有药品
     */
    @GetMapping("/all")
    public Result getAllMedicines() {
        List<Medicine> list = medicineMapper.selectAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取药品
     */
    @GetMapping("/{id}")
    public Result getMedicineById(@PathVariable Long id) {
        Medicine medicine = medicineMapper.selectById(id);
        if (medicine == null) {
            return Result.error("药品不存在");
        }
        return Result.success(medicine);
    }

    /**
     * 添加药品
     */
    @PostMapping
    public Result addMedicine(@RequestBody Medicine medicine) {
        // 生成药品编号
        medicine.setMedicineNo(CodeGenerator.generateMedicineNo());
        
        // 设置默认状态
        if (medicine.getStatus() == null) {
            medicine.setStatus(Medicine.STATUS_ACTIVE);
        }
        
        // 检查有效期
        if (medicine.getExpiryDate() != null && medicine.getExpiryDate().isBefore(LocalDate.now())) {
            medicine.setStatus(Medicine.STATUS_EXPIRED);
        }
        
        medicineMapper.insert(medicine);
        return Result.success("添加成功", medicine);
    }

    /**
     * 更新药品
     */
    @PutMapping
    public Result updateMedicine(@RequestBody Medicine medicine) {
        Medicine existMedicine = medicineMapper.selectById(medicine.getId());
        if (existMedicine == null) {
            return Result.error("药品不存在");
        }
        
        // 检查有效期并自动更新状态
        if (medicine.getExpiryDate() != null) {
            if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
                // 已过期，设置为过期状态
                medicine.setStatus(Medicine.STATUS_EXPIRED);
            } else if (Medicine.STATUS_EXPIRED.equals(medicine.getStatus())) {
                // 未过期且当前是过期状态，恢复为正常状态
                medicine.setStatus(Medicine.STATUS_ACTIVE);
            }
        }
        
        medicineMapper.update(medicine);
        return Result.success("更新成功", null);
    }

    /**
     * 删除药品
     */
    @DeleteMapping("/{id}")
    public Result deleteMedicine(@PathVariable Long id) {
        Medicine medicine = medicineMapper.selectById(id);
        if (medicine == null) {
            return Result.error("药品不存在");
        }
        
        medicineMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 获取低库存药品
     */
    @GetMapping("/low-stock")
    public Result getLowStockMedicines() {
        List<Medicine> list = medicineMapper.selectLowStock();
        return Result.success(list);
    }

    /**
     * 获取过期药品
     */
    @GetMapping("/expired")
    public Result getExpiredMedicines() {
        List<Medicine> list = medicineMapper.selectExpired();
        return Result.success(list);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        Long total = medicineMapper.countAll();
        Long active = medicineMapper.countByStatus(Medicine.STATUS_ACTIVE);
        Long expired = medicineMapper.countByStatus(Medicine.STATUS_EXPIRED);
        Long disabled = medicineMapper.countByStatus(Medicine.STATUS_DISABLED);
        
        List<Medicine> lowStock = medicineMapper.selectLowStock();
        
        stats.put("total", total != null ? total : 0);
        stats.put("active", active != null ? active : 0);
        stats.put("expired", expired != null ? expired : 0);
        stats.put("disabled", disabled != null ? disabled : 0);
        stats.put("lowStockCount", lowStock != null ? lowStock.size() : 0);
        
        return Result.success(stats);
    }
}
