package com.bairock.eleMonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		model.addAttribute("stationId", stationId);
		model.addAttribute("stationName", station.getName());
		model.addAttribute("listSubstation", listSubstation);
		if(listSubstation.size() > 0) {
			if(substationId == 0) {
				model.addAttribute("substation", listSubstation.get(0));
			}else {
				boolean haved = false;
				for(Substation s : listSubstation) {
					if(s.getId() == substationId) {
						model.addAttribute("substation", s);
						haved = true;
						break;
					}
				}
				if(!haved) {
					model.addAttribute("substation", listSubstation.get(0));
				}
			}
		}
		
		return "substation/substation";
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
