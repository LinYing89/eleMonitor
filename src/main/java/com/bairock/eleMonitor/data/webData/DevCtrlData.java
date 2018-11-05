package com.bairock.eleMonitor.data.webData;

public class DevCtrlData {

	//设备id
	private long devId;
	//设备操作, 开,关
	private int option;
	
	public long getDevId() {
		return devId;
	}
	public void setDevId(long devId) {
		this.devId = devId;
	}
	public int getOption() {
		return option;
	}
	public void setOption(int option) {
		this.option = option;
	}
	
}
