package com.bairock.eleMonitor.controller.devices;

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
		model.addAttribute("substation", substation);
		if(listMsgManager.size() > 0) {
			MsgManager mm = null;
			if(msgManagerId == 0) {
				mm = listMsgManager.get(0);
				//尝试获取缓存中的对象
				mm = msgManagerService.findByMsgManagerCode(mm.getCode());
			}else {
				mm = msgManagerService.findByMsgManagerId(msgManagerId);
			}
			model.addAttribute("msgManager", mm);
		}
		
		return "devices/msgManager";
	}
	
	@GetMapping("/{substationId}")
	public String getSubstationDevices(@PathVariable long substationId, Model model) {
		Substation substation = substationService.findBySubstationId(substationId);
		model.addAttribute("substation", substation);
		return "devices/substation";
	}
	
	@ResponseBody
	@GetMapping("/allDevice/{substationId}")
	public List<DeviceTreeNode> getDeviceTree(@PathVariable long substationId) {
		List<DeviceTreeNode> list = new ArrayList<>();
		Substation substation = substationService.findBySubstationId(substationId);
		
		List<MsgManager> listMsgManager = substation.getListMsgManager();
		for(MsgManager mm : listMsgManager) {
			DeviceTreeNode dtn = new DeviceTreeNode();
			list.add(dtn);
			dtn.setText(mm.getName());
			dtn.setHref("/msgManager/" + substationId + "/" + mm.getId());
			dtn.setDeviceId(mm.getId());
			List<DeviceTreeNode> listCollector = new ArrayList<>();
			for(Collector c : mm.getListCollector()) {
				DeviceTreeNode dtnCollector = new DeviceTreeNode();
				listCollector.add(dtnCollector);
				dtnCollector.setText(c.getName());
				dtnCollector.setHref("/collector/find/"+c.getId());
				dtnCollector.setDeviceId(c.getId());
				List<DeviceTreeNode> listDevice = new ArrayList<>();
				for(Device d : c.getListDevice()) {
					DeviceTreeNode dtnDevice = new DeviceTreeNode();
					listDevice.add(dtnDevice);
					dtnDevice.setText(d.getName());
					dtnDevice.setHref("/linkage/" + d.getId() + "/0");
					dtnDevice.setDeviceId(d.getId());
				}
				dtnCollector.setNodes(listDevice);
			}
			dtn.setNodes(listCollector);
		}
		return list;
	}
	
	@ResponseBody
	@PostMapping("/add/{substationId}")
	public FormResult addMsgManager(@PathVariable long substationId, @ModelAttribute MsgManager msgManager) {
		MsgManager res = msgManagerService.addMsgManager(substationId, msgManager);
		FormResult result = new FormResult();
		if(null == res) {
			//通信机号重复
			result.setCode(1);
			result.setMsg("通信机号重复");
		}else {
			result.setCode(0);
			result.setMsg("添加成功");
		}
		return result;
//		return "redirect:/msgManager/" + substationId + "/" + res.getId();
	}
	
	@ResponseBody
	@PostMapping("/edit/{oldCode}")
	public FormResult editMsgManager(@PathVariable int oldCode, @ModelAttribute MsgManager msgManager) {
		MsgManager res = msgManagerService.editMsgManager(oldCode, msgManager);
		FormResult result = new FormResult();
		if(null == res) {
			//通信机号重复
			result.setCode(1);
			result.setMsg("通信机号重复");
		}else {
			result.setCode(0);
			result.setMsg("添加成功");
		}
		return result;
		//return "redirect:/msgManager/" + res.getSubstation().getId() + "/" + res.getId();
	}
	
	@GetMapping("/del/{msgManagerId}")
	public String deleteMsgManager(@PathVariable long msgManagerId) {
		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
		Substation station = msgManager.getSubstation();
		msgManagerService.deleteMsgManager(msgManager);
		station.removeMsgManager(msgManager);
		return "redirect:/msgManager/" + station.getId() + "/0";
	}
	
	@GetMapping("/configurationList/{msgManagerId}")
	public String getConfigurationList(@PathVariable long msgManagerId, Model model) {
		MsgManager msgManager = msgManagerService.findByMsgManagerId(msgManagerId);
		model.addAttribute("msgManager", msgManager);
		return "device/configurationList";
	}

}
