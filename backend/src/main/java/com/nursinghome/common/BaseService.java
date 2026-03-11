package com.nursinghome.common;

import java.util.List;

/**
 * 基础Service接口 - 定义通用的CRUD操作
 * @param <T> 实体类型
 */
public interface BaseService<T> {
    
    /**
     * 根据ID查询
     */
    T findById(Long id);
    
    /**
     * 查询所有
     */
    List<T> findAll();
    
    /**
     * 根据关键字查询（用于分页列表）
     */
    List<T> findByKeyword(String keyword);
    
    /**
     * 保存
     */
    void save(T entity);
    
    /**
     * 更新
     */
    void update(T entity);
    
    /**
     * 删除
     */
    void delete(Long id);
}
