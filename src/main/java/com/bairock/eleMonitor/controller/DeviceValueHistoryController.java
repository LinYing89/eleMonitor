package com.bairock.eleMonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceValueHistory;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.DeviceService;
import com.bairock.eleMonitor.service.DeviceValueHistoryService;
import com.bairock.eleMonitor.service.SubstationService;

@Controller
@RequestMapping("/valueHistory")
public class DeviceValueHistoryController {

	@Autowired
	private DeviceValueHistoryService deviceValueHistoryController;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private SubstationService substationService;

	@GetMapping("/{substationId}/{deviceId}")
	public String getHistory(@PathVariable long substationId, @PathVariable long deviceId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		List<Device> listValueDevice = substation.findAllValueDevice();
		List<Device> listCtrlDevice = substation.findAllCtrlDevice();
		Device dev = null;
		if (deviceId == 0) {
			if (!listValueDevice.isEmpty()) {
				dev = listValueDevice.get(0);
			} else if (!listCtrlDevice.isEmpty()) {
				dev = listCtrlDevice.get(0);
			}
		} else {
			dev = deviceService.findById(deviceId);
		}
		if (null != dev) {
			List<DeviceValueHistory> list = deviceValueHistoryController.findAllByDeviceId(dev.getId());
			model.addAttribute("substation", substation);
			model.addAttribute("device", dev);
			model.addAttribute("listCtrlDevice", listCtrlDevice);
			model.addAttribute("listValueDevice", listValueDevice);
			model.addAttribute("listValueHistory", list);
		}
		return "device/valueHistory";
	}
}
