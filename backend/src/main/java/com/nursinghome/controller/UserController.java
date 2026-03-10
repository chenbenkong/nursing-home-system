package com.nursinghome.controller;

import com.nursinghome.entity.Result;
import com.nursinghome.entity.User;
import com.nursinghome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }
        
        return userService.login(username, password);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error("用户名和密码不能为空");
        }
        return userService.register(user);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getUserById(userId);
    }

    /**
     * 获取用户列表（支持搜索）
     */
    @GetMapping("/list")
    public Result getUserList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer status) {
        return userService.getUserList(pageNum, pageSize, username, realName, phone, role, status);
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 添加用户
     */
    @PostMapping
    public Result addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
