package com.bairock.eleMonitor.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bairock.eleMonitor.data.User;
import com.bairock.eleMonitor.data.webData.UserLoginForm;
import com.bairock.eleMonitor.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/login")
	public String login(HttpServletResponse httpServletResponse, @ModelAttribute UserLoginForm userLoginForm, Model model) {
		User user = userRepository.findByNameAndPassword(userLoginForm.getUserName(), userLoginForm.getPassword());
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
}
