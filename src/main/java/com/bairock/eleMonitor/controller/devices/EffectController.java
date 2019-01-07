package com.bairock.eleMonitor.controller.devices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bairock.eleMonitor.data.Effect;
import com.bairock.eleMonitor.data.Linkage;
import com.bairock.eleMonitor.service.EffectService;

@Controller
@RequestMapping("/effect")
public class EffectController {

	@Autowired
	private EffectService effectService;
	
	@PostMapping("/add/{linkageId}")
	public String addEffect(@PathVariable long linkageId, @RequestParam long deviceId, @RequestParam float value) {
		Effect effect = effectService.addEffect(linkageId, deviceId, value);
		return "redirect:/linkage/" + effect.getLinkage().getDevice().getId() +"/" + linkageId;
	}
	
	@PostMapping("/edit/{effectId}")
	public String editEffect(@PathVariable long effectId, @RequestParam long deviceId, @RequestParam float value) {
		Effect effect = effectService.editEffect(effectId, deviceId, value);
		return "redirect:/linkage/" + effect.getLinkage().getDevice().getId() +"/" + effect.getLinkage().getId();
	}
	
	@GetMapping("/del/{effectId}")
	public String deleteEffect(@PathVariable long effectId) {
		Effect res = effectService.findById(effectId);
		Linkage linkage = res.getLinkage();
		linkage.removeEffect(res);
		
		effectService.delete(res);
		return "redirect:/linkage/" + res.getLinkage().getDevice().getId() +"/" + res.getLinkage().getId();
	}
}
