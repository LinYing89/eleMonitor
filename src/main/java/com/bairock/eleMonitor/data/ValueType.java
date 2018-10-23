package com.bairock.eleMonitor.data;

/**
 * 值类型
 * @author 44489
 *
 */
public enum ValueType {

	INT(0, "整数"),
	FLOAT(1, "浮点数");
	
	private int code;
	private String info;
	
	private ValueType(int code, String info) {
		this.code = code;
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
}
