package com.bairock.eleMonitor.controller.devices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.Linkage;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.DeviceService;
import com.bairock.eleMonitor.service.LinkageService;

@Controller
@RequestMapping("/linkage")
public class LinkageController {

	@Autowired
	private LinkageService linkageService;
	@Autowired
	private DeviceService deviceService;
	
	@GetMapping("/{deviceId}/{linkageId}")
	public String getLinkage(@PathVariable long deviceId, @PathVariable long linkageId, Model model) {
		Device device = deviceService.findById(deviceId);
		Substation substation = device.getCollector().getMsgManager().getSubstation();
		
		if(!device.getListLinkage().isEmpty()) {
			Linkage linkage = null;
			if(linkageId == 0) {
				linkage = device.getListLinkage().get(0);
			}else {
				linkage = linkageService.findById(linkageId);
				if(null == linkage) {
					linkage = device.getListLinkage().get(0);
				}
			}
			model.addAttribute("linkage", linkage);
		}
		
		model.addAttribute("substation", substation);
		model.addAttribute("device", device);
		model.addAttribute("listValueDevice", substation.findAllValueDevice());
		model.addAttribute("listCtrlDevice", substation.findAllCtrlDevice());
		return "devices/linkage";
	}
	
	@PostMapping("/add/{deviceId}")
	public String addDevice(@PathVariable long deviceId, @ModelAttribute Linkage linkage) {
		linkage = linkageService.addDevice(deviceId, linkage);
		return "redirect:/linkage/" + deviceId +"/" + linkage.getId();
	}
	
	@PostMapping("/edit/{linkageId}")
	public String editDevice(@PathVariable long linkageId, @ModelAttribute Linkage linkage) {
		Linkage res = linkageService.editDevice(linkageId, linkage);
		return "redirect:/linkage/" + res.getDevice().getId() + "/" + linkage.getId();
	}
	
	@GetMapping("/del/{linkageId}")
	public String deleteDevice(@PathVariable long linkageId) {
		Linkage res = linkageService.findById(linkageId);
		Device device = res.getDevice();
		device.removeLinkage(res);
		
		linkageService.delete(res);
		return "redirect:/linkage/" + device.getId() + "/0";
	}
}
