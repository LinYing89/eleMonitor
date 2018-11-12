package com.bairock.eleMonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.DeviceValueHistory;
import com.bairock.eleMonitor.repository.DeviceValueHistoryRepository;

@Service
public class DeviceValueHistoryService {

	@Autowired
	private DeviceValueHistoryRepository deviceValueHistoryRepository;
	
	public DeviceValueHistory add(DeviceValueHistory device) {
		deviceValueHistoryRepository.saveAndFlush(device);
		return device;
	}
	
	public DeviceValueHistory delete(DeviceValueHistory device) {
		deviceValueHistoryRepository.delete(device);
		return device;
	}
}
