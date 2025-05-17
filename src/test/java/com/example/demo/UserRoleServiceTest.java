package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.enity.UserRole;
import com.example.demo.service.UserRoleService;

@SpringBootTest
public class UserRoleServiceTest {
    @Autowired
    private UserRoleService userRoleService;

    @Test
    public void getByNameTest() {
        UserRole userRole = userRoleService.getByName("教师");
        System.out.println(userRole);
    }

    
}
