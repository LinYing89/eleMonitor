package com.bairock.eleMonitor.data;

/**
 * 门卡权限
 * @author 44489
 *
 */
public enum DoorCardAuthority {

	/**
	 * 允许
	 */
	ENABLE(0, "允许"),
	/**
	 * 禁止
	 */
	DISABLE(1, "禁止");
	
	private int code;
	private String text;
	
	DoorCardAuthority(int code, String text){
		this.code = code;
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
