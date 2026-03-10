package com.nursinghome.controller;

import com.nursinghome.entity.Result;
import com.nursinghome.entity.SysConfig;
import com.nursinghome.mapper.SysConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置Controller
 */
@RestController
@RequestMapping("/sys-config")
public class SysConfigController {
    
    @Autowired
    private SysConfigMapper sysConfigMapper;
    
    /**
     * 获取所有配置
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> list() {
        List<SysConfig> list = sysConfigMapper.selectAll();
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        return Result.success(result);
    }
    
    /**
     * 获取自定义配置
     */
    @GetMapping("/custom")
    public Result<List<SysConfig>> custom() {
        List<SysConfig> list = sysConfigMapper.selectCustom();
        return Result.success(list);
    }
    
    /**
     * 根据ID获取配置
     */
    @GetMapping("/{id}")
    public Result<SysConfig> getById(@PathVariable Long id) {
        SysConfig config = sysConfigMapper.selectById(id);
        if (config == null) {
            return Result.error("配置不存在");
        }
        return Result.success(config);
    }
    
    /**
     * 根据Key获取配置
     */
    @GetMapping("/key/{configKey}")
    public Result<SysConfig> getByKey(@PathVariable String configKey) {
        SysConfig config = sysConfigMapper.selectByKey(configKey);
        if (config == null) {
            return Result.error("配置不存在");
        }
        return Result.success(config);
    }
    
    /**
     * 新增配置
     */
    @PostMapping
    public Result<Void> add(@RequestBody SysConfig config) {
        // 检查key是否已存在
        SysConfig existing = sysConfigMapper.selectByKey(config.getConfigKey());
        if (existing != null) {
            return Result.error("配置键已存在");
        }
        config.setIsSystem(0);
        int result = sysConfigMapper.insert(config);
        if (result > 0) {
            return Result.success(null);
        }
        return Result.error("新增失败");
    }
    
    /**
     * 更新配置
     */
    @PutMapping
    public Result<Void> update(@RequestBody SysConfig config) {
        int result = sysConfigMapper.update(config);
        if (result > 0) {
            return Result.success(null);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除配置（只能删除非系统配置）
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        int result = sysConfigMapper.deleteById(id);
        if (result > 0) {
            return Result.success(null);
        }
        return Result.error("删除失败，系统配置不能删除");
    }
    
    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", sysConfigMapper.countAll());
        stats.put("systemCount", sysConfigMapper.countSystem());
        stats.put("customCount", sysConfigMapper.countAll() - sysConfigMapper.countSystem());
        return Result.success(stats);
    }
}
