package com.bairock.eleMonitor.data.webData;

public class NetMessageSentResult {
	private int code = 0;
	private String time = "";
	private String sentMsg = "";
	private String message = "";

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getSentMsg() {
		return sentMsg;
	}
	public void setSentMsg(String sentMsg) {
		this.sentMsg = sentMsg;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
