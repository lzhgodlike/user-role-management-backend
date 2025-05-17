-- 检查ADMIN角色是否存在
INSERT INTO user_role (role_name, role_desc, is_enabled, create_time, update_time)
SELECT 'ADMIN', '系统管理员', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM user_role WHERE role_name = 'ADMIN'
    LIMIT 1
);

-- 查询新建的ADMIN角色ID
SET @admin_role_id = (SELECT id FROM user_role WHERE role_name = 'ADMIN' LIMIT 1);

-- 插入管理员用户 (密码为123456的BCrypt加密值)
-- BCrypt加密的密码值: $2a$10$EuZ.z1daTrXUZOQQEEjkZe/K2gN5GsIxJIm7feP5JGTGt3WD9aV5O
INSERT INTO users (username, password, role_id, create_time, update_time)
SELECT 'admin', '123456', 
       @admin_role_id, 
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

-- 插入测试用户（明文密码为了测试方便）
INSERT INTO users (username, password, role_id, create_time, update_time)
SELECT 'test', '123456', 
       @admin_role_id, 
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'test'
);

-- 注意：这里的密码是明文，实际使用中应该是加密的
-- 下面的INSERT语句仅用于示例，实际应用需要使用加密的密码
-- 真实环境请使用应用程序的注册功能来创建用户 