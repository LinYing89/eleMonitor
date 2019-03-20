package com.bairock.eleMonitor.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.DeviceGroupService;
import com.bairock.eleMonitor.service.DeviceService;
import com.bairock.eleMonitor.service.SubstationService;

@Controller
@RequestMapping("/devGroup")
public class DeviceGroupController {

	@Autowired
	private DeviceGroupService deviceGroupService;
	@Autowired
	private SubstationService substationService;
	@Autowired
	private DeviceService deviceService;

	@GetMapping("/{substationId}/{devGroupId}")
	public String getMsgManager(@PathVariable long substationId, @PathVariable long devGroupId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		List<DeviceGroup> listDevGroup = substation.getListDeviceGroup();
		// 获取没有组的设备
		List<Device> listDevice = substation.findDeviceNoGroup();
		model.addAttribute("substation", substation);
		model.addAttribute("listDevice", listDevice);
		if (listDevGroup.size() > 0) {
			if (devGroupId == 0) {
				model.addAttribute("devGroup", listDevGroup.get(0));
			} else {
				boolean haved = false;
				for (DeviceGroup s : listDevGroup) {
					if (s.getId() == devGroupId) {
						model.addAttribute("devGroup", s);
						haved = true;
						break;
					}
				}
				if (!haved) {
					model.addAttribute("devGroup", listDevGroup.get(0));
				}
			}
		}

		return "devices/devGroup";
	}

	@ResponseBody
	@GetMapping("/{substationId}")
	public List<Device> findDeviceNoGroup(@PathVariable long substationId) {
		Substation substation = substationService.findBySubstationId(substationId);
		List<Device> listDevice = substation.findDeviceNoGroup();
		return listDevice;
	}

	@PostMapping("/{substationId}")
	public String addDevGroup(@PathVariable long substationId, @ModelAttribute DeviceGroup devGroup) {
		DeviceGroup res = deviceGroupService.addDeviceGroup(substationId, devGroup);
		return "redirect:/devGroup/" + substationId + "/" + res.getId();
	}

	@PostMapping("/edit/{devGroupId}")
	public String editDevGroup(@PathVariable long devGroupId, @ModelAttribute DeviceGroup devGroup) {
		DeviceGroup res = deviceGroupService.editDeviceGroup(devGroupId, devGroup);
		return "redirect:/devGroup/" + res.getSubstation().getId() + "/" + res.getId();
	}

	@GetMapping("/del/{devGroupId}")
	public String deleteDevGroup(@PathVariable long devGroupId, Model model) {
		DeviceGroup res = deviceGroupService.findById(devGroupId);
		List<Device> listDev = new ArrayList<>(res.getListDevice());
		res.removeAllDevice();
		for (Device dev : listDev) {
			deviceService.update(dev);
		}
		Substation station = res.getSubstation();
		station.removeDeviceGroup(res);
		deviceGroupService.deleteDeviceGroup(res);
		return "redirect:/devGroup/" + station.getId() + "/0";
	}

	@PostMapping("/add/device/{devGroupId}")
	public String addDevice(@PathVariable long devGroupId, @RequestParam long deviceId) {
		DeviceGroup res = deviceGroupService.findById(devGroupId);
		Device dev = deviceService.findById(deviceId);
		res.addDevice(dev);
		deviceGroupService.update(res);
		deviceService.update(dev);
		return "redirect:/devGroup/" + res.getSubstation().getId() + "/" + res.getId();
	}

	@GetMapping("/del/device/{devGroupId}/{deviceId}")
	public String deleteDevice(@PathVariable long devGroupId, @PathVariable long deviceId) {
		DeviceGroup res = deviceGroupService.findById(devGroupId);
		Device dev = res.findDeviceById(deviceId);
		if (null != dev) {
			res.removeDevice(dev);
			deviceGroupService.update(res);
			deviceService.update(dev);
		}
		return "redirect:/devGroup/" + res.getSubstation().getId() + "/" + res.getId();
	}

	/**
	 * 为设备排序, 上移还是下移根据action选择
	 * 
	 * @param action     0为上移, 1为下移
	 * @param devGroupId
	 * @param deviceId
	 * @return
	 */
	@GetMapping("/move/device/{action}/{devGroupId}/{deviceId}")
	public String moveDevice(@PathVariable int action, @PathVariable long devGroupId, @PathVariable long deviceId) {
		DeviceGroup res = deviceGroupService.findById(devGroupId);
		Device dev = res.findDeviceById(deviceId);
		Device[] devs = null;
		if (action == 0) {
			devs = res.moveUpDevice(dev);
		} else {
			devs = res.moveDownDevice(dev);
		}
		if (null != devs) {
			deviceService.update(devs[0]);
			deviceService.update(devs[1]);
		}
		return "redirect:/devGroup/" + res.getSubstation().getId() + "/" + res.getId();
	}
}
