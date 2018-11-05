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
	private float value;
	//值字符串
	private String valueString;
	//是否有组
	private boolean haveDevGroup;
	//组id
	private long devGroupId;
	//是否正常
	private boolean normal;
	//是否告警
	private boolean alarm;
	//是否是开关量
	private boolean onOff;
	
	public long getDevId() {
		return devId;
	}
	public void setDevId(long devId) {
		this.devId = devId;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
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
	public String getValueString() {
		return valueString;
	}
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	public boolean isOnOff() {
		return onOff;
	}
	public void setOnOff(boolean onOff) {
		this.onOff = onOff;
	}
	
}
