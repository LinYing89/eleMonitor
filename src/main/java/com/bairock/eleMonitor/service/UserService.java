package com.bairock.eleMonitor.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.Util;
import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.SysRole;
import com.bairock.eleMonitor.data.User;
import com.bairock.eleMonitor.repository.SysRoleRepo;
import com.bairock.eleMonitor.repository.UserRepository;

@Service
public class UserService {

	@Resource
	private UserService self;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SysRoleRepo sysRoleRepo;
	@Autowired
	private StationService stationService;
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	@Cacheable(value="user", key="#username")
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Cacheable(value="user", key="#username")
	public User findByNameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}
	
	@CachePut(value="user",key="#result.username")
	public User addUser(User user) {
		user.setCreateTime(new Date());
		long roleId = user.getRoles().get(0).getId();
		setRole(user, roleId);
		if(user.getRoles().get(0).getName().equals("ROLE_ADMIN")) {
			user.getListStation().clear();
		}else {
			long stationId = user.getListStation().get(0).getId();
			setStation(user, stationId);
		}
		
		String encodedPsd = Util.encodePassword(user.getPassword());
		user.setPassword(encodedPsd);
		userRepository.saveAndFlush(user);
		return user;
	}
	
	public User editUser(User user) {
		User userDb = self.findByUsername(user.getUsername());
		if(null != userDb) {
			if(userDb.getRoles().isEmpty() || user.getRoles().get(0).getId() != userDb.getRoles().get(0).getId()) {
				long roleId = user.getRoles().get(0).getId();
				setRole(userDb, roleId);
			}
			if(userDb.getListStation().isEmpty() || user.getListStation().get(0).getId() != userDb.getListStation().get(0).getId()) {
				long stationId = user.getListStation().get(0).getId();
				setStation(userDb, stationId);
			}
			userDb.setPersonName(user.getPersonName());
			userDb.setTel(user.getTel());
			userDb.setEnable(user.isEnable());
			userDb.setRemark(user.getRemark());
			userRepository.saveAndFlush(userDb);
		}
		return userDb;
	}
	
	private void setRole(User user, long roleId) {
		SysRole role = sysRoleRepo.findById(roleId).orElse(null);
		if(null != role) {
			if(!user.getRoles().isEmpty()) {
				user.getRoles().clear();
			}
			user.getRoles().add(role);
		}
	}
	
	private void setStation(User user, long stationId) {
		Station station = stationService.findByIdNoCache(stationId);
		if(!user.getListStation().isEmpty()) {
			user.getListStation().clear();
		}
		user.addStation(station);
	}
	
	@CacheEvict(value="user", condition="#result!=null", key="#result.username")
	public User deleteUser(long userId) {
		User userDb = userRepository.findById(userId).orElse(null);
		if(null != userDb) {
			userRepository.deleteById(userId);
		}
		return userDb;
	}
	
}
