package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.ElderMeal;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.ElderMealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/elder-meal")
public class ElderMealController {

    @Autowired
    private ElderMealMapper elderMealMapper;

    @GetMapping("/list")
    public Result getElderMealList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String date) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<ElderMeal> list;
        
        if (elderId != null && date != null && !date.isEmpty()) {
            list = elderMealMapper.selectByElderIdAndDate(elderId, LocalDate.parse(date));
        } else if (elderId != null) {
            list = elderMealMapper.selectByElderId(elderId);
        } else if (date != null && !date.isEmpty()) {
            list = elderMealMapper.selectByDate(LocalDate.parse(date));
        } else {
            list = elderMealMapper.selectAll();
        }
        
        PageInfo<ElderMeal> pageInfo = new PageInfo<>(list);
        PageResult<ElderMeal> pageResult = new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/all")
    public Result getAllElderMeals() {
        return Result.success(elderMealMapper.selectAll());
    }

    @GetMapping("/{id}")
    public Result getElderMealById(@PathVariable Long id) {
        ElderMeal record = elderMealMapper.selectById(id);
        if (record == null) return Result.error("记录不存在");
        return Result.success(record);
    }

    @GetMapping("/elder/{elderId}")
    public Result getMealsByElderId(@PathVariable Long elderId) {
        return Result.success(elderMealMapper.selectByElderId(elderId));
    }

    @GetMapping("/date/{date}")
    public Result getMealsByDate(@PathVariable String date) {
        return Result.success(elderMealMapper.selectByDate(LocalDate.parse(date)));
    }

    @GetMapping("/today")
    public Result getTodayMeals() {
        return Result.success(elderMealMapper.selectByDate(LocalDate.now()));
    }

    @PostMapping
    public Result addElderMeal(@RequestBody ElderMeal elderMeal) {
        if (elderMeal.getMealDate() == null) elderMeal.setMealDate(LocalDate.now());
        elderMealMapper.insert(elderMeal);
        return Result.success("添加成功", elderMeal);
    }

    @PutMapping
    public Result updateElderMeal(@RequestBody ElderMeal elderMeal) {
        if (elderMealMapper.selectById(elderMeal.getId()) == null) {
            return Result.error("记录不存在");
        }
        elderMealMapper.update(elderMeal);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result deleteElderMeal(@PathVariable Long id) {
        if (elderMealMapper.selectById(id) == null) return Result.error("记录不存在");
        elderMealMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", elderMealMapper.countAll());
        stats.put("todayCount", elderMealMapper.countByDate(LocalDate.now()));
        return Result.success(stats);
    }
}
