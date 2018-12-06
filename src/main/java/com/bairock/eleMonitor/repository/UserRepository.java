package com.bairock.eleMonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUsername(String name);
	public User findByUsernameAndPassword(String name, String password);
}
