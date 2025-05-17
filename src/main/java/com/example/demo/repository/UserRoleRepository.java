package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    // 根据名称查询
    Optional<UserRole> findByName(String name);
    
    // 根据启用状态查询
    List<UserRole> findByIsEnabled(Boolean isEnabled);
    
    // 根据名称模糊查询
    List<UserRole> findByNameContaining(String name);

    Optional<UserRole> findById(Long id);
    
    void deleteById(Long id);
} 