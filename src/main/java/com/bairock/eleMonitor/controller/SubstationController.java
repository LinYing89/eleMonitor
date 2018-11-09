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

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.data.LineTemFormGroup;
import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.StationService;
import com.bairock.eleMonitor.service.SubstationService;

/**
 * 变电站
 * @author 44489
 *
 */
@Controller
@RequestMapping("/substation")
public class SubstationController {

	@Autowired
	private SubstationService substationService;
	@Autowired
	private StationService stationService;
	
	/**
	 * 获取站点下所有变电站
	 * @param stationId 站点名称
	 * @param substationId 变电站名称, 默认激活此变电站
	 * @param model
	 * @return
	 */
	@GetMapping("/{stationId}/{substationId}")
	public String getSubstation(@PathVariable long stationId, @PathVariable long substationId, Model model) {
		Station station = stationService.findStation(stationId);
		List<Substation> listSubstation = station.getListSubstation();
//		List<Substation> listSubstation = substationService.findByStationId(stationId);
		model.addAttribute("station", station);
		//model.addAttribute("listSubstation", listSubstation);
		if(listSubstation.size() > 0) {
			Substation substation = null;
			
			if(substationId != 0) {
				substation = substationService.findBySubstationId(substationId);
				if(null == substation) {
					substation = listSubstation.get(0);
					substation = substationService.findBySubstationId(substation.getId());
				}
			}else {
				//尝试从缓存中获取对象
				substation = listSubstation.get(0);
				substation = substationService.findBySubstationId(substation.getId());
			}
			
			model.addAttribute("substation", substation);
			for(DeviceGroup dg : substation.getListDeviceGroup()) {
				for(Device dev : dg.getListDevice()) {
					DeviceGroup d2 = dev.getDeviceGroup();
					System.out.println(d2.toString());
				}
			}
			
			List<Device> listValueDevice = substation.findValueDeviceNoGroup();
			List<DeviceGroup> listValueGroup = substation.findValueDeviceGroup();
			List<Device> listCtrlDevice = substation.findCtrlDeviceNoGroup();
			List<DeviceGroup> listCtrlGroup = substation.findCtrlDeviceGroup();
			//电缆测温组, 需特殊处理
			List<DeviceGroup> listLineTemGroup = substation.findLineTemGroup();
			
			//将电缆测温的组转为LineTemFormGroup组
			List<LineTemFormGroup> listLineTemFormGroup = new ArrayList<>();
			for(DeviceGroup devGroup : listLineTemGroup) {
				LineTemFormGroup lineGroup = new LineTemFormGroup();
				lineGroup.setId(devGroup.getId());
				lineGroup.setName(devGroup.getName());
				for(Device dev : devGroup.getListDevice()) {
					lineGroup.addLineTemDevice(dev);
				}
				//按名称排序
				lineGroup.sort();
				listLineTemFormGroup.add(lineGroup);
			}
			model.addAttribute("listValueDevice", listValueDevice);
			model.addAttribute("listValueGroup", listValueGroup);
			model.addAttribute("listCtrlDevice", listCtrlDevice);
			model.addAttribute("listCtrlGroup", listCtrlGroup);
			model.addAttribute("listLineTemFormGroup", listLineTemFormGroup);
		}
		
		return "substation/substation2";
	}
	
	@PostMapping("/{stationId}")
	public String addSubstation(@PathVariable long stationId, @ModelAttribute Substation substation) {
		Substation res = substationService.addSubStation(stationId, substation);
		return "redirect:/substation/" + stationId + "/" + res.getId();
	}
	
	@PostMapping("/edit/{substationId}")
	public String editSubstation(@PathVariable long substationId, @ModelAttribute Substation substation) {
		Substation res = substationService.editSubStation(substationId, substation);
		return "redirect:/substation/" + res.getStation().getId() + "/" + res.getId();
	}
	
	@GetMapping("/del/{substationId}")
	public String deleteSubstation(@PathVariable long substationId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		Station station = substation.getStation();
		substationService.deleteSubstation(substation);
		station.removeSubstation(substation);
		return "redirect:/substation/" + station.getId() + "/0";
	}
}
