package com.bairock.eleMonitor.data;

/**
 * 值格式
 * @author 44489
 *
 */
public enum ValueFormat {

	/**
	 * 绝对值
	 */
	ABS(0, "绝对值");
	
	private int code;
	private String info;
	
	private ValueFormat(int code, String info) {
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
