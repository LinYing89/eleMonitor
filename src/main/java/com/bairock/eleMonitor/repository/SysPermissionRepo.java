package com.bairock.eleMonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bairock.eleMonitor.data.SysPermission;

public interface SysPermissionRepo extends JpaRepository<SysPermission, Long> {

	@Query(value="select sp.* from user su"
			+ " left join user_roles sur on su.id = sur.user_id"
			+ " left join sys_role_permissions srp on sur.roles_id = srp.sys_role_id"
			+ " left join sys_permission sp on srp.permissions_id = sp.id where su.username = :username", nativeQuery = true)
	List<SysPermission> findByUsername(@Param("username") String username);
}
