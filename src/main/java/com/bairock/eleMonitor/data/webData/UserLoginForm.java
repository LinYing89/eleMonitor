package com.bairock.eleMonitor.data.webData;

public class UserLoginForm {

	private String username;
	private String password;
	private boolean rememberPsd;
	
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isRememberPsd() {
		return rememberPsd;
	}
	public void setRememberPsd(boolean rememberPsd) {
		this.rememberPsd = rememberPsd;
	}
	
	
}
