package com.bairock.eleMonitor.service;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceEventMessage;
import com.bairock.eleMonitor.data.webData.DevWebData;
import com.bairock.eleMonitor.repository.DeviceRepository;

@Service
public class DeviceService {

	private SimpMessageSendingOperations messaging;
	
	@Resource
	private DeviceService self;
	
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private DeviceValueHistoryService deviceValueHistoryService;
	@Autowired
	private CollectorService collectorService;
	
	@Autowired
	public DeviceService(SimpMessageSendingOperations messaging) {
		this.messaging = messaging;
	}
	
	@Cacheable(value = "device", key = "#deviceId")
	public Device findById(long deviceId) {
		Optional<Device> option = deviceRepository.findById(deviceId);
		Device d = option.orElse(null);
		return d;
	}
	
	@CachePut(value = "device", key = "#result.id")
	public Device addDevice(long collectorId, Device device) {
		Collector collector = collectorService.findById(collectorId);
		if(null == collector) {
			return null;
		}
		collector.addDevice(device);
		deviceRepository.saveAndFlush(device);
		return device;
	}
	
//	@CachePut(value = "device", key = "#result.id")
	public Device editDevice(long deviceId, Device device) {
		Device res = self.findById(deviceId);
		if(null != res) {
			if(!res.getName().equals(device.getName())) {
				res.setName(device.getName());
				deviceValueHistoryService.update(res);
			}
			res.setPlace(device.getPlace());
			res.setBeginAddress(device.getBeginAddress());
			res.setDataLength(device.getDataLength());
			res.setByteOrder(device.getByteOrder());
			res.setValueType(device.getValueType());
			res.setAlarmTriggerValue(device.getAlarmTriggerValue());
			res.setValueFormat(device.getValueFormat());
			res.setCoefficient(device.getCoefficient());
			res.setUnit(device.getUnit());
			res.setDeviceCategory(device.getDeviceCategory());
			res.setIcon(device.getIcon());
			deviceRepository.saveAndFlush(res);
		}
		return res;
	}
	
	public void update(Device device) {
		deviceRepository.saveAndFlush(device);
	}
	
	@CacheEvict(value = "device", key = "#result.id")
	public Device deleteDevice(Device device) {
//		deviceRepository.deleteById(device.getId());
		deviceRepository.delete(device);
		return device;
	}
	
	/**
	 * 向网页发送设备状态
	 * @param substationId
	 * @param devWebData
	 */
	public void broadcastValueChanged(long substationId, DevWebData devWebData) {
		String topic = String.format("/topic/%d/devState", substationId);
//		String topic = "/topic/admin/devState";
		messaging.convertAndSend(topic, devWebData);
	}
	
	public void broadcastEvent(long substationId, DeviceEventMessage event) {
		String topic = String.format("/topic/%d/devEvent", substationId);
//		String topic = "/topic/admin/devEvent";
		messaging.convertAndSend(topic, event);
	}
	
	public void broadcastAdminEvent(DeviceEventMessage event) {
		String topic = String.format("/topic/admin/devEvent");
//		String topic = "/topic/admin/devEvent";
		messaging.convertAndSend(topic, event);
	}
}
