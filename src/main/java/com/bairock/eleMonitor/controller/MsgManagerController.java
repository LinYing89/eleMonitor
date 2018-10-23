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

import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.Substation;
import com.bairock.eleMonitor.service.MsgManagerService;
import com.bairock.eleMonitor.service.SubstationService;

/**
 * 通信管理机
 * @author 44489
 *
 */
@Controller
@RequestMapping("/msgManager")
public class MsgManagerController {

	@Autowired
	private MsgManagerService msgManagerService;
	@Autowired
	private SubstationService substationService;
	
	@GetMapping("/{substationId}/{msgManagerId}")
	public String getMsgManager(@PathVariable long substationId, @PathVariable long msgManagerId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		List<MsgManager> listMsgManager = substation.getListMsgManager();
//		List<Substation> listSubstation = substationService.findByStationId(stationId);
		model.addAttribute("substationId", substationId);
		model.addAttribute("substationName", substation.getName());
		model.addAttribute("listMsgManager", listMsgManager);
		if(listMsgManager.size() > 0) {
			if(msgManagerId == 0) {
				model.addAttribute("msgManager", listMsgManager.get(0));
			}else {
				boolean haved = false;
				for(MsgManager s : listMsgManager) {
					if(s.getId() == msgManagerId) {
						model.addAttribute("msgManager", s);
						haved = true;
						break;
					}
				}
				if(!haved) {
					model.addAttribute("msgManager", listMsgManager.get(0));
				}
			}
		}
		
		return "device/msgManager";
	}
	
	@PostMapping("/{substationId}")
	public String addMsgManager(@PathVariable long substationId, @ModelAttribute MsgManager msgManager) {
		MsgManager res = msgManagerService.addMsgManager(substationId, msgManager);
		return "redirect:/msgManager/" + substationId + "/" + res.getId();
	}
	
	@PostMapping("/edit/{msgManagerId}")
	public String editMsgManager(@PathVariable long msgManagerId, @ModelAttribute MsgManager msgManager) {
		MsgManager res = msgManagerService.editMsgManager(msgManagerId, msgManager);
		return "redirect:/msgManager/" + res.getSubstation().getId() + "/" + res.getId();
	}
	
	@GetMapping("/del/{msgManagerId}")
	public String deleteMsgManager(@PathVariable long msgManagerId, Model model) {
		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
		Substation station = msgManager.getSubstation();
		msgManagerService.deleteMsgManager(msgManager);
		station.removeMsgManager(msgManager);
		return "redirect:/msgManager/" + station.getId() + "/0";
	}
}
