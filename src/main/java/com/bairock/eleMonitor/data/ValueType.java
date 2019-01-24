package com.bairock.eleMonitor.data;

/**
 * 值类型
 * @author 44489
 *
 */
public enum ValueType {

	SWITCH(1, "开关量"),
	ALARM(2, "报警"),
	VALUE(3, "数值量"),
	ELE(4, "电力");
	
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
