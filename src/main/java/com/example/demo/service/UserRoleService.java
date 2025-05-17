package com.example.demo.service;

import com.example.demo.entity.UserRole;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * 查询所有用户角色
     */
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    /**
     * 根据ID查询用户角色
     */
    public UserRole findById(Long id) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(id);
        return optionalUserRole.orElse(null);
    }

    /**
     * 根据角色名称查询
     */
    public UserRole findByName(String name) {
        return userRoleRepository.findByName(name).orElse(null);
    }

    /**
     * 根据启用状态查询
     */
    public List<UserRole> findByIsEnabled(Boolean isEnabled) {
        return userRoleRepository.findByIsEnabled(isEnabled);
    }

    /**
     * 根据角色名称模糊查询
     */
    public List<UserRole> findByNameContaining(String name) {
        return userRoleRepository.findByNameContaining(name);
    }

    /**
     * 保存用户角色
     */
    @Transactional
    public UserRole save(UserRole userRole) {
        if (userRole.getId() != null) {
            UserRole existingUserRole = findById(userRole.getId());
            if (existingUserRole != null) {
                userRole.setCreateTime(existingUserRole.getCreateTime());
            }
        }
        return userRoleRepository.save(userRole);
    }

    /**
     * 删除用户角色
     */
    @Transactional
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }

    /**
     * 启用或禁用用户角色
     */
    @Transactional
    public UserRole updateStatus(Long id, Boolean isEnabled) {
        UserRole userRole = findById(id);
        if (userRole != null) {
            userRole.setIsEnabled(isEnabled);
            return userRoleRepository.save(userRole);
        }
        return null;
    }
}
