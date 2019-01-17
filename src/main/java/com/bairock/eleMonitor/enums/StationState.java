package com.bairock.eleMonitor.enums;

/**
 * 站点状态
 * @author 44489
 *
 */
public enum StationState {

	NORMAL(0, "正常"),
	OFFLINE(1, "离线"),
	ALARM(2, "报警"),
	UNSET(3, "未配置");
	
	private int code;
	private String message;
	
	private StationState(int code, String message) {
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
