package com.bairock.eleMonitor.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bairock.eleMonitor.data.Station;
import com.bairock.eleMonitor.data.User;
import com.bairock.eleMonitor.data.webData.UserLoginForm;
import com.bairock.eleMonitor.service.StationService;
import com.bairock.eleMonitor.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private StationService stationService;
	
	@PostMapping("/login")
	public String login(HttpServletResponse httpServletResponse, @ModelAttribute UserLoginForm userLoginForm, Model model) {
		User user = userService.findByNameAndPassword(userLoginForm.getUserName(), userLoginForm.getPassword());
		if(null != user) {
			
			//保存名称cookie
			Cookie cookieName = new Cookie("userName", userLoginForm.getUserName());
			cookieName.setPath("/");
			cookieName.setMaxAge(Integer.MAX_VALUE);
			httpServletResponse.addCookie(cookieName);
			
			if(userLoginForm.isRememberPsd()) {
				//保存密码cookie
				Cookie cookiePsd = new Cookie("password", userLoginForm.getPassword());
				cookiePsd.setPath("/");
				cookiePsd.setMaxAge(Integer.MAX_VALUE);
				httpServletResponse.addCookie(cookiePsd);
			}else {
				//删除密码cookie
				Cookie cookiePsd = new Cookie("password", "");
				cookiePsd.setPath("/");
				cookiePsd.setMaxAge(0);
				httpServletResponse.addCookie(cookiePsd);
			}
			
			if(userLoginForm.getUserName().equals("admin")) {
				//管理员
				return "redirect:/station/mystations/" + user.getId() + "/true";
			}else {
				return "redirect:/station/mystations/" + user.getId() + "/false";
			}
		}else {
			//用户不存在
			model.addAttribute("error", "用户名或密码错误");
			return "login";
		}
	}
	
	@GetMapping("/user/page/all")
	public String getUsers(Model model) {
		List<Station> listStation = stationService.findAll();
		List<User> list = userService.findAll();
		model.addAttribute("listUser", list);
		model.addAttribute("listStation", listStation);
		return "sysset/users";
	}
	
	@PostMapping("/user/add")
	public String addUser(@ModelAttribute User user) {
		userService.addUser(user);
		return "redirect:/user/page/all";
	}
	
	@GetMapping("/user/del/{userId}")
	public String delUser(@PathVariable long userId) {
		userService.deleteUser(userId);
		return "redirect:/user/page/all";
	}
	
	@PostMapping("/user/edit")
	public String editUser(@ModelAttribute User user) {
		userService.editUser(user);
		return "redirect:/user/page/all";
	}
}
