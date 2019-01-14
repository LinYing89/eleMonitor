package com.bairock.eleMonitor.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.data.webData.DeviceTreeNode;
import com.bairock.eleMonitor.data.webData.StationTreeNode;
import com.bairock.eleMonitor.service.StationService;

@Controller
@RequestMapping("/station")
public class StationController {

	@Autowired
	private StationService stationService;

	@GetMapping("/mystations/{stationId}")
	public String myStations(Model model, @PathVariable long stationId) {
		List<Station> list = stationService.findAll();
		if (list.size() > 0) {
			Station station = null;
			if (stationId == 0) {
				station = list.get(0);
			} else {
				for (Station s : list) {
					if (s.getId() == stationId) {
						station = s;
						break;
					}
				}
			}
			if(null != station) {
				model.addAttribute("station", station);
				if(!station.getListSubstation().isEmpty()) {
					Substation substation = station.getListSubstation().get(0);
					model.addAttribute("substation", substation);
					int[] stateCount = substation.findMsgManagerStateCount();
					model.addAttribute("msgSuccessCount", stateCount[0]);
					model.addAttribute("msgOfflineCount", stateCount[1]);
				}
			}
		}
		int[] stationStateCount = new int[3];
		for (Station s : list) {
			switch (s.getState()) {
			case NORMAL:
				stationStateCount[0]++;
				break;
			case OFFLINE:
				stationStateCount[1]++;
				break;
			case ALARM:
				stationStateCount[2]++;
				break;
			}
		}
		model.addAttribute("stationStateCount", stationStateCount);
		model.addAttribute("listStation", list);
		return "map/map";
//		return "redirect:/map";
	}

	@ResponseBody
	@GetMapping("/find/stationstateCount")
	public int[] findStationstateCount() {
		List<Station> list = stationService.findAll();
		int[] stationStateCount = new int[3];
		for (Station s : list) {
			switch (s.getState()) {
			case NORMAL:
				stationStateCount[0]++;
				break;
			case OFFLINE:
				stationStateCount[1]++;
				break;
			case ALARM:
				stationStateCount[2]++;
				break;
			}
		}
		return stationStateCount;
	}
	
	/**
	 * 获取站点的树形结构
	 */
	@ResponseBody
	@GetMapping("/find/treeNode/allStation")
	public List<StationTreeNode> findAllStationNodes() {
		List<StationTreeNode> listNode = new ArrayList<>();
		List<Station> listStation = stationService.findAll();
		for (Station station : listStation) {
			StationTreeNode dtnStation = new StationTreeNode();
			listNode.add(dtnStation);
			dtnStation.setType("station");
			dtnStation.setText(station.getName());
			dtnStation.setHref("/substation/" + station.getId() + "/0");
			dtnStation.setDeviceId(station.getId());
			dtnStation.setLat(station.getLat());
			dtnStation.setLng(station.getLng());
			dtnStation.setStateCode(station.getState().getCode());
			int countSubstation = 0;
			List<DeviceTreeNode> listSubstationNode = new ArrayList<>();
			dtnStation.setNodes(listSubstationNode);
			for (Substation substation : station.getListSubstation()) {
				DeviceTreeNode dtnSubstation = new DeviceTreeNode();
				listSubstationNode.add(dtnSubstation);
				dtnSubstation.setType("substation");
				dtnSubstation.setText(substation.getName());
				dtnSubstation.setHref("/substation/" + station.getId() + "/" + substation.getId());
				dtnSubstation.setDeviceId(substation.getId());
				int countMsgManager = substation.countUnsccessMsgManager();
				dtnSubstation.setTag(String.valueOf(countMsgManager));
				countSubstation += countMsgManager;
			}
			dtnStation.setTag(String.valueOf(countSubstation));
		}
		return listNode;
	}
	
	@RequestMapping("/info/{stationId}")
	public String getStationInfo(@PathVariable long stationId, Model model) {
		Station station = stationService.findStation(stationId);
		model.addAttribute("station", station);
		return "map/map::station_info";
	}

	@PostMapping("/add")
	public String addStationSubmit(@ModelAttribute Station station) {
		station.setRegisterTime(new Date());
		stationService.save(station);
		return "redirect:/station/mystations/" + station.getId();
	}

	@PostMapping("/edit/{stationId}")
	public String editStation(@PathVariable long stationId, @ModelAttribute Station station) {
		stationService.edit(stationId, station);
		return "redirect:/station/mystations/" + station.getId();
	}

	@GetMapping("/del/{stationId}")
	public String deleteStation(@PathVariable long stationId) {
		stationService.deleteById(stationId);
		return "redirect:/station/mystations/0";
	}
}
