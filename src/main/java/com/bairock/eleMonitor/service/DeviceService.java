package com.bairock.eleMonitor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.repository.DeviceRepository;

@Service
public class DeviceService {

	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private CollectorService collectorService;
	
	
	public Device findById(long deviceId) {
		Optional<Device> option = deviceRepository.findById(deviceId);
		return option.orElse(null);
	}
	
	public Device addDevice(long collectorId, Device device) {
		Collector collector = collectorService.findById(collectorId);
		if(null == collector) {
			return null;
		}
		collector.addDevice(device);
		deviceRepository.saveAndFlush(device);
		return device;
	}
	
	public Device editDevice(long deviceId, Device device) {
		Device res = findById(deviceId);
		if(null != res) {
			res.setName(device.getName());
			res.setBeginAddress(device.getBeginAddress());
			res.setDataLength(device.getDataLength());
			res.setByteOrder(device.getByteOrder());
			res.setValueType(device.getValueType());
			res.setValueFormat(device.getValueFormat());
			res.setCoefficient(device.getCoefficient());
			res.setUnit(device.getUnit());
			deviceRepository.saveAndFlush(res);
		}
		return res;
	}
	
	public Device deleteDevice(Device device) {
		deviceRepository.delete(device);
		return device;
	}
}