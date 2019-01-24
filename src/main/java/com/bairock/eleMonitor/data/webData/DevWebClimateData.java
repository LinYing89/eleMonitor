package com.bairock.eleMonitor.data.webData;

public class DevWebClimateData extends DevWebData {
	// 是否有组
	private boolean haveDevGroup;
	// 组id
	private long devGroupId;
	// 是否正常
	private boolean normal;
	// 是否告警
	private boolean alarm;
	// 是否是开关量
	private boolean onOff;

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

	public boolean isOnOff() {
		return onOff;
	}

	public void setOnOff(boolean onOff) {
		this.onOff = onOff;
	}
}
