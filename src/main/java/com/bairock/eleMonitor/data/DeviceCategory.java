package com.bairock.eleMonitor.data;

/**
 * 设备类别
 * @author 44489
 *
 */
public enum DeviceCategory {

	DEFAULT(0, "无"),
	VOLTAGE(1, "电压"),
	CURRENT(2, "电流"),
	FACTOR(3, "功率因数"),
	RESIDUE_ELE(4, "剩余电流"),
	DOOR(10, "门控");
	
	private int code;
	private String name;
	
	DeviceCategory(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
