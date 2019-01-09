package com.bairock.eleMonitor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@GetMapping(value = { "/", "/map" })
	public String map(Model model) {
		return "redirect:/station/mystations/0";
	}
	
	@RequestMapping("/login")
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
	
	@RequestMapping("/loginSuccess")
	public String loginSuccess(HttpServletRequest request) {
		HttpSession session = request.getSession();
		SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
		String name = ((UserDetails)securityContext.getAuthentication().getPrincipal()).getUsername();
		if(name.equals("admin")) {
			System.out.println("LoginController admin login");
			return "redirect:/map";
		}
		return "redirect:/map";
	}
}
