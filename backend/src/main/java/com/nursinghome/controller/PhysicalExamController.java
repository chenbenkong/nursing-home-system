package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.PhysicalExam;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.PhysicalExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检记录Controller
 */
@RestController
@RequestMapping("/physical-exam")
public class PhysicalExamController {

    @Autowired
    private PhysicalExamMapper physicalExamMapper;

    /**
     * 获取体检记录列表（分页）
     */
    @GetMapping("/list")
    public Result getPhysicalExamList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String examType) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<PhysicalExam> list;
        
        if (elderId != null) {
            list = physicalExamMapper.selectByElderId(elderId);
        } else if (examType != null && !examType.isEmpty()) {
            list = physicalExamMapper.selectByExamType(examType);
        } else {
            list = physicalExamMapper.selectAll();
        }
        
        PageInfo<PhysicalExam> pageInfo = new PageInfo<>(list);
        PageResult<PhysicalExam> pageResult = new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 获取所有体检记录
     */
    @GetMapping("/all")
    public Result getAllPhysicalExams() {
        return Result.success(physicalExamMapper.selectAll());
    }

    /**
     * 根据ID获取体检记录
     */
    @GetMapping("/{id}")
    public Result getPhysicalExamById(@PathVariable Long id) {
        PhysicalExam exam = physicalExamMapper.selectById(id);
        if (exam == null) return Result.error("体检记录不存在");
        return Result.success(exam);
    }

    /**
     * 获取老人的体检记录
     */
    @GetMapping("/elder/{elderId}")
    public Result getExamsByElderId(@PathVariable Long elderId) {
        return Result.success(physicalExamMapper.selectByElderId(elderId));
    }

    /**
     * 添加体检记录
     */
    @PostMapping
    public Result addPhysicalExam(@RequestBody PhysicalExam physicalExam) {
        physicalExamMapper.insert(physicalExam);
        return Result.success("添加成功", physicalExam);
    }

    /**
     * 更新体检记录
     */
    @PutMapping
    public Result updatePhysicalExam(@RequestBody PhysicalExam physicalExam) {
        if (physicalExamMapper.selectById(physicalExam.getId()) == null) {
            return Result.error("体检记录不存在");
        }
        physicalExamMapper.update(physicalExam);
        return Result.success("更新成功", null);
    }

    /**
     * 删除体检记录
     */
    @DeleteMapping("/{id}")
    public Result deletePhysicalExam(@PathVariable Long id) {
        if (physicalExamMapper.selectById(id) == null) return Result.error("体检记录不存在");
        physicalExamMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", physicalExamMapper.countAll());
        return Result.success(stats);
    }
}
