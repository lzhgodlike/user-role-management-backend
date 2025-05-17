package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户认证控制器，提供登录、注册和用户信息接口
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "false")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> loginRequest) {
        logger.info("接收到登录请求");
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        logger.info("用户尝试登录: {}", username);
        
        if (username == null || password == null) {
            logger.warn("登录请求缺少用户名或密码");
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户名和密码不能为空");
            return ResponseEntity.badRequest().body(error);
        }
        
        // 简化版登录，仅用于演示
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            logger.info("用户找到: {}", username);
            
            // 简单密码验证，实际中应使用加密比较
            if (!password.equals(user.getPassword())) {
                logger.warn("密码错误: {}", username);
                Map<String, String> error = new HashMap<>();
                error.put("message", "用户名或密码错误");
                return ResponseEntity.badRequest().body(error);
            }
            
            Map<String, Object> response = new HashMap<>();
            String token = "demo-token-" + System.currentTimeMillis();
            response.put("token", token);
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            if (user.getRole() != null) {
                response.put("role", user.getRole().getName());
            }
            
            logger.info("用户登录成功: {}", username);
            return ResponseEntity.ok(response);
        }
        
        logger.warn("用户不存在: {}", username);
        Map<String, String> error = new HashMap<>();
        error.put("message", "用户名或密码错误");
        return ResponseEntity.badRequest().body(error);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        logger.info("接收到注册请求: {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("用户名已存在: {}", user.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("message", "用户名已存在");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 为新用户分配ADMIN角色
            List<UserRole> roles = userRoleRepository.findByNameContaining("ADMIN");
            if (!roles.isEmpty()) {
                user.setRole(roles.get(0));
                logger.info("为用户分配角色: {}", roles.get(0).getName());
            } else {
                logger.warn("找不到ADMIN角色");
            }
    
            // 设置创建时间
            user.setCreateTime(new Date());
            
            // 注意：这里没有加密密码，仅作演示
            // 在实际环境中，应该使用passwordEncoder.encode加密密码
            User savedUser = userRepository.save(user);
            logger.info("用户注册成功: {}", savedUser.getUsername());
    
            Map<String, Object> response = new HashMap<>();
            response.put("message", "用户注册成功");
            response.put("id", savedUser.getId());
            response.put("username", savedUser.getUsername());
            if (savedUser.getRole() != null) {
                response.put("role", savedUser.getRole().getName());
            }
    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("注册失败: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "注册失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(@RequestParam(required = false) String username) {
        logger.info("获取用户信息请求: {}", username);
        if (username != null) {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                logger.info("找到用户: {}", username);
                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("username", user.getUsername());
                if (user.getRole() != null) {
                    response.put("role", user.getRole().getName());
                }
                return ResponseEntity.ok(response);
            }
        }
        
        // 如果没有提供用户名或找不到用户，返回一个默认用户（仅用于演示）
        logger.info("返回默认用户信息");
        Map<String, Object> response = new HashMap<>();
        response.put("id", 1);
        response.put("username", "demo");
        response.put("role", "USER");
        
        return ResponseEntity.ok(response);
    }
    
    // 添加一个处理全局异常的方法
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.error("全局异常处理: {}", e.getMessage(), e);
        Map<String, String> error = new HashMap<>();
        error.put("message", "服务器错误: " + e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
} 