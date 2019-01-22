package com.bairock.eleMonitor.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping(value = { "/", "/map" })
	public String map(Model model) {
		return "redirect:/loginSuccess";
//		return "redirect:/station/mystations/0";
	}

	@RequestMapping("/login")
	public String login(Model model, @CookieValue(value = "userName", required = false) String userName,
			@CookieValue(value = "password", required = false) String password) {
		if (null != userName) {
			model.addAttribute("userName", userName);
		} else {
			model.addAttribute("userName", "admin");
		}
		if (null != password) {
			model.addAttribute("password", password);
		} else {
			model.addAttribute("password", "admin");
		}

		return "login";
	}

	@RequestMapping("/loginSuccess")
	public String loginSuccess(HttpServletRequest request, @AuthenticationPrincipal User user) {
//		HttpSession session = request.getSession();
//		SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
//		String name = ((UserDetails)securityContext.getAuthentication().getPrincipal()).getUsername();
		if (hasRoleMap(user)) {
			return "redirect:/station/mystations/0";
		} else {
			com.bairock.eleMonitor.data.User myUser = userService.findByUsername(user.getUsername());
			if (myUser.getListStation().isEmpty()) {
				return "redirect:/station/mystations/0";
			} else {
				Station station = myUser.getListStation().get(0);
				return "redirect:/substation/" + station.getId() + "/0";
			}
		}
	}
	
//	@GetMapping("/logout")
//	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth != null){    
//			new SecurityContextLogoutHandler().logout(request, response, auth);
//		}
//		return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
//	}

	private boolean hasRoleMap(User user) {
		for (GrantedAuthority ga : user.getAuthorities()) {
			if (ga.getAuthority().equals("ROLE_MAP")) {
				return true;
			}
		}
		return false;
	}
}
