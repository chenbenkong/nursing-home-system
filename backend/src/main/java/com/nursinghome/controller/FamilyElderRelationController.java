package com.nursinghome.controller;

import com.nursinghome.entity.Result;
import com.nursinghome.entity.FamilyElderRelation;
import com.nursinghome.mapper.FamilyElderRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 家属账户关联Controller
 */
@RestController
@RequestMapping("/family-relation")
public class FamilyElderRelationController {
    
    @Autowired
    private FamilyElderRelationMapper familyElderRelationMapper;
    
    /**
     * 获取家属关联列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String familyName,
            @RequestParam(required = false) String relation,
            @RequestParam(required = false) Integer status) {
        List<FamilyElderRelation> list = familyElderRelationMapper.selectList(elderName, familyName, relation, status);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        return Result.success(result);
    }
    
    /**
     * 获取所有家属关联
     */
    @GetMapping("/all")
    public Result<List<FamilyElderRelation>> all() {
        List<FamilyElderRelation> list = familyElderRelationMapper.selectList(null, null, null, null);
        return Result.success(list);
    }
    
    /**
     * 根据ID获取家属关联
     */
    @GetMapping("/{id}")
    public Result<FamilyElderRelation> getById(@PathVariable Long id) {
        FamilyElderRelation relation = familyElderRelationMapper.selectById(id);
        if (relation == null) {
            return Result.error("记录不存在");
        }
        return Result.success(relation);
    }
    
    /**
     * 根据老人ID获取家属关联
     */
    @GetMapping("/elder/{elderId}")
    public Result<List<FamilyElderRelation>> getByElderId(@PathVariable Long elderId) {
        List<FamilyElderRelation> list = familyElderRelationMapper.selectByElderId(elderId);
        return Result.success(list);
    }
    
    /**
     * 新增家属关联
     */
    @PostMapping
    public Result<Void> add(@RequestBody FamilyElderRelation relation) {
        relation.setStatus(1);
        int result = familyElderRelationMapper.insert(relation);
        if (result > 0) {
            return Result.success(null);
        }
        return Result.error("新增失败");
    }
    
    /**
     * 更新家属关联
     */
    @PutMapping
    public Result<Void> update(@RequestBody FamilyElderRelation relation) {
        int result = familyElderRelationMapper.update(relation);
        if (result > 0) {
            return Result.success(null);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除家属关联
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        int result = familyElderRelationMapper.deleteById(id);
        if (result > 0) {
            return Result.success(null);
        }
        return Result.error("删除失败");
    }
    
    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", familyElderRelationMapper.countActive());
        return Result.success(stats);
    }
}
