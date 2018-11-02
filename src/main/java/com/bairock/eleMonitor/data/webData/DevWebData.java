package com.bairock.eleMonitor.data.webData;

/**
 * 发往网页的bean
 * @author 44489
 *
 */
public class DevWebData {

	//设备id
	private long devId;
	//设备值
	private String value;
	//是否有组
	private boolean haveDevGroup;
	//组id
	private long devGroupId;
	//是否正常
	private boolean normal;
	//是否告警
	private boolean alarm;
	
	public long getDevId() {
		return devId;
	}
	public void setDevId(long devId) {
		this.devId = devId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isHaveDevGroup() {
		return haveDevGroup;
	}
	public void setHaveDevGroup(boolean haveDevGroup) {
		this.haveDevGroup = haveDevGroup;
	}
	public long getDevGroupId() {
		return devGroupId;
	}
	public void setDevGroupId(long devGroupId) {
		this.devGroupId = devGroupId;
	}
	public boolean isNormal() {
		return normal;
	}
	public void setNormal(boolean normal) {
		this.normal = normal;
	}
	public boolean isAlarm() {
		return alarm;
	}
	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}
	
}
