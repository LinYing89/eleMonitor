package com.bairock.eleMonitor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EnCodePassword {
	public static void main(String[] args) {
		BCryptPasswordEncoder bcry = new BCryptPasswordEncoder();
		String password = "admin";
		String hashpw = bcry.encode(password);
		System.out.println(hashpw);
		System.out.println(bcry.matches("admin", hashpw));
	}
}
