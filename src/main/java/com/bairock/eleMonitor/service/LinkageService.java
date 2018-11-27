package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Linkage;
import com.bairock.eleMonitor.repository.LinkageRepository;

@Service
public class LinkageService {
	@Autowired
	private LinkageService self;
	@Autowired
	private LinkageRepository linkageRepository;
	@Autowired
	private DeviceService deviceService;
	
	@Cacheable(value = "linkage", key = "#id")
	public Linkage findById(long id) {
		Optional<Linkage> option = linkageRepository.findById(id);
		Linkage d = option.orElse(null);
		return d;
	}
	
	@CachePut(value = "linkage", key = "#result.id")
	public Linkage addDevice(long deviceId, Linkage linkage) {
		Device device = deviceService.findById(deviceId);
		if(null == device) {
			return null;
		}
		device.addLinkage(linkage);
		linkageRepository.saveAndFlush(linkage);
		return linkage;
	}
	
	public Linkage editDevice(long linkageId, Linkage linkage) {
		Linkage res = self.findById(linkageId);
		if(null != res) {
			res.setAlarming(linkage.isAlarming());
			res.setCompareSymbol(linkage.getCompareSymbol());
			res.setCompareValue(linkage.getCompareValue());
			linkageRepository.saveAndFlush(res);
		}
		return res;
	}
	
	@CacheEvict(value = "linkage", key = "#result.id")
	public Linkage delete(Linkage linkage) {
		linkageRepository.delete(linkage);
		return linkage;
	}
}
