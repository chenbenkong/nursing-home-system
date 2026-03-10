package com.nursinghome.service;

import com.nursinghome.entity.Result;
import com.nursinghome.entity.User;
import java.util.List;
import java.util.Map;

/**
 * 用户Service接口
 */
public interface UserService {
    
    Result login(String username, String password);
    
    Result<Void> register(User user);
    
    Result<User> getUserById(Long id);
    
    Result<Map<String, Object>> getAllUsers();
    
    Result<Map<String, Object>> getUserList(Integer pageNum, Integer pageSize, String username, String realName, String phone, String role, Integer status);
    
    Result<Void> addUser(User user);
    
    Result<Void> updateUser(User user);
    
    Result<Void> deleteUser(Long id);
}
