package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.ElderMedication;
import com.nursinghome.entity.Medicine;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.ElderMedicationMapper;
import com.nursinghome.mapper.MedicineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老人用药记录Controller
 */
@RestController
@RequestMapping("/elder-medication")
public class ElderMedicationController {

    @Autowired
    private ElderMedicationMapper elderMedicationMapper;
    
    @Autowired
    private MedicineMapper medicineMapper;

    /**
     * 获取用药记录列表（分页）
     */
    @GetMapping("/list")
    public Result getElderMedicationList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String status) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<ElderMedication> list;
        
        if (elderId != null) {
            list = elderMedicationMapper.selectByElderId(elderId);
        } else if (status != null && !status.isEmpty()) {
            list = elderMedicationMapper.selectByStatus(status);
        } else {
            list = elderMedicationMapper.selectAll();
        }
        
        PageInfo<ElderMedication> pageInfo = new PageInfo<>(list);
        
        PageResult<ElderMedication> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        
        return Result.success(pageResult);
    }

    /**
     * 获取所有用药记录
     */
    @GetMapping("/all")
    public Result getAllElderMedications() {
        List<ElderMedication> list = elderMedicationMapper.selectAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取用药记录
     */
    @GetMapping("/{id}")
    public Result getElderMedicationById(@PathVariable Long id) {
        ElderMedication elderMedication = elderMedicationMapper.selectById(id);
        if (elderMedication == null) {
            return Result.error("用药记录不存在");
        }
        return Result.success(elderMedication);
    }

    /**
     * 获取老人的用药记录
     */
    @GetMapping("/elder/{elderId}")
    public Result getMedicationsByElderId(@PathVariable Long elderId) {
        List<ElderMedication> list = elderMedicationMapper.selectByElderId(elderId);
        return Result.success(list);
    }

    /**
     * 获取老人的活跃用药记录
     */
    @GetMapping("/elder/{elderId}/active")
    public Result getActiveMedicationsByElderId(@PathVariable Long elderId) {
        List<ElderMedication> list = elderMedicationMapper.selectActiveByElderId(elderId);
        return Result.success(list);
    }

    /**
     * 添加用药记录
     */
    @PostMapping
    public Result addElderMedication(@RequestBody ElderMedication elderMedication) {
        // 设置默认状态
        if (elderMedication.getStatus() == null) {
            elderMedication.setStatus(ElderMedication.STATUS_ACTIVE);
        }
        
        // 设置开始日期
        if (elderMedication.getStartDate() == null) {
            elderMedication.setStartDate(LocalDate.now());
        }
        
        // 获取药品信息
        if (elderMedication.getMedicineId() != null) {
            Medicine medicine = medicineMapper.selectById(elderMedication.getMedicineId());
            if (medicine != null) {
                elderMedication.setMedicineName(medicine.getName());
            }
        }
        
        elderMedicationMapper.insert(elderMedication);
        return Result.success("添加成功", elderMedication);
    }

    /**
     * 更新用药记录
     */
    @PutMapping
    public Result updateElderMedication(@RequestBody ElderMedication elderMedication) {
        ElderMedication existRecord = elderMedicationMapper.selectById(elderMedication.getId());
        if (existRecord == null) {
            return Result.error("用药记录不存在");
        }
        
        // 获取药品信息
        if (elderMedication.getMedicineId() != null) {
            Medicine medicine = medicineMapper.selectById(elderMedication.getMedicineId());
            if (medicine != null) {
                elderMedication.setMedicineName(medicine.getName());
            }
        }
        
        elderMedicationMapper.update(elderMedication);
        return Result.success("更新成功", null);
    }

    /**
     * 删除用药记录
     */
    @DeleteMapping("/{id}")
    public Result deleteElderMedication(@PathVariable Long id) {
        ElderMedication existRecord = elderMedicationMapper.selectById(id);
        if (existRecord == null) {
            return Result.error("用药记录不存在");
        }
        
        elderMedicationMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 完成用药
     */
    @PutMapping("/{id}/complete")
    public Result completeMedication(@PathVariable Long id) {
        ElderMedication existRecord = elderMedicationMapper.selectById(id);
        if (existRecord == null) {
            return Result.error("用药记录不存在");
        }
        
        ElderMedication updateRecord = new ElderMedication();
        updateRecord.setId(id);
        updateRecord.setStatus(ElderMedication.STATUS_COMPLETED);
        updateRecord.setEndDate(LocalDate.now());
        
        elderMedicationMapper.update(updateRecord);
        return Result.success("用药已完成", null);
    }

    /**
     * 暂停用药
     */
    @PutMapping("/{id}/pause")
    public Result pauseMedication(@PathVariable Long id) {
        ElderMedication existRecord = elderMedicationMapper.selectById(id);
        if (existRecord == null) {
            return Result.error("用药记录不存在");
        }
        
        ElderMedication updateRecord = new ElderMedication();
        updateRecord.setId(id);
        updateRecord.setStatus(ElderMedication.STATUS_STOPPED);
        
        elderMedicationMapper.update(updateRecord);
        return Result.success("用药已暂停", null);
    }

    /**
     * 恢复用药
     */
    @PutMapping("/{id}/resume")
    public Result resumeMedication(@PathVariable Long id) {
        ElderMedication existRecord = elderMedicationMapper.selectById(id);
        if (existRecord == null) {
            return Result.error("用药记录不存在");
        }
        
        ElderMedication updateRecord = new ElderMedication();
        updateRecord.setId(id);
        updateRecord.setStatus(ElderMedication.STATUS_ACTIVE);
        
        elderMedicationMapper.update(updateRecord);
        return Result.success("用药已恢复", null);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        Long total = elderMedicationMapper.countAll();
        Long active = elderMedicationMapper.countByStatus(ElderMedication.STATUS_ACTIVE);
        Long completed = elderMedicationMapper.countByStatus(ElderMedication.STATUS_COMPLETED);
        Long today = elderMedicationMapper.countToday();
        
        stats.put("totalCount", total != null ? total : 0);
        stats.put("activeCount", active != null ? active : 0);
        stats.put("completedCount", completed != null ? completed : 0);
        stats.put("todayCount", today != null ? today : 0);
        
        return Result.success(stats);
    }
}
