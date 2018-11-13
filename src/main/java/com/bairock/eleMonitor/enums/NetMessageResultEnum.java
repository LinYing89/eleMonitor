package com.bairock.eleMonitor.enums;

/**
 * 解析网络数据时的结果信息
 * @author 44489
 *
 */
public enum NetMessageResultEnum {

	UNKNOW(-1, "未知错误"),
	SUCCESS(0, "成功"),
	UNKNOW_MANAGER(1, "未知通信机"),
	UNKNOW_COLLECTOR(2, "未知采集终端"),
	DATA_LENGTH_ZERO(3, "数据长度为0"),
	ERR_LENGTH(4, "长度不匹配");
	
	private int code;
	private String message;
	
	private NetMessageResultEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
