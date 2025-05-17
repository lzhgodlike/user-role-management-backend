package com.example.demo.controller;

import com.example.demo.entity.UserRole;
import com.example.demo.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-roles")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH}, allowCredentials = "false")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 处理数据校验错误
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * 获取所有用户角色
     */
    @GetMapping
    public ResponseEntity<List<UserRole>> getAllUserRoles() {
        List<UserRole> userRoles = userRoleService.findAll();
        return ResponseEntity.ok(userRoles);
    }

    /**
     * 根据ID获取用户角色
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserRoleById(@PathVariable Long id) {
        UserRole userRole = userRoleService.findById(id);
        if (userRole != null) {
            return ResponseEntity.ok(userRole);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 创建用户角色
     */
    @PostMapping
    public ResponseEntity<UserRole> createUserRole(@Valid @RequestBody UserRole userRole) {
        UserRole savedUserRole = userRoleService.save(userRole);
        return new ResponseEntity<>(savedUserRole, HttpStatus.CREATED);
    }

    /**
     * 更新用户角色
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRole userRole) {
        UserRole existingUserRole = userRoleService.findById(id);
        if (existingUserRole == null) {
            return ResponseEntity.notFound().build();
        }
        
        userRole.setId(id);
        UserRole updatedUserRole = userRoleService.save(userRole);
        return ResponseEntity.ok(updatedUserRole);
    }

    /**
     * 删除用户角色
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserRole(@PathVariable Long id) {
        UserRole existingUserRole = userRoleService.findById(id);
        if (existingUserRole == null) {
            return ResponseEntity.notFound().build();
        }
        
        userRoleService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "用户角色删除成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 更新用户角色状态（启用/禁用）
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserRoleStatus(@PathVariable Long id, @RequestParam Boolean isEnabled) {
        UserRole userRole = userRoleService.updateStatus(id, isEnabled);
        if (userRole != null) {
            return ResponseEntity.ok(userRole);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 根据角色名称模糊查询
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserRole>> searchUserRolesByName(@RequestParam String name) {
        List<UserRole> userRoles = userRoleService.findByNameContaining(name);
        return ResponseEntity.ok(userRoles);
    }

    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "服务器错误: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
