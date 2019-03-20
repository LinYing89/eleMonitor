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
import org.springframework.web.bind.annotation.ResponseBody;

import com.bairock.eleMonitor.data.Collector;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceCategory;
import com.bairock.eleMonitor.data.DeviceEventMessage;
import com.bairock.eleMonitor.data.DeviceGroup;
import com.bairock.eleMonitor.data.LineTemFormGroup;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.Place;
import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.data.cable.EleCable;
import com.bairock.eleMonitor.data.cable.Phase;
import com.bairock.eleMonitor.data.webData.DeviceTreeNode;
import com.bairock.eleMonitor.service.PlaceService;
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
	@Autowired
	private PlaceService placeService;
	
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
			
			//电力组特殊处理
			List<DeviceGroup> listEleGroup = substation.findEleDeviceGroup();
			//将电力设备组转为线缆组
			List<EleCable> listCable = changeEleGroupToEleCableGroup(listEleGroup);
			
			model.addAttribute("listValueDevice", listValueDevice);
			model.addAttribute("listValueGroup", listValueGroup);
			model.addAttribute("listCtrlDevice", listCtrlDevice);
			model.addAttribute("listCtrlGroup", listCtrlGroup);
			model.addAttribute("listLineTemFormGroup", listLineTemFormGroup);
			model.addAttribute("listCable", listCable);
			
//			List<DeviceEventMessage> listEvent = substation.findDeviceEventMessages();
			List<DeviceEventMessage> listEvent = substationService.findAllEvent(substation);
			model.addAttribute("listEvent", listEvent);
		}
		
		return "devices/deviceDataMain";
	}
	
	//将电力设备组转为线缆组
	private List<EleCable> changeEleGroupToEleCableGroup(List<DeviceGroup> listEleGroup) {
		List<EleCable> listCable = new ArrayList<>();
		//一个电力组一条线缆
		for(DeviceGroup group : listEleGroup) {
			EleCable cable = new EleCable();
			cable.setGroupName(group.getName());
			listCable.add(cable);
			//一条线缆3相
			Phase pa = new Phase();
			Phase pb = new Phase();
			Phase pc = new Phase();
			cable.setPhaseA(pa);
			cable.setPhaseB(pb);
			cable.setPhaseC(pc);
			//一相采集3个值, 电压, 电流, 功率因数
			for(Device dev : group.getListDevice()) {
				if(dev.getName().contains("A")) {
					setPhaseValue(pa, dev);
				}else if(dev.getName().contains("B")) {
					setPhaseValue(pb, dev);
				}else if(dev.getName().contains("C")) {
					setPhaseValue(pc, dev);
				}else if(dev.getDeviceCategory() == DeviceCategory.RESIDUE_ELE) {
					cable.setResidueCurrent(dev.getValue());
					cable.setResidueCurrentId(dev.getId());
				}
			}
		}
		return listCable;
	}
	
	private void setPhaseValue(Phase phase, Device dev) {
		switch(dev.getDeviceCategory()) {
		case VOLTAGE:
			phase.setVoltage(dev.getValue());
			phase.setVoltageId(dev.getId());
			break;
		case CURRENT:
			phase.setCurrent(dev.getValue());
			phase.setCurrentId(dev.getId());
			break;
		case FACTOR:
			phase.setFactor(dev.getValue());
			phase.setFactorId(dev.getId());
			break;
		case PHASE_TEM:
			phase.setTem(dev.getValue());
			phase.setTemId(dev.getId());
			break;
		default:
			break;
		}
	}
	
	@ResponseBody
	@GetMapping("/allDevice/{stationId}")
	public List<DeviceTreeNode> getDeviceTree(@PathVariable long stationId) {
		List<DeviceTreeNode> list = new ArrayList<>();
		Station station = stationService.findStation(stationId);
		List<Substation> listSubstation = station.getListSubstation();
		for(Substation substation : listSubstation) {
			DeviceTreeNode dtnSubstation = new DeviceTreeNode();
			list.add(dtnSubstation);
			dtnSubstation.setText(substation.getName());
			dtnSubstation.setHref("/substation/" + stationId + "/" + substation.getId());
			dtnSubstation.setDeviceId(substation.getId());
			
			List<DeviceTreeNode> listMsgManager = new ArrayList<>();
			for(MsgManager mm : substation.getListMsgManager()) {
				DeviceTreeNode dtn = new DeviceTreeNode();
				listMsgManager.add(dtn);
				dtn.setText(mm.getName());
				dtn.setHref("/msgManager/" + substation.getId() + "/" + mm.getId());
				dtn.setDeviceId(mm.getId());
				List<DeviceTreeNode> listCollector = new ArrayList<>();
				for(Collector c : mm.getListCollector()) {
					DeviceTreeNode dtnCollector = new DeviceTreeNode();
					listCollector.add(dtnCollector);
					dtnCollector.setText(c.getName());
					dtnCollector.setHref("/collector/find/"+c.getId());
					dtnCollector.setDeviceId(c.getId());
				}
				dtn.setNodes(listCollector);
			}
			dtnSubstation.setNodes(listMsgManager);
		}
		
		return list;
	}
	
	@RequestMapping("/info/{substationId}")
	public String getSubtationInfo(@PathVariable long substationId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		int[] stateCount = substation.findMsgManagerStateCount();
		model.addAttribute("substation", substation);
		model.addAttribute("msgSuccessCount", stateCount[0]);
		model.addAttribute("msgOfflineCount", stateCount[1]);
		return "/map/map::substation_info";
	}
	
	@GetMapping("/reload/{stationId}/{substationId}")
	public String reload(@PathVariable long stationId, @PathVariable long substationId) {
		substationService.reloadCache(substationId);
		return "redirect:/substation/" + stationId + "/" + substationId;
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
	public String deleteSubstation(@PathVariable long substationId) {
		Substation substation = substationService.findBySubstationId(substationId);
		Station station = substation.getStation();
		substationService.deleteSubstation(substation);
		station.removeSubstation(substation);
		return "redirect:/substation/" + station.getId() + "/0";
	}
	
	@GetMapping("/page/place/{substationId}")
	public String getPlacePage(@PathVariable long substationId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		model.addAttribute("substation", substation);
		model.addAttribute("listPlace", substation.getListPlace());
		return "devices/place";
	}
	
	@PostMapping("/add/place/{substationId}")
	public String addPlace(@PathVariable long substationId, @ModelAttribute Place place) {
		placeService.addPlace(substationId, place);
		return "redirect:/substation/page/place/" + substationId;
	}
	
	@GetMapping("/del/place/{placeId}")
	public String delPlace(@PathVariable long placeId) {
		Place place = placeService.deletePlace(placeId);
		Substation substation = substationService.findBySubstationId(place.getSubstation().getId());
		return "redirect:/substation/page/place/" + substation.getId();
	}
	
	@PostMapping("/edit/place/{placeId}")
	public String editPlace(@PathVariable long placeId, @ModelAttribute Place place) {
		Place placeDb = placeService.editPlace(placeId, place);
		Substation substation = substationService.findBySubstationId(placeDb.getSubstation().getId());
		return "redirect:/substation/page/place/" + substation.getId();
	}
}
