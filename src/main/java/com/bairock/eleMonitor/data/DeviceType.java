package com.bairock.eleMonitor.data;

/**
 * 设备类型
 * @author 44489
 *
 */
public enum DeviceType {

	/**
	 * 控制类型
	 */
	CTRL(0, "控制类"),
	/**
	 * 值类型
	 */
	VALUE(1, "环境类");
	
	private int code;
	private String info;
	
	private DeviceType(int code, String info) {
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
