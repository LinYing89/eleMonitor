package com.bairock.eleMonitor.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.User;
import com.bairock.eleMonitor.repository.UserRepository;

@Service
public class UserService {

	@Resource
	private UserService self;
	@Autowired
	private UserRepository userRepository;
	
	@Cacheable(value="user", key="#name")
	public User findByNameAndPassword(String name, String password) {
		return userRepository.findByUsernameAndPassword(name, password);
	}
	
	@CachePut(value="user",key="#result.id")
	public User addUser(User user) {
		userRepository.saveAndFlush(user);
		return user;
	}
	
	public User editUser(User user) {
		User userDb = self.findByNameAndPassword(user.getUsername(), user.getPassword());
		if(null != userDb) {
			userDb.setPassword(user.getPassword());
			userDb.setTel(user.getTel());
			userRepository.saveAndFlush(userDb);
		}
		return userDb;
	}
	
	@CacheEvict(value="user", key="#userId")
	public void deleteUser(long userId) {
		userRepository.deleteById(userId);
	}
	
}
