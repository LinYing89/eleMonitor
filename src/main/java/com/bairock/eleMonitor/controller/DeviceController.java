package com.bairock.eleMonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.service.CollectorService;
import com.bairock.eleMonitor.service.DeviceService;

@Controller
@RequestMapping("/device")
public class DeviceController {

	@Autowired
	private CollectorService collectorService; 
	@Autowired
	private DeviceService deviceService; 
	
	@GetMapping("/{collectorId}")
	public String getDevices(@PathVariable long collectorId, Model model) {
		Collector collector = collectorService.findById(collectorId);
		model.addAttribute("collector", collector);
		return "device/device";
	}
	
	@PostMapping("/{collectorId}")
	public String addDevice(@PathVariable long collectorId, @ModelAttribute Device device) {
		deviceService.addDevice(collectorId, device);
		return "redirect:/device/" + collectorId;
	}
	
	@PostMapping("/edit/{deviceId}")
	public String editDevice(@PathVariable long deviceId, @ModelAttribute Device device) {
		Device res = deviceService.editDevice(deviceId, device);
		return "redirect:/device/" + res.getCollector().getId();
	}
	
	@GetMapping("/del/{deviceId}")
	public String deleteDevice(@PathVariable long deviceId) {
		Device device = deviceService.findById(deviceId);
		DeviceGroup dg = device.getDeviceGroup();
		if(dg != null) {
			dg.removeDevice(device);
		}
		Collector collector = device.getCollector();
		collector.removeDevice(device);
		
		deviceService.deleteDevice(device);
		return "redirect:/device/" + collector.getId();
	}
}
