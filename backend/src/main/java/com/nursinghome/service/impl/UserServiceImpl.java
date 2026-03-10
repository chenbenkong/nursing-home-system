package com.nursinghome.service.impl;

import com.nursinghome.entity.Result;
import com.nursinghome.entity.User;
import com.nursinghome.mapper.UserMapper;
import com.nursinghome.service.UserService;
import com.nursinghome.util.BCryptUtil;
import com.nursinghome.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        if (user.getStatus() == User.STATUS_DISABLED) {
            return Result.error("账号已被禁用");
        }
        
        // 使用BCrypt验证密码
        if (!BCryptUtil.matches(password, user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        
        // 更新最后登录时间
        userMapper.updateLastLoginTime(user.getId());
        
        // 生成JWT token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        // 清除密码
        user.setPassword(null);
        
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("token", token);
        
        return Result.success("登录成功", data);
    }

    @Override
    @Transactional
    public Result<Void> register(User user) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        
        // 使用BCrypt加密密码
        user.setPassword(BCryptUtil.encode(user.getPassword()));
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(User.STATUS_ENABLED);
        }
        
        // 设置默认角色
        if (user.getRole() == null) {
            user.setRole(User.ROLE_STAFF);
        }
        
        userMapper.insert(user);
        return Result.success("注册成功", null);
    }

    @Override
    public Result<User> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @Override
    public Result<Map<String, Object>> getAllUsers() {
        List<User> users = userMapper.selectAll();
        users.forEach(user -> user.setPassword(null));
        Map<String, Object> data = new HashMap<>();
        data.put("list", users);
        data.put("total", users.size());
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> getUserList(Integer pageNum, Integer pageSize, String username, String realName, String phone, String role, Integer status) {
        List<User> users = userMapper.selectByCondition(username, realName, phone, role, status);
        users.forEach(user -> user.setPassword(null));
        Map<String, Object> data = new HashMap<>();
        data.put("list", users);
        data.put("total", users.size());
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Void> addUser(User user) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        
        // 使用BCrypt加密密码
        user.setPassword(BCryptUtil.encode(user.getPassword()));
        
        if (user.getStatus() == null) {
            user.setStatus(User.STATUS_ENABLED);
        }
        
        userMapper.insert(user);
        return Result.success("添加成功", null);
    }

    @Override
    @Transactional
    public Result<Void> updateUser(User user) {
        User existUser = userMapper.selectById(user.getId());
        if (existUser == null) {
            return Result.error("用户不存在");
        }
        
        // 如果修改了用户名，检查是否与其他用户重复
        if (user.getUsername() != null && !user.getUsername().equals(existUser.getUsername())) {
            User duplicateUser = userMapper.selectByUsername(user.getUsername());
            if (duplicateUser != null && !duplicateUser.getId().equals(user.getId())) {
                return Result.error("用户名已存在");
            }
        }
        
        // 如果修改了密码，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(BCryptUtil.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        
        userMapper.update(user);
        return Result.success("更新成功", null);
    }

    @Override
    @Transactional
    public Result<Void> deleteUser(Long id) {
        User existUser = userMapper.selectById(id);
        if (existUser == null) {
            return Result.error("用户不存在");
        }
        
        userMapper.deleteById(id);
        return Result.success("删除成功", null);
    }
}
