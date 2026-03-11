package com.nursinghome.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursinghome.entity.PageResult;
import com.nursinghome.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础Controller - 提供通用的CRUD操作
 * @param <T> 实体类型
 * @param <S> Service类型
 */
public abstract class BaseController<T, S extends BaseService<T>> {

    @Autowired
    protected S service;

    /**
     * 分页查询列表
     */
    @GetMapping("/list")
    public Result getList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = service.findByKeyword(keyword);
        PageInfo<T> pageInfo = new PageInfo<>(list);
        
        return Result.success(new PageResult<>(
            pageInfo.getList(),
            pageInfo.getTotal(),
            pageNum,
            pageSize
        ));
    }

    /**
     * 获取所有数据
     */
    @GetMapping("/all")
    public Result getAll() {
        return Result.success(service.findAll());
    }

    /**
     * 根据ID获取
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        T entity = service.findById(id);
        if (entity == null) {
            return Result.error("记录不存在");
        }
        return Result.success(entity);
    }

    /**
     * 新增
     */
    @PostMapping
    public Result add(@RequestBody T entity) {
        service.save(entity);
        return Result.success("添加成功", entity);
    }

    /**
     * 更新
     */
    @PutMapping
    public Result update(@RequestBody T entity) {
        service.update(entity);
        return Result.success("更新成功", null);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success("删除成功", null);
    }
}
