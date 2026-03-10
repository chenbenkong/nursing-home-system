package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.HealthRecord;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.HealthRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/health-record")
public class HealthRecordController {

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    @GetMapping("/list")
    public Result getHealthRecordList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String bloodType) {

        PageHelper.startPage(pageNum, pageSize);
        List<HealthRecord> list = healthRecordMapper.selectAll();

        PageInfo<HealthRecord> pageInfo = new PageInfo<>(list);
        PageResult<HealthRecord> pageResult = new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/all")
    public Result getAllHealthRecords() {
        return Result.success(healthRecordMapper.selectAll());
    }

    @GetMapping("/{id}")
    public Result getHealthRecordById(@PathVariable Long id) {
        HealthRecord record = healthRecordMapper.selectById(id);
        if (record == null) return Result.error("记录不存在");
        return Result.success(record);
    }

    @GetMapping("/elder/{elderId}")
    public Result getRecordsByElderId(@PathVariable Long elderId) {
        return Result.success(healthRecordMapper.selectByElderId(elderId));
    }

    @GetMapping("/elder/{elderId}/active")
    public Result getActiveRecordByElderId(@PathVariable Long elderId) {
        HealthRecord record = healthRecordMapper.selectByElderIdActive(elderId);
        if (record == null) return Result.error("没有有效的健康档案");
        return Result.success(record);
    }

    @PostMapping
    public Result addHealthRecord(@RequestBody HealthRecord healthRecord) {
        healthRecordMapper.insert(healthRecord);
        return Result.success("添加成功", healthRecord);
    }

    @PutMapping
    public Result updateHealthRecord(@RequestBody HealthRecord healthRecord) {
        if (healthRecordMapper.selectById(healthRecord.getId()) == null) {
            return Result.error("记录不存在");
        }
        healthRecordMapper.update(healthRecord);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result deleteHealthRecord(@PathVariable Long id) {
        if (healthRecordMapper.selectById(id) == null) return Result.error("记录不存在");
        healthRecordMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/archive")
    public Result archiveHealthRecord(@PathVariable Long id) {
        HealthRecord record = healthRecordMapper.selectById(id);
        if (record == null) return Result.error("记录不存在");
        return Result.success("归档成功", null);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", healthRecordMapper.countAll());
        return Result.success(stats);
    }
}
