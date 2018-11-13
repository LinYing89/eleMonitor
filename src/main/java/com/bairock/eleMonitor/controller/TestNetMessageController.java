package com.bairock.eleMonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/netMessage")
public class TestNetMessageController {

	@GetMapping("/received")
	public String getReceivedPage() {
		return "test/netMessage";
	}
	
	@GetMapping("/sent")
	public String getSentPage() {
		return "test/sent";
	}
}
