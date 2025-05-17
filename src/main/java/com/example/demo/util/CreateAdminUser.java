package com.example.demo.util;

import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

/*
 * 暂时禁用此组件以解决启动问题
 */
// @Component
public class CreateAdminUser /* implements CommandLineRunner */ {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Override
    public void run(String... args) throws Exception {
        // 检查是否已存在管理员角色 - 修复返回多个结果的问题
        List<UserRole> adminRoles = userRoleRepository.findByNameContaining("ADMIN");
        
        UserRole adminRole;
        if (adminRoles.isEmpty()) {
            // 如果不存在，创建管理员角色
            UserRole newRole = new UserRole();
            newRole.setName("ADMIN");
            newRole.setRoleDesc("系统管理员");
            newRole.setIsEnabled(true);
            adminRole = userRoleRepository.save(newRole);
        } else {
            // 使用第一个找到的角色
            adminRole = adminRoles.get(0);
            System.out.println("找到" + adminRoles.size() + "个管理员角色，使用ID为" + adminRole.getId() + "的角色");
        }
        
        // 检查是否已存在管理员用户
        if (!userRepository.existsByUsername("admin")) {
            // 创建管理员用户
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("123456")); // 使用加密的密码
            adminUser.setRole(adminRole);
            
            userRepository.save(adminUser);
            System.out.println("管理员用户已创建, 用户名: admin, 密码: 123456");
        } else {
            System.out.println("管理员用户已存在，跳过创建");
        }
    }
} 