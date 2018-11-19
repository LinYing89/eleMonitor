package com.bairock.eleMonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping(value="/")
	public String login(Model model, @CookieValue(value="userName", required=false) String userName, @CookieValue(value="password", required=false) String password) {
		if(null != userName) {
			model.addAttribute("userName", userName);
		}else {
			model.addAttribute("userName", "admin");
		}
		if(null != password) {
			model.addAttribute("password", password);
		}else {
			model.addAttribute("password", "admin");
		}

		return "login";
	}
}
