package com.nursinghome.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.MealMenu;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import com.nursinghome.mapper.MealMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meal-menu")
public class MealMenuController {

    @Autowired
    private MealMenuMapper mealMenuMapper;

    @GetMapping("/list")
    public Result getMealMenuList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String mealType) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<MealMenu> list;
        
        if (mealType != null && !mealType.isEmpty()) {
            list = mealMenuMapper.selectByMealType(mealType);
        } else {
            list = mealMenuMapper.selectAll();
        }
        
        PageInfo<MealMenu> pageInfo = new PageInfo<>(list);
        PageResult<MealMenu> pageResult = new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/all")
    public Result getAllMealMenus() {
        return Result.success(mealMenuMapper.selectAll());
    }

    @GetMapping("/{id}")
    public Result getMealMenuById(@PathVariable Long id) {
        MealMenu menu = mealMenuMapper.selectById(id);
        if (menu == null) return Result.error("菜单不存在");
        return Result.success(menu);
    }

    @PostMapping
    public Result addMealMenu(@RequestBody MealMenu mealMenu) {
        mealMenuMapper.insert(mealMenu);
        return Result.success("添加成功", mealMenu);
    }

    @PutMapping
    public Result updateMealMenu(@RequestBody MealMenu mealMenu) {
        if (mealMenuMapper.selectById(mealMenu.getId()) == null) {
            return Result.error("菜单不存在");
        }
        mealMenuMapper.update(mealMenu);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result deleteMealMenu(@PathVariable Long id) {
        if (mealMenuMapper.selectById(id) == null) return Result.error("菜单不存在");
        mealMenuMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", mealMenuMapper.countAll());
        return Result.success(stats);
    }
}
