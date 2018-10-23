package com.bairock.eleMonitor.controller;

import java.util.Date;
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
import com.bairock.eleMonitor.service.StationService;

@Controller
@RequestMapping("/station")
public class StationController {
	
	@Autowired
	private StationService stationService;
	
	@GetMapping("/mystations")
	public String myStations(Model model) {
		List<Station> list = stationService.findAll();
		model.addAttribute("listStation", list);
		return "sysset/mystations";
	}
	
	@GetMapping("/add")
	public String getStationAdd() {
		return "sysset/stationAdd";
	}
	
	@PostMapping("/add")
	public String addStationSubmit(@ModelAttribute Station station) {
		station.setRegisterTime(new Date());
		stationService.save(station);
		return "redirect:/station/mystations";
	}
	
	@PostMapping("/edit/{stationId}")
	public String editStation(@PathVariable long stationId, @ModelAttribute Station station) {
		stationService.edit(stationId, station);
		return "redirect:/station/mystations";
	}
	
	@GetMapping("/delete/{stationId}")
	public String deleteStation(@PathVariable long stationId) {
		stationService.deleteById(stationId);
		return "redirect:/station/mystations";
	}
}
