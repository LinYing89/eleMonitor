package com.bairock.eleMonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping(value="/")
	public String login() {
		return "login";
	}
}
