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
	
	@GetMapping("/{stationId}")
	public String getSubstation(@PathVariable long stationId, Model model) {
		List<Substation> listSubstation = substationService.findByStationId(stationId);
		model.addAttribute("listSubstation", listSubstation);
		return "substation/substation";
	}
	
	@PostMapping("/{stationId}")
	public String addSubstation(@PathVariable long stationId, @ModelAttribute Substation substation) {
		substationService.addSubStation(stationId, substation);
		return "redirect:/substation/" + stationId;
	}
	
	@GetMapping("/del/{substationId}")
	public String deleteSubstation(@PathVariable long substationId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		Station station = substation.getStation();
		substationService.deleteSubstation(substation);
		station.removeSubstation(substation);
		return "redirect:/substation/" + station.getId();
	}
}
