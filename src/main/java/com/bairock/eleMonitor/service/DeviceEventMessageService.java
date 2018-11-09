package com.bairock.eleMonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.DeviceEventMessage;
import com.bairock.eleMonitor.repository.DeviceEventMessageRepository;

@Service
public class DeviceEventMessageService {

	@Autowired
	private DeviceEventMessageRepository deviceEventMessageRepository;
	
	public DeviceEventMessage add(DeviceEventMessage device) {
		deviceEventMessageRepository.saveAndFlush(device);
		return device;
	}
	
	public DeviceEventMessage delete(DeviceEventMessage device) {
		deviceEventMessageRepository.delete(device);
		return device;
	}
}
