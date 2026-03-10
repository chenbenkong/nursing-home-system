package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.MedicationRecord;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.MedicationRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用药执行记录Controller
 */
@RestController
@RequestMapping("/medication-record")
public class MedicationRecordController {

    @Autowired
    private MedicationRecordMapper medicationRecordMapper;

    /**
     * 获取用药执行记录列表（分页）
     */
    @GetMapping("/list")
    public Result getMedicationRecordList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<MedicationRecord> list;
        
        if (elderId != null) {
            list = medicationRecordMapper.selectByElderId(elderId);
        } else if (status != null && !status.isEmpty()) {
            list = medicationRecordMapper.selectByStatus(status);
        } else if (date != null && !date.isEmpty()) {
            list = medicationRecordMapper.selectByDate(LocalDate.parse(date));
        } else {
            list = medicationRecordMapper.selectAll();
        }
        
        PageInfo<MedicationRecord> pageInfo = new PageInfo<>(list);
        
        PageResult<MedicationRecord> pageResult = new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        );
        
        return Result.success(pageResult);
    }

    /**
     * 获取所有用药执行记录
     */
    @GetMapping("/all")
    public Result getAllMedicationRecords() {
        List<MedicationRecord> list = medicationRecordMapper.selectAll();
        return Result.success(list);
    }

    /**
     * 根据ID获取用药执行记录
     */
    @GetMapping("/{id}")
    public Result getMedicationRecordById(@PathVariable Long id) {
        MedicationRecord record = medicationRecordMapper.selectById(id);
        if (record == null) {
            return Result.error("记录不存在");
        }
        return Result.success(record);
    }

    /**
     * 获取老人的用药执行记录
     */
    @GetMapping("/elder/{elderId}")
    public Result getRecordsByElderId(@PathVariable Long elderId) {
        List<MedicationRecord> list = medicationRecordMapper.selectByElderId(elderId);
        return Result.success(list);
    }

    /**
     * 获取某日的用药执行记录
     */
    @GetMapping("/date/{date}")
    public Result getRecordsByDate(@PathVariable String date) {
        List<MedicationRecord> list = medicationRecordMapper.selectByDate(LocalDate.parse(date));
        return Result.success(list);
    }

    /**
     * 获取今日的用药执行记录
     */
    @GetMapping("/today")
    public Result getTodayRecords() {
        List<MedicationRecord> list = medicationRecordMapper.selectByDate(LocalDate.now());
        return Result.success(list);
    }

    /**
     * 添加用药执行记录
     */
    @PostMapping
    public Result addMedicationRecord(@RequestBody MedicationRecord medicationRecord) {
        // 设置默认状态
        if (medicationRecord.getStatus() == null) {
            medicationRecord.setStatus(MedicationRecord.STATUS_PENDING);
        }
        
        // 设置默认计划时间
        if (medicationRecord.getScheduledTime() == null) {
            medicationRecord.setScheduledTime(LocalDateTime.now());
        }
        
        medicationRecordMapper.insert(medicationRecord);
        return Result.success("添加成功", medicationRecord);
    }

    /**
     * 更新用药执行记录
     */
    @PutMapping
    public Result updateMedicationRecord(@RequestBody MedicationRecord medicationRecord) {
        MedicationRecord existRecord = medicationRecordMapper.selectById(medicationRecord.getId());
        if (existRecord == null) {
            return Result.error("记录不存在");
        }
        
        medicationRecordMapper.update(medicationRecord);
        return Result.success("更新成功", null);
    }

    /**
     * 删除用药执行记录
     */
    @DeleteMapping("/{id}")
    public Result deleteMedicationRecord(@PathVariable Long id) {
        MedicationRecord existRecord = medicationRecordMapper.selectById(id);
        if (existRecord == null) {
            return Result.error("记录不存在");
        }
        
        medicationRecordMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 标记为已完成
     */
    @PutMapping("/{id}/complete")
    public Result completeRecord(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> params) {
        MedicationRecord existRecord = medicationRecordMapper.selectById(id);
        if (existRecord == null) {
            return Result.error("记录不存在");
        }
        
        MedicationRecord updateRecord = new MedicationRecord();
        updateRecord.setId(id);
        updateRecord.setStatus(MedicationRecord.STATUS_TAKEN);
        updateRecord.setActualTime(LocalDateTime.now());
        
        // 接收服用结果和备注
        if (params != null) {
            if (params.get("result") != null) {
                updateRecord.setResult(params.get("result").toString());
            }
            if (params.get("notes") != null) {
                updateRecord.setNotes(params.get("notes").toString());
            }
        }
        
        medicationRecordMapper.update(updateRecord);
        return Result.success("已标记为完成", null);
    }

    /**
     * 标记为跳过
     */
    @PutMapping("/{id}/skip")
    public Result skipRecord(@PathVariable Long id) {
        MedicationRecord existRecord = medicationRecordMapper.selectById(id);
        if (existRecord == null) {
            return Result.error("记录不存在");
        }
        
        MedicationRecord updateRecord = new MedicationRecord();
        updateRecord.setId(id);
        updateRecord.setStatus(MedicationRecord.STATUS_SKIPPED);
        
        medicationRecordMapper.update(updateRecord);
        return Result.success("已标记为跳过", null);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        
        // 今日总记录数
        Long todayTotal = medicationRecordMapper.countByDate(today);
        // 今日已服用
        Long todayTaken = medicationRecordMapper.countByDateAndStatus(today, MedicationRecord.STATUS_TAKEN);
        // 今日待服药（未服用）
        Long todayPending = medicationRecordMapper.countByDateAndStatus(today, MedicationRecord.STATUS_PENDING);
        // 今日漏服
        Long todayMissed = medicationRecordMapper.countByDateAndStatus(today, MedicationRecord.STATUS_MISSED);
        
        stats.put("todayTotal", todayTotal != null ? todayTotal : 0);
        stats.put("todayTaken", todayTaken != null ? todayTaken : 0);
        stats.put("todayPending", todayPending != null ? todayPending : 0);
        stats.put("todayMissed", todayMissed != null ? todayMissed : 0);
        
        return Result.success(stats);
    }
}
