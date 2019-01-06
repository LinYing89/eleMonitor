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
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.data.webData.DeviceTreeNode;
import com.bairock.eleMonitor.data.webData.FormResult;
import com.bairock.eleMonitor.service.MsgManagerService;
import com.bairock.eleMonitor.service.SubstationService;

/**
 * 通信管理机
 * @author 44489
 *
 */
//@Controller
//@RequestMapping("/msgManager")
public class MsgManagerController {

//	@Autowired
//	private MsgManagerService msgManagerService;
//	@Autowired
//	private SubstationService substationService;
//	
//	@GetMapping("/{substationId}/{msgManagerId}")
//	public String getMsgManager(@PathVariable long substationId, @PathVariable long msgManagerId, Model model) {
//		Substation substation = substationService.findBySubstationId(substationId);
//		List<MsgManager> listMsgManager = substation.getListMsgManager();
////		List<Substation> listSubstation = substationService.findByStationId(stationId);
//		model.addAttribute("substation", substation);
//		model.addAttribute("substationId", substationId);
//		model.addAttribute("substationName", substation.getName());
//		model.addAttribute("listMsgManager", listMsgManager);
//		if(listMsgManager.size() > 0) {
//			MsgManager mm = null;
//			if(msgManagerId == 0) {
//				mm = listMsgManager.get(0);
//				//尝试获取缓存中的对象
//				mm = msgManagerService.findByMsgManagerCode(mm.getCode());
//			}else {
////				MsgManager m1 = msgManagerService.findByMsgManagerId(msgManagerId);
////				mm = msgManagerService.findByMsgManagerCode(m1.getCode());
//				mm = msgManagerService.findByMsgManagerId(msgManagerId);
//			}
//			model.addAttribute("msgManager", mm);
//			
////			if(msgManagerId == 0) {
////				model.addAttribute("msgManager", listMsgManager.get(0));
////			}else {
////				boolean haved = false;
////				for(MsgManager s : listMsgManager) {
////					if(s.getId() == msgManagerId) {
////						model.addAttribute("msgManager", s);
////						haved = true;
////						break;
////					}
////				}
////				if(!haved) {
////					model.addAttribute("msgManager", listMsgManager.get(0));
////				}
////			}
//		}
//		
////		return "device/msgManager";
//		return "devices/substation";
//	}
//	
//	@GetMapping("/{substationId}")
//	public String getSubstationDevices(@PathVariable long substationId, Model model) {
//		Substation substation = substationService.findBySubstationId(substationId);
//		model.addAttribute("substation", substation);
//		return "devices/substation";
//	}
//	
//	@ResponseBody
//	@GetMapping("/allDevice/{substationId}")
//	public List<DeviceTreeNode> getDeviceTree(@PathVariable long substationId) {
//		List<DeviceTreeNode> list = new ArrayList<>();
//		Substation substation = substationService.findBySubstationId(substationId);
//		
//		List<MsgManager> listMsgManager = substation.getListMsgManager();
//		for(MsgManager mm : listMsgManager) {
//			DeviceTreeNode dtn = new DeviceTreeNode();
//			list.add(dtn);
//			dtn.setText(mm.getName());
//			dtn.setHref("/msgManager/" + substationId + "/" + mm.getId());
//			dtn.setDeviceId(mm.getId());
//			List<DeviceTreeNode> listCollector = new ArrayList<>();
//			for(Collector c : mm.getListCollector()) {
//				DeviceTreeNode dtnCollector = new DeviceTreeNode();
//				listCollector.add(dtnCollector);
//				dtnCollector.setText(c.getName());
//				dtnCollector.setHref("cc");
//				dtnCollector.setDeviceId(c.getId());
//				List<DeviceTreeNode> listDevice = new ArrayList<>();
//				for(Device d : c.getListDevice()) {
//					DeviceTreeNode dtnDevice = new DeviceTreeNode();
//					listDevice.add(dtnDevice);
//					dtnDevice.setText(d.getName());
//					dtnDevice.setHref("dd");
//					dtnDevice.setDeviceId(d.getId());
//				}
//				dtnCollector.setNodes(listDevice);
//			}
//			dtn.setNodes(listCollector);
//		}
//		return list;
//	}
//	
//	@ResponseBody
//	@PostMapping("/{substationId}")
//	public FormResult addMsgManager(@PathVariable long substationId, @ModelAttribute MsgManager msgManager) {
//		MsgManager res = msgManagerService.addMsgManager(substationId, msgManager);
//		FormResult result = new FormResult();
//		if(null == res) {
//			//通信机号重复
//			result.setCode(1);
//			result.setMsg("通信机号重复");
//		}else {
//			result.setCode(0);
//			result.setMsg("添加成功");
//		}
//		return result;
////		return "redirect:/msgManager/" + substationId + "/" + res.getId();
//	}
//	
//	@ResponseBody
//	@PostMapping("/edit/{msgManagerId}")
//	public FormResult editMsgManager(@PathVariable long msgManagerId, @ModelAttribute MsgManager msgManager) {
//		MsgManager res = msgManagerService.editMsgManager(msgManagerId, msgManager);
//		FormResult result = new FormResult();
//		if(null == res) {
//			//通信机号重复
//			result.setCode(1);
//			result.setMsg("通信机号重复");
//		}else {
//			result.setCode(0);
//			result.setMsg("添加成功");
//		}
//		return result;
//		//return "redirect:/msgManager/" + res.getSubstation().getId() + "/" + res.getId();
//	}
//	
//	@GetMapping("/del/{msgManagerId}")
//	public String deleteMsgManager(@PathVariable long msgManagerId) {
//		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
//		Substation station = msgManager.getSubstation();
//		msgManagerService.deleteMsgManager(msgManager);
//		station.removeMsgManager(msgManager);
//		return "redirect:/msgManager/" + station.getId() + "/0";
//	}
//	
//	@GetMapping("/configurationList/{msgManagerId}")
//	public String getConfigurationList(@PathVariable long msgManagerId, Model model) {
//		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
//		model.addAttribute("msgManager", msgManager);
//		return "device/configurationList";
//	}
}
