package com.bairock.eleMonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EleController {

	@GetMapping("/ele")
	public String ele() {
		return "ele/ele";
	}
}
