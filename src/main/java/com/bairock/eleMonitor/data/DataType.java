package com.bairock.eleMonitor.data;

/**
 * 采集终端数据类型
 * @author 44489
 *
 */
public enum DataType {

	/**
	 * 遥信
	 */
	YAO_XIN(0, "遥信"),
	/**
	 * 遥测
	 */
	YAO_CE(1, "遥测"),
	/**
	 * 遥脉
	 */
	YAO_MAI(2, "遥脉"),
	/**
	 * 遥控
	 */
	YAO_KONG(3, "遥控");
	
	private int code;
	private String info;
	
	private DataType(int code, String info) {
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
