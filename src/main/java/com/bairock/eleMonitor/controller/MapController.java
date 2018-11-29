package com.bairock.eleMonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.service.StationService;

@Controller
public class MapController {

	@Autowired
	private StationService stationService;
	
	@GetMapping(value= {"/", "/map"})
	public String map(Model model) {
		List<Station> list = stationService.findAll();
		model.addAttribute("listStation", list);
		return "map";
	}
}
